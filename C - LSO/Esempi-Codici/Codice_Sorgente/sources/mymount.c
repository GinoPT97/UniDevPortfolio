/* mymount.c
 * 
 * Copyright (C) 2012 Simone Piccardi
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
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
 * File mymount.c: An example for mount function
 *
 * Author: S. Piccardi Jan. 2012
 *
 *****************************************************************************/
#include <sys/types.h>   /* primitive system data types */
#include <sys/stat.h>    /* file characteristics constants and functions */
#include <stdlib.h>      /* C standard library */
#include <stdio.h>       /* standard I/O library */
#include <unistd.h>      /* unix standard library */
#include <string.h>
#include <sys/mount.h>

/* pass -D DEBUG to gcc to enable debug printing */
#ifdef DEBUG
#define debug printf
#else
#define debug(fmt, arg...)
#endif /* DEBUG */

/* Adding undefined flags in glibc headers */ 
#ifndef MS_DIRSYNC      
#define MS_DIRSYNC 128
#endif
#ifndef MS_BIND         
#define MS_BIND 4096
#endif
#ifndef MS_MOVE         
#define MS_MOVE 8192
#endif
#ifndef MS_REC          
#define MS_REC 16384
#endif
#ifndef MS_SILENT         
#define MS_SILENT 32768
#endif
#ifndef MS_UNBINDABLE   
#define MS_UNBINDABLE (1<<17) 
#endif
#ifndef MS_PRIVATE     
#define MS_PRIVATE (1<<18)
#endif
#ifndef MS_SLAVE         
#define MS_SLAVE (1<<19) 
#endif
#ifndef MS_SHARED       
#define MS_SHARED (1<<20)
#endif
#ifndef MS_RELATIME       
#define MS_RELATIME (1<<21)
#endif
#ifndef MS_STRICTATIME       
#define MS_STRICTATIME (1<<24)
#endif


/*
 * Program mymount
 */
/* Help printing routine */
void usage(void);

/* struct for name<->flag correspondence */
struct ms_flag {
    const char *name;
    int flag;
};

/* compare funtion to find a flag name */
int ms_flag_cmp(const void *c1, const void *c2) 
{
    struct ms_flag *v1 = (struct ms_flag *) c1;
    struct ms_flag *v2 = (struct ms_flag *) c2;
    return strcmp(v1->name, v2->name);
}

/* main body */
int main(int argc, char *argv[]) 
{
/* 
 * Variables definition
 */
    int i, flags=0;
    char * mountflags=NULL, *data=NULL, *filesystemtype=NULL;
    char *token;
    /* mount flag table, must be alphabetically ordered */
    struct ms_flag ms_values[]=
	{
	    {"bind", MS_BIND},
	    {"dirsync",MS_DIRSYNC},
	    {"mandlock",MS_MANDLOCK},
	    {"move",MS_MOVE},
	    {"noatime",MS_NOATIME},
	    {"nodev",MS_NODEV},
	    {"nodiratime",MS_NODIRATIME},
	    {"noexec",MS_NOEXEC},
	    {"nosuid",MS_NOSUID},
	    {"private",MS_PRIVATE},
	    {"rdonly",MS_RDONLY},
	    {"rec",MS_REC},
	    {"relatime",MS_RELATIME},
	    {"remount",MS_REMOUNT},
	    {"share",MS_SHARED},
	    {"silent",MS_SILENT},
	    {"slave",MS_SLAVE},
	    {"strictatime",MS_STRICTATIME},
	    {"synchronous",MS_SYNCHRONOUS},
	    {"unbindable",MS_UNBINDABLE}
	};
    struct ms_flag key, *result;
    int ms_count = sizeof(ms_values)/sizeof(struct ms_flag);

    /*
     * Input section: decode command line parameters 
     * Use getopt function
     */
    opterr = 0;	 /* don't want writing to stderr */
    while ( (i = getopt(argc, argv, "o:f:t:h")) != -1) {
	switch (i) {
	/* 
	 * Handling options 
	 */ 
	case 'h':                                  /* help option */
	    printf("Wrong -h option use\n");
	    usage();
	    return -1;
	    break;
        case 'o':                                  /* options data */
            data = optarg;
	    debug("passed option data: %s\n", data);
            break;
        case 't':                                  /* filesystem type */
            filesystemtype = optarg;
	    debug("passed filesystem type: %s\n", filesystemtype);
            break;
        case 'f':                                  /* mount flags */ 
            mountflags = optarg;
	    debug("passed mount flags: %s\n", mountflags);
	    break;
	case '?':                                  /* unrecognized options */
	    printf("Unrecognized options -%c\n",optopt);
	    usage();
	default:                                   /* should not reached */
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
    /* There must be 2 arguments, source and target */
    if ((argc - optind) != 2) {   
	printf("Wrong number of arguments %d\n", argc - optind);
        usage();
    }
    debug("source %s, target %s\n",argv[optind], argv[optind+1]);

    /* parse -f option parameter, and build mount flags argument */ 
    if ( token = strtok(mountflags, ",") ) {
        do {
	    key.name = token;
	    result = bsearch(&key, ms_values, ms_count, sizeof(struct ms_flag),
			     ms_flag_cmp);
	    if (result) {
		debug("found option: %s\n",result->name);
		flags |= result->flag;
	    } else {
		printf("unknown flag: %s\n", key.name);
		exit(EXIT_FAILURE);
	    }
	} while (token=strtok(NULL,","));
    }
    debug("flags value %X\n", flags);
    /* do mount */
    if (mount(argv[optind], argv[optind + 1], filesystemtype, flags, data) == -1) {
	perror("Mount call failed");
	exit(EXIT_FAILURE);
    }
    exit(EXIT_SUCCESS);
}
/*
 * routine to print usage info and exit
 */
void usage(void) {
    printf("Program mymount:  \n");
    printf("Usage:\n");
    printf("mymount [-h] [-o opt1[,...]] [-t type] [-f fl1[,...]] source target\n");
    printf("  -h          print this help\n");
    printf("  -o options  comma separated options data string\n");
    printf("  -t fstype   filesystemtype string\n");
    printf("  -f flags    comma separated mount flags string\n");
    printf("\n");
    printf("you always need to specify two argument, source and target,\n");
    printf("matching the system call ones, if one of them is not needed\n");
    printf("just pass an empty string\n");
    printf("you also need to specify options and flag, give an empty \n");
    printf("string for option if you don't want to set any, i.e. \n");
    printf("   ./mymount -t ext4 -o "" -f remount,rdonly /dev/sdb1 /mnt");
    exit(1);
}
