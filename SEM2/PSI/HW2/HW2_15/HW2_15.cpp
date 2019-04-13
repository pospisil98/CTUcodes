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
#include "md5\hashlibpp.h"
#include "md5\hl_hashwrapper.h"
#include "md5\hl_md5wrapper.h"
#include "md5\hl_md5.h"

using namespace std;

#define TARGET_IP		"147.32.221.18"
#define BUFFERS_LEN		1024
#define TARGET_PORT		5555
#define LOCAL_PORT		8888

#define FILENAME		"reallybig.jpg"
#define START			"START"
#define END				"END"
#define SEPARATOR		" "

#define DATALEN			1016
#define CRCLEN			4
#define ORDERLEN		4

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

	ifstream myFile(FILENAME, ios::in | ios::binary);
	int packetNumber = 0;
	int tempReadCount;
	int size;
	uint32_t crc = 0;
	boolean md5Correct = false;
	boolean crcCorrect = false;

	/* uncomment for packet error simulation
	boolean wrong = true;
	boolean makeCRCWrong = true;
	*/

	while (!md5Correct) {
		while (!myFile.eof()) {
			// read data from file into start of data part
			myFile.read(&buffer_tx[ORDERLEN + CRCLEN], DATALEN);
			tempReadCount = (int)myFile.gcount();

			
			/* //simulate error on 5 packet
			if (packetNumber == 5 && makeCRCWrong) {
				makeCRCWrong = false;
				crc = 1;
			} else {
				crc = 0;
				crc = crc32c(crc, &buffer_tx[ORDERLEN + CRCLEN], DATALEN);
			}*/
			
			// caluclate crc and store it
			crc = 0;
			crc = crc32c(crc, &buffer_tx[ORDERLEN + CRCLEN], DATALEN);
			

			// copy crc and packet order into send buffer
			memcpy(&buffer_tx[0], &packetNumber, sizeof(packetNumber));
			memcpy(&buffer_tx[ORDERLEN], &crc, sizeof(crc));

			// ---------------- CHECK
			/*printf("PACKET %i :", packetNumber);
			for (int i = 0; i < BUFFERS_LEN; i++) {
			printf("%c", buffer_tx[i]);
			}
			printf("\n\n");*/
			// ---------------- EOCHECK

			while (!crcCorrect) {
				/* uncomment for error on 5 packet
				if (packetNumber == 5 && !wrong) {
					crc = 0;
					crc = crc32c(crc, &buffer_tx[ORDERLEN + CRCLEN], DATALEN);
					memcpy(&buffer_tx[0], &packetNumber, sizeof(packetNumber));
					memcpy(&buffer_tx[ORDERLEN], &crc, sizeof(crc));
				}

				if (!makeCRCWrong) {
					wrong = false;
				}*/


				cout << "Sending " << packetNumber << endl;
				// send packet
				sendto(socketS, buffer_tx, BUFFERS_LEN, 0, (sockaddr*)&addrDest, sizeof(addrDest));
				//Sleep(100);

				// set timeout to x sec
				struct timeval tv;
				tv.tv_sec = 50;
				tv.tv_usec = 0;
				if (setsockopt(socketS, SOL_SOCKET, SO_RCVTIMEO, (char*)&tv, sizeof(tv)) < 0) {
					perror("Error");
				}

				//Recieve state message from 
				string message;
				//printf("\n------WAITING FOR CRC MESSAGE------\n");
				memset(buffer_rx, 0, BUFFERS_LEN);
				if ((size = recvfrom(socketS, buffer_rx, BUFFERS_LEN, 0, (sockaddr*)&from, &fromlen)) < 0) {
					//timeout
					crcCorrect = false;
				}
				else {
					cout << "RECIEVED: " << buffer_rx << endl;

					//printf("\nSIZE: %d\n", size);

					if (size == SOCKET_ERROR) {
						int err = WSAGetLastError();
						printf("\nERROR: %d\n", err);
					}

					string arr[2];
					int i = 0;
					stringstream ssin(buffer_rx);

					while (ssin.good() && i < 2) {
						ssin >> arr[i];
						i++;
					}

					// if message was confirmation
					if (arr[0] == "1" && stoi(arr[1]) == packetNumber) {
						crcCorrect = true;
					}

					//printf("\nCRCORRECT: %d\n", crcCorrect);
				}
			}


			memset(buffer_tx, 0, BUFFERS_LEN);
			packetNumber++;
			crcCorrect = false;
		}

		//Recieve state message from 
		string message;
		printf("\n------WAITING FOR CRC MESSAGE------\n");
		memset(buffer_rx, 0, BUFFERS_LEN);

		// set timeout to x sec
		struct timeval tv;
		tv.tv_sec = 5;
		tv.tv_usec = 0;
		if (setsockopt(socketS, SOL_SOCKET, SO_RCVTIMEO, (char*)&tv, sizeof(tv)) < 0) {
			perror("Error");
		}

		if ((size = recvfrom(socketS, buffer_rx, 1, 0, (sockaddr*)&from, &fromlen)) < 0) {
			//timeout
			crcCorrect = false;
		}
		else {
			//printf("\nSIZE: %d\n", size);

			if (size == SOCKET_ERROR) {
				int err = WSAGetLastError();
				printf("\nERROR: %d\n", err);
			}

			// if message was confirmation
			if (buffer_rx[0] == '1') {
				md5Correct = true;
			}

			printf("\nCRCORRECT: %d\n", crcCorrect);
		}
	}

	printf("\n----END----\n");
	myFile.close();
	closesocket(socketS);
	getchar(); //wait for press Enter
	return 0;
}
