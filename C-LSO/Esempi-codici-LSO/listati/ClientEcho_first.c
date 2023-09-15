void ClientEcho(FILE * filein, int socket) 
{
    char sendbuff[MAXLINE+1], recvbuff[MAXLINE+1];
    int nread; 
    while (fgets(sendbuff, MAXLINE, filein) != NULL) {
        FullWrite(socket, sendbuff, strlen(sendbuff)); 
        nread = read(socket, recvbuff, strlen(sendbuff));        
        recvbuff[nread] = 0;
        fputs(recvbuff, stdout);
    }
    return;
}
