/* mysetquota.c
 * 
 * Copyright (C) 2010 Simone Piccardi
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
/*****************************************************************************
 *
 * File mysetquota.c: An example for quotactl function
 *
 * Author: S. Piccardi Jun. 2010
 *
 *****************************************************************************/
#include <string.h>      /* C strings library */
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <unistd.h>      /* unix standard library */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <sys/quota.h>

/*
 * Program mysetquota
 *
 * Use quotactl to set an user quota
 */

/* Help printing routine */
void usage(void);
void printquota(struct dqblk dq);

int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i,j,ret;
    int who=USRQUOTA;
    uid_t uid=0;
    struct dqblk dq;
    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "hgu")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                             /* help option */
	    usage();
	    break;
	case 'g':                                             /* group option */
	    who=GRPQUOTA;
	    break;
	case 'u':                                             /* user option */
	    who=USRQUOTA;
	    break;
	case '?':                                    /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:                                       /* should not reached */
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
    /* remaining argument check */
    if ((argc - optind) != 6) {
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* main body */
    ret = quotactl(QCMD(Q_GETQUOTA,who), argv[optind+1], uid, (caddr_t) &dq);
    if (!ret) {
	printf("Previous values\n");
	printquota(dq);
    } else {
	perror("errore");
	exit(1);
    }

    uid = atoi(argv[optind]);
    dq.dqb_bsoftlimit =  atoi(argv[optind+2]);
    dq.dqb_bhardlimit =  atoi(argv[optind+3]);
    dq.dqb_isoftlimit =  atoi(argv[optind+4]);
    dq.dqb_ihardlimit =  atoi(argv[optind+5]);
    dq.dqb_valid = QIF_LIMITS;
    ret = quotactl(QCMD(Q_SETQUOTA,who), argv[optind+1], uid, (caddr_t) &dq);
    if (!ret) {
	ret = quotactl(QCMD(Q_GETQUOTA,who), argv[optind+1], uid, (caddr_t) &dq);
	if (!ret) {
	    printf("After values\n");
	    printquota(dq);
	} else {
	    perror("errore in rilettura");
	    exit(1);
	}
    } else {
	perror("errore in scrittura");
	exit(1);
    }


}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mysetquota: set an user quota \n");
    printf("Usage:\n");
    printf("mysetquota [-u|-g] uid filesystem \n");
    exit(1);
}

void printquota(struct dqblk dq) 
{
    printf("Data:%lld\n", dq.dqb_curspace);
    printf("Soft:%lld\tHard: %lld\tGrace: %lld\n", dq.dqb_bsoftlimit, dq.dqb_bhardlimit, dq.dqb_btime);	
    printf("Files: %lld\n", dq.dqb_curinodes);
    printf("Soft: %lld\tHard: %lld\tGrace: %lld\n", dq.dqb_isoftlimit, dq.dqb_ihardlimit, dq.dqb_itime);
    
}
