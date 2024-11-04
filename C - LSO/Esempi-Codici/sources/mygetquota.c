/* mygetquota.c
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
 * File mygetquota.c: An example for quotactl function
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
 * Program mygetquota
 *
 * Use quotactl to get an user quota
 */

/* Help printing routine */
void usage(void);

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
    if ((argc - optind) != 2) {
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    /* main body */    
    uid = atoi(argv[optind]);
    ret = quotactl(QCMD(Q_GETQUOTA,who), argv[optind+1], uid, (caddr_t) &dq);
    if (!ret) {
	printf("Data:%lld\n", dq.dqb_curspace);
	printf("Soft:%lld\tHard: %lld\tGrace: %lld\n", dq.dqb_bsoftlimit, dq.dqb_bhardlimit, dq.dqb_btime);	
	printf("Files: %7.1qu\n", dq.dqb_curinodes);
	printf("Soft: %lld\tHard: %lld\tGrace: %lld\n", dq.dqb_isoftlimit, dq.dqb_ihardlimit, dq.dqb_itime);
    } else
	perror("errore");
    exit(0);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mygetquota: get an user quota \n");
    printf("Usage:\n");
    printf("mygetquota uid filesystem \n");
    exit(1);
}
