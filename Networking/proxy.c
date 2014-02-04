#include <stdarg.h>
#include "csapp.h"

FILE *lg;

void plog(const char * format, ...) {
    va_list ap1, ap2;
    time_t timer;
    char buffer[20];
    struct tm* tm_info;

    time(&timer);
    tm_info = localtime(&timer);

    strftime(buffer, 20, "%Y:%m:%d %H:%M:%S", tm_info);

    va_start(ap1, format);
    va_copy(ap2, ap1);

    fprintf(lg, "%s - ", buffer);
    vfprintf(lg, format, ap1);
    va_end(ap1); 
    
    printf("%s - ", buffer);
    vprintf(format, ap2);
    va_end(ap2);
}

void onint() {
    plog("Killed by interrupt\n");
    if (lg != NULL) {
        Fclose(lg);
    }
    exit(0);
}

void echo(int connfd) {
    size_t n;
    char buf[MAXLINE];
    rio_t rio;
    
    Rio_readinitb(&rio, connfd);
    while((n = Rio_readlineb(&rio, buf, MAXLINE)) != 0) {
        printf("server received %d bytes\n", (int)n);
        Rio_writen(connfd, buf, n);
    }
}

int main(int argc, char **argv) {
    int listenfd, connfd, port, clientlen;
    struct sockaddr_in clientaddr;
    struct hostent *hp;
    char *haddrp;
    if (argc != 2) {
        fprintf(stderr, "usage: %s <port>\n", argv[0]);
        exit(0);
    }
    signal(SIGINT, onint);
    lg = Fopen("proxy.log", "w");
    if (lg == NULL) {
        plog("Failed to open proxy.log\n");
        exit(1);
    }
    plog("Proxy server starting.\n");
    port = atoi(argv[1]);
    plog("Opening socket...\n");

    listenfd = Open_listenfd(port);
    plog("Opened socket on port %d.\n", port);
    while (1) {
        plog("Listening for connections...\n");
        clientlen = sizeof(clientaddr);
        connfd = Accept(listenfd, (SA *)&clientaddr, &clientlen);

        /* determine the domain name and IP address of the client */
        hp = Gethostbyaddr((const char *)&clientaddr.sin_addr.s_addr,
                           sizeof(clientaddr.sin_addr.s_addr), AF_INET);
        haddrp = inet_ntoa(clientaddr.sin_addr);
        plog("Connected to %s (%s).\n", hp->h_name, haddrp);
        
        echo(connfd);
        Close(connfd);
    }
    exit(0);
}
