// UDP_Communication_Framework.cpp : Defines the entry point for the console application.
//

#pragma comment(lib, "ws2_32.lib")
#include "stdafx.h"
#include <winsock2.h>
#include <iostream>
#include <string>
#include "ws2tcpip.h"
#include <sys/stat.h>
#include <fstream>

using namespace std;

#define TARGET_IP		"25.39.162.87"
#define BUFFERS_LEN		1024
#define TARGET_PORT		5555
#define LOCAL_PORT		8888

#define FILENAME		"morty.png"
#define START			"START"
#define END				"END"
#define SEPARATOR		" "



void InitWinsock()
{
	WSADATA wsaData;
	WSAStartup(MAKEWORD(2, 2), &wsaData);
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
	//**********************************************************************
	char buffer_tx[BUFFERS_LEN];

	sockaddr_in addrDest;
	addrDest.sin_family = AF_INET;
	addrDest.sin_port = htons(TARGET_PORT);
	InetPton(AF_INET, _T(TARGET_IP), &addrDest.sin_addr.s_addr);


	// -------------------------- FIRST PACKET
	struct stat results;

	if (stat(FILENAME, &results) != 0) {
		printf("Error ocured while opening file");
		getchar();
		return 0;
	}

	string firstPacket = START;
	firstPacket += SEPARATOR;
	firstPacket += FILENAME;
	firstPacket += SEPARATOR;
	firstPacket += to_string(BUFFERS_LEN);
	firstPacket += SEPARATOR;
	firstPacket += to_string(results.st_size);

	const char* firstPacketChar = firstPacket.c_str();

	strncpy_s(buffer_tx, firstPacketChar, BUFFERS_LEN);
	sendto(socketS, buffer_tx, strlen(buffer_tx), 0, (sockaddr*)&addrDest, sizeof(addrDest));
	memset(buffer_tx, 0, BUFFERS_LEN);

	// --------------------------------- EO FIRST PACKET

	ifstream myFile(FILENAME, ios::in | ios::binary);
	for(int i = 0; i < results.st_size/BUFFERS_LEN; i++) {
		myFile.read(buffer_tx, BUFFERS_LEN - 1);
		printf("PACKET %i %i :\n <<%s>>\n",i, sizeof(buffer_tx), buffer_tx);
		sendto(socketS, buffer_tx, sizeof(buffer_tx), 0, (sockaddr*)&addrDest, sizeof(addrDest));
		memset(buffer_tx, 0, BUFFERS_LEN);
		Sleep(500);
	}
	char yolo[BUFFERS_LEN];
	memset(yolo, 0, (results.st_size - (results.st_size / BUFFERS_LEN)));
	myFile.read(yolo, (results.st_size - (results.st_size / BUFFERS_LEN)));
	printf("LAST PACKET %i :\n <<%s>>\n", sizeof(yolo), yolo);
	sendto(socketS, yolo, (results.st_size - (results.st_size / BUFFERS_LEN)), 0, (sockaddr*)&addrDest, sizeof(addrDest));

	myFile.close();

	closesocket(socketS);
	getchar(); //wait for press Enter
	return 0;
}
