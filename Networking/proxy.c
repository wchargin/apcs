#include <stdarg.h>
#include "csapp.h"

FILE *lg;
sem_t logmutex;

void plog(const char * format, ...) {
    /* prints formatted string to stdout and the log file */
    va_list ap1, ap2;
    time_t timer;
    char buffer[20];
    struct tm* tm_info;

    sem_wait(&logmutex);

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

    sem_post(&logmutex);
}

void onint() {
    /* run on ^C interrupt to clean up */
    plog("Killed by interrupt.\n");
    if (lg != NULL) {
        Fclose(lg);
    }
    exit(0);
}

typedef struct {
    int clientfd;
    int serverfd;
} relayinfo;

void *dorelay(void *infoptr) {
    /* argument should be relayinfo* pointing to information struct */
    /* catches SIGUSR1 when it is time to die */
    size_t n;
    char buf[MAXLINE];
    rio_t rio;
    int clientfd, serverfd;

    relayinfo info = *(relayinfo *) infoptr;
    clientfd = info.clientfd;
    serverfd = info.serverfd;

    plog("Opening server-client relay.\n");

    rio_readinitb(&rio, info.serverfd);
    while ((n = Rio_readlineb(&rio, buf, MAXLINE)) != 0) {
        /* got data from server; echo back to client */
        plog("[SERV] Data incoming: %d bytes\n", n);
        if (rio_writen(info.clientfd, buf, n) != n) {
            break;
        } 
    } 
    plog("Closing server-client relay.\n");

    /* use close instead of Close - don't complain on error */
    /* (if it's already closed that's okay) */
    close(clientfd);
    close(serverfd);
}

void *handleconn(void *connptr) {
    /* argument should be int* pointing tp connection file descriptor */
    size_t n;
    char bufcl[MAXLINE], bufsv[MAXLINE];
    rio_t rio;
    relayinfo info;
    pthread_t relay;
    int connfd = *(int *) connptr;
    int serverfd = 0;

    rio_readinitb(&rio, connfd);
    while((n = rio_readlineb(&rio, bufcl, MAXLINE)) != 0) {
        plog("[DATA] %s", bufcl);
        if (!serverfd) {
            char method[MAXLINE], uri[MAXLINE], version[MAXLINE], host[MAXLINE];
            int port = 80;
            if (sscanf(bufcl, "%s %s %s", method, uri, version) != 3) {
                plog("String parsing failed; exiting connection thraed.\n");
                return;
            }
            sscanf(uri, "%*[^:]://%[^/]", host); 
            sscanf(host, "%[^:]:%d", host, &port);

            plog("Configuring connection to host %s on port: %d\n", host, port);

            info.clientfd = connfd;
            info.serverfd = serverfd = Open_clientfd(host, port);
            Pthread_create(&relay, NULL, dorelay, &info);
        }
        rio_writen(serverfd, bufcl, n);
    }
    plog("Connection closed.\n");
    close(connfd);
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
    sem_init(&logmutex, 1, 1); /* addr, shared, initial value */
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
        pthread_t tid;

        /* determine the domain name and IP address of the client */
        hp = Gethostbyaddr((const char *)&clientaddr.sin_addr.s_addr,
                           sizeof(clientaddr.sin_addr.s_addr), AF_INET);
        haddrp = inet_ntoa(clientaddr.sin_addr);
        plog("Connected to %s (%s).\n", hp->h_name, haddrp);
        
        Pthread_create(&tid, NULL, handleconn, &connfd);
    }
    exit(0);
}
