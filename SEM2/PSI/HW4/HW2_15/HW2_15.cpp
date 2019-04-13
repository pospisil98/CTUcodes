#pragma comment(lib, "ws2_32.lib")

#include "stdafx.h"
#include <winsock2.h>
#include <iostream>
#include <string>
#include "ws2tcpip.h"
#include <sys/stat.h>
#include <stddef.h>
#include <sstream>
#include <stdint.h>
#include <fstream>
#include <time.h>
#include <vector>
#include "md5\hashlibpp.h"
#include "md5\hl_hashwrapper.h"
#include "md5\hl_md5wrapper.h"
#include "md5\hl_md5.h"

using namespace std;

#define TARGET_IP		"10.242.48.92"
#define BUFFERS_LEN		1024
#define TARGET_PORT		5555
#define LOCAL_PORT		8888

#define FILENAME		"bubu.pdf"
#define START			"START"
#define END				"END"
#define SEPARATOR		" "

#define DATALEN			1016
#define CRCLEN			4
#define ORDERLEN		4
#define TIMEOUT			200

/* CRC-32 (Ethernet, ZIP, etc.) polynomial in reversed bit order. */
#define POLY 0xedb88320

uint32_t crc32c(uint32_t crc, char *buf, size_t len)
{
	int k;

	crc = ~crc;
	while (len--) {
		crc ^= *buf++;
		for (k = 0; k < 8; k++)
			crc = crc & 1 ? (crc >> 1) ^ POLY : crc >> 1;
	}
	return ~crc;
}

void InitWinsock()
{
	WSADATA wsaData;
	WSAStartup(MAKEWORD(2, 2), &wsaData);
}

string getMd5Hash(string filename) {
	hashwrapper *hashWrap = new md5wrapper();
	string md5Hash;

	try {
		md5Hash = hashWrap->getHashFromFile(filename);
		cout << md5Hash << endl;
	}
	catch (hlException &e) {
		printf("Erroro ocured");
		cout << e.error_message() << endl;
	}

	delete hashWrap;

	return md5Hash;
}

string createFirstPacketString(const char* filename, int packetsize)
{
	// -------------------------- FIRST PACKET
	struct stat results;

	if (stat(filename, &results) != 0) {
		printf("Error ocured while opening file");
		getchar();
		return 0;
	}

	string firstPacket = START;
	firstPacket += SEPARATOR;
	firstPacket += FILENAME;
	firstPacket += SEPARATOR;
	firstPacket += to_string(packetsize);
	firstPacket += SEPARATOR;
	firstPacket += to_string(results.st_size);
	firstPacket += SEPARATOR;
	firstPacket += getMd5Hash(FILENAME);

	return firstPacket;
}

//**********************************************************************
int main()
{
	SOCKET socketS;

	InitWinsock();

	struct sockaddr_in local;
	struct sockaddr_in from;

	int fromlen = sizeof(from);
	local.sin_family = AF_INET;
	local.sin_port = htons(LOCAL_PORT);
	local.sin_addr.s_addr = INADDR_ANY;


	socketS = socket(AF_INET, SOCK_DGRAM, 0);
	if (bind(socketS, (sockaddr*)&local, sizeof(local)) != 0) {
		printf("Binding error!\n");
		getchar(); //wait for press Enter
		return 1;
	}

	sockaddr_in addrDest;
	addrDest.sin_family = AF_INET;
	addrDest.sin_port = htons(TARGET_PORT);
	InetPton(AF_INET, _T(TARGET_IP), &addrDest.sin_addr.s_addr);

	//**********************************************************************

	char buffer_tx[BUFFERS_LEN];
	char buffer_rx[BUFFERS_LEN];
	int size;
	int maxDatarate;

	// get data for first packet and send them
	string firstPacket = createFirstPacketString(FILENAME, BUFFERS_LEN);
	const char* firstPacketChar = firstPacket.c_str();
	strncpy_s(buffer_tx, firstPacketChar, firstPacket.length());
	sendto(socketS, buffer_tx, firstPacket.length(), 0, (sockaddr*)&addrDest, sizeof(addrDest));
	memset(buffer_tx, 0, BUFFERS_LEN);
	
	
	/*
	PACKET STRUCTURE
	4B - packet order
	4B - CRC
	1016B - DATA
	*/

	// set timeout to x sec
	struct timeval tv;
	tv.tv_sec = TIMEOUT;
	tv.tv_usec = 0;
	if (setsockopt(socketS, SOL_SOCKET, SO_RCVTIMEO, (char*)&tv, sizeof(tv)) < 0) {
		perror("Error");
	}


	ifstream myFile(FILENAME, ios::in | ios::binary);
	const int windowSize = 10;
	char window[windowSize * BUFFERS_LEN];
	boolean acknowledged[windowSize];
	int packetNumber = 0;
	uint32_t crc = 0;
	boolean md5Correct = false;
	char tempRead[DATALEN];
	char tempPacket[DATALEN + CRCLEN + ORDERLEN];
	
	for (int i = 0; i < windowSize; i++)
	{
		acknowledged[i] = true;
	}

	memset(window, 0, windowSize * BUFFERS_LEN);

	while (!md5Correct) {
		while (!myFile.eof()) {
			for (int i = 0; i < windowSize; i++)
			{				
				if (acknowledged[i] == true) {
					memset(&window[i * BUFFERS_LEN], 0, BUFFERS_LEN);
					if (!myFile.eof()) {
						//printf("READING NEW %d  ", packetNumber);
						//myFile.read(tempRead, DATALEN);
						myFile.read(&window[ORDERLEN + CRCLEN + i * BUFFERS_LEN], DATALEN);
						crc = 0;
						crc = crc32c(crc, &window[ORDERLEN + CRCLEN + i * BUFFERS_LEN], DATALEN);
						//printf("CRC: %d\n", crc);

						acknowledged[i] = false;

						// copy crc and packet order into temp packet
						memcpy(&window[0 + (i * BUFFERS_LEN)], &packetNumber, sizeof(packetNumber));
						memcpy(&window[ORDERLEN + (i * BUFFERS_LEN)], &crc, sizeof(crc));					

						packetNumber++;
					} else {
						acknowledged[i] = true;
					}
				}
			}
			
			// sending
			for (int k = 0; k < windowSize; k++) {
				if (acknowledged[k] == false)
				{
					//printf("SENDING %d\n", k);
					sendto(socketS, &window[k * BUFFERS_LEN], BUFFERS_LEN, 0, (sockaddr*)&addrDest, sizeof(addrDest));
				}
			}

			string message;
			for (int i = 0; i < windowSize; i++) {
				memset(buffer_rx, 0, BUFFERS_LEN);

				size = recvfrom(socketS, buffer_rx, BUFFERS_LEN, 0, (sockaddr*)&from, &fromlen);

				if (size == SOCKET_ERROR) {
					int err = WSAGetLastError();
					printf("\nERROR: %d\n", err);
				} else {
					string arr[2];
					int j = 0;
					stringstream ssin(buffer_rx);

					while (ssin.good() && j < 2) {
						ssin >> arr[j];
						j++;
					}

					// if message was confirmation
					int ackNum = 0;
					if (arr[0] == "1") {
						ackNum = stoi(arr[1]) % windowSize;
						acknowledged[ackNum] = true;
						//printf("RECIEVED OK");
					} else {
						//printf("RECIEVED NOK");
					}
					//printf(" %d\n", ackNum);
				}
			}
		}


		string end = "END";
		memset(buffer_rx, 0, BUFFERS_LEN); 
		strncpy_s(buffer_rx, end.c_str(), end.length());
		sendto(socketS, buffer_rx, BUFFERS_LEN, 0, (sockaddr*)&addrDest, sizeof(addrDest));
		string message;
		printf("\n------WAITING FOR MD5 MESSAGE------\n");
		memset(buffer_rx, 0, BUFFERS_LEN);

		size = recvfrom(socketS, buffer_rx, 1, 0, (sockaddr*)&from, &fromlen);

		if (size == SOCKET_ERROR) {
			int err = WSAGetLastError();
			printf("\nERROR: %d\n", err);
		}

		// if message was confirmation
		if (buffer_rx[0] == '1') {
			md5Correct = true;
		}
	}

	printf("\n----END----\n");
	myFile.close();
	closesocket(socketS);
	getchar(); //wait for press Enter
	return 0;
}
