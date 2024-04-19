/* wwwd.c
 * 
 * Copyright (C) 2005 Simone Piccardi
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
/****************************************************************
 *
 * Program wwwd 
 * Elementary WWW server (port 80)
 *
 * Author: Simone Piccardi
 * Mar. 2005
 *
 * Usage: wwwd -h give all info
 *
 ****************************************************************/
/* 
 * Include needed headers
 */
#define _GNU_SOURCE
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>	 /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <arpa/inet.h>   /* IP addresses conversion utilities */
#include <sys/socket.h>  /* socket constants, types and functions */
#include <time.h>        /* date and time constants, types and functions */
#include <syslog.h>      /* syslog system functions */
#include <signal.h>      /* signal constants, types and functions */
#include <errno.h>       /* error definitions and routines */
#include <string.h>      /* C strings library */
#include <dirent.h>      /* directory operation constants and functions */

#include "Gapil.h"

/* 
 * Function and globals definitions
 */
#define BACKLOG 10
#define MAXLINE 256
int demonize  = 1;  /* daemon use option: default is daemon */
int debugging = 0;  /* debug info printing option: default is no debug */
struct code_page {
    char * code; 
    char * name;
    char * body;
};

void usage(void);
void ServPage(int sockfd);
void PrintErr(char * error);
void print_headers(FILE *file, struct code_page code);
void print_error(FILE *file, struct code_page page, char * string);

/*
 * Main program
 */int main(int argc, char *argv[])
{
    /* 
     * Variables definition  
     */
    int list_fd, conn_fd;
    int compat = 0;
    int reroot = 0;
    int reuse = 1;
    char * rootdir;
    pid_t pid;
    struct sockaddr_in cli_add;
    socklen_t len;
    char debug[MAXLINE], ipaddr[20];
    /*
     * Input section: decode parameters passed in the calling 
     * Use getopt function
     */
    int i;
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hwdicr:")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':  
	    printf("Wrong -h option use\n");
	    usage();
	    return(0);
	    break;
	case 'i':
	    demonize = 0;
	    break;
	case 'c':
	    compat = 1;
	    break;
	case 'd':
	    debugging = 1;
	    break;
	case 'w':
	    reuse = 0;
	    break;
	case 'r':
	    reroot = 1;
	    rootdir = optarg;
	    break;
	case '?':   /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:    /* should not reached */
	    usage();
	}
    }
    /* ***********************************************************
     * 
     *		 Options processing completed
     *
     *		      Main code beginning
     * 
     * ***********************************************************/
    /* Main code begin here */
    if (compat) {                             /* install signal handler */
	Signal(SIGCHLD, HandSigCHLD);         /* non restarting handler */
    } else {
	SignalRestart(SIGCHLD, HandSigCHLD);  /* restarting handler */
    }
    /* create and bind socket */
    if ( (list_fd = sockbindopt(argv[optind], "www", 6, 
				SOCK_STREAM, reuse)) < 0) {
	return 1;
    }   
    /* chroot if requested */
    if (reroot) {
	if (chdir(rootdir)) {
	    perror("Cannot find directory to chroot");
	    exit(1);
	}
	if (chroot(rootdir)) {
	    perror("Cannot chroot");
	    exit(1);
	}
    }
    /* release privileges and go daemon */
    if (setgid(65534) !=0) { /* first give away group privileges */
	perror("cannot give away group privileges");
	exit(1);
    }
    if (setuid(65534) !=0) { /* and only after user ... */
	perror("cannot give away user privileges");
	exit(1);
    }
    if (demonize) {          /* go daemon */
        openlog(argv[0], 0, LOG_DAEMON); /* open logging */
 	if (daemon(0, 0) != 0) {
	    perror("cannot start as daemon");
	    exit(1);
	}
    }
    /* main body */
    if (listen(list_fd, BACKLOG) < 0 ) {
	PrintErr("listen error");
	exit(1);
    }
    /* handle echo to client */
    while (1) {
	/* accept connection */
	len = sizeof(cli_add);
	while (((conn_fd = accept(list_fd, (struct sockaddr *)&cli_add, &len)) 
		< 0) && (errno == EINTR)); 
	if (conn_fd < 0) {
	    PrintErr("accept error");
	    exit(1);
	}
	if (debugging) {
	    inet_ntop(AF_INET, &cli_add.sin_addr, ipaddr, sizeof(ipaddr));
	    snprintf(debug, MAXLINE, "Accepted connection form %s\n", ipaddr);
	    if (demonize) {
		syslog(LOG_DEBUG, debug);
	    } else {
		printf("%s", debug);
	    }
	}
	/* fork to handle connection */
	if ( (pid = fork()) < 0 ){
	    PrintErr("fork error");
	    exit(1);
	}
	if (pid == 0) {      /* child */
	    close(list_fd);          /* close listening socket */   
	    ServPage(conn_fd);       /* handle echo */
	    if (debugging) {
		snprintf(debug, MAXLINE, "Closed connection %s\n", ipaddr);
		if (demonize) {
		    syslog(LOG_DEBUG, debug);
		} else {
		    printf("%s", debug);
		}
	    }
	    exit(0);
	} else {             /* parent */
	    close(conn_fd);          /* close connected socket */
	}
    }
    /* normal exit, never reached */
    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) 
{
    printf("Elementary echo server\n");
    printf("Usage:\n");
    printf("  wwwd [-h] \n");
    printf("  -h	   print this help\n");
    printf("  -d	   write debug info\n");
    printf("  -i	   use interactively\n");
    printf("  -c	   disable BSD semantics\n");
    printf("  -r /path	   chroot on /path\n");
    exit(1);
}
/*
 * routine to handle echo for connection
 */
void ServPage(int sockfd) 
{
    char buffer[MAXLINE];
    char outbuf[1024];
    FILE *sock, *file;
    char *line, *copy, *method, *ptr, *filename, *version;
    char *methods[] = { "GET", "HEAD", NULL };
    struct code_page codes[] = {
	{ "200", "OK", "%s"},
	{ "400", "Bad Request", 
          "Your browser sent a request that this server could not understand."
	  "<P>The request line<P>%s<P> is invalid following the protocol<P>"}, 
	{ "404", "Not Found", 
          "The requested URL %s was not found on this server.<P>"},
	{ "500", "Internal Server Error", 
          "We got an error processing your request.<P>Error is: %s<P>"},
	{ "405", "Method Not Allowed", "Method %s not allowed.<P>"},
	{ "403", "Forbidden", "You cannot access %s.<P>"}
    };
    int nleft;
    int i;

    sock = fdopen(sockfd, "w+");
    /* main loop, reading 0 char means client close connection */
    line = fgets(buffer, MAXLINE, sock);
    if (line == NULL) {
	PrintErr("Errore in lettura");
	return;
    }
    /* parsing first line, getting method and filename */
    copy = strndupa(line, MAXLINE);
    if ((method = strtok_r(copy, " ", &ptr)) == NULL) {
	print_headers(sock, codes[1]);
	print_error(sock, codes[1], line);
	return;
    }
    if ((filename = strtok_r(NULL, " ", &ptr)) == NULL) {
	print_headers(sock, codes[1]);
	print_error(sock, codes[1], line);
	return;
    }
    if ((version = strtok_r(NULL, " ", &ptr)) == NULL) {
	print_headers(sock, codes[1]);
	print_error(sock, codes[1], line);
	return;
    }
    i = 0;
    while ( (ptr = methods[i]) != NULL) {
	if ( (strncmp(ptr, method, strlen(ptr)) == 0)) {
	    break;
	}
	i++;
    }
    if (i>=2) {
	print_headers(sock, codes[4]);
	print_error(sock, codes[4], method);
	return;
    }

    while (strcmp(line,"\r\n")) {
	line = fgets(buffer, MAXLINE, sock);
    }

    if ( (file = fopen(filename, "r")) == NULL) {
	if ( (errno == EACCES)||(errno == EPERM) ) {
	    print_headers(sock, codes[5]);
	    print_error(sock, codes[5], filename);
	} else {
	    print_headers(sock, codes[2]);
	    print_error(sock, codes[2], filename);
	}
	return;
    }
    print_headers(sock, codes[0]);
    while (!feof(file)) {
	if ( (nleft = full_fread(file, outbuf, 1024)) != 0) {
	    if (ferror(file)) {
		strncpy(buffer, strerror(errno), MAXLINE);
		print_headers(sock, codes[3]);
		print_error(sock, codes[3], buffer);
		return;
	    }
	}
	if (full_fwrite(sock, outbuf, 1024-nleft) != 0) {
	    if (ferror(file)) {
		strncpy(buffer, strerror(errno), MAXLINE);
		print_headers(sock, codes[3]);
		print_error(sock, codes[3], buffer);
		return;
	    }   
	}
    }
    fclose(file);
    fclose(sock);
    return;
}
/*
 * routine to print error on stout or syslog
 */
void PrintErr(char * error) 
{
    if (demonize) {                       /* daemon mode */
	syslog(LOG_ERR, "%s: %m", error); /* log string and error message */
    } else {
	perror(error);
    }
    return;
}

void print_headers(FILE *file, struct code_page code)
{
    time_t tempo;

    fprintf(file, "HTTP/1.0 %s %s \n", code.code, code.name);
    time(&tempo);
    fprintf(file, "Date: %s", ctime(&tempo));
    fprintf(file, "Server: WWWd test server\n");
    fprintf(file, "Connection: close\n");
    fprintf(file, "Content-Type: text/html; charset=iso-8859-1\n");
    fprintf(file, "\n");
    return;
}

void print_error(FILE *file, struct code_page page, char * string)
{
    fprintf(file, "<HTML><HEAD><TITLE>%s %s</TITLE></HEAD>\n",
	    page.code, page.name);
    fprintf(file, "<BODY><H1>%s</H1>\n", page.name);
    fprintf(file, page.body, string);
    fprintf(file, "<HR><ADDRESS>WWWd by S. Piccardi</ADDRESS></BODY></HTML>");
    return;
}

