int main(int argc, char *argv[], char *envp[])
{
    ...
    /* create two pipes, pipein and pipeout, to handle communication */
    if ( (retval = pipe(pipein)) ) {
        WriteMess("input pipe creation error");
        exit(0);        
    }
    if ( (retval = pipe(pipeout)) ) {
        WriteMess("output pipe creation error");
        exit(0);        
    }    
    /* First fork: use child to run barcode program */
    if ( (pid = fork()) == -1) {          /* on error exit */
        WriteMess("child creation error");
        exit(0);        
    }
    /* if child */
    if (pid == 0) {
        close(pipein[1]);                /* close pipe write end  */
        dup2(pipein[0], STDIN_FILENO);   /* remap stdin to pipe read end */
        close(pipeout[0]);
        dup2(pipeout[1], STDOUT_FILENO); /* remap stdout in pipe output */
        execlp("barcode", "barcode", size, NULL);
    } 
    close(pipein[0]);                    /* close input side of input pipe */
    write(pipein[1], argv[1], strlen(argv[1]));  /* write parameter to pipe */
    close(pipein[1]);                    /* closing write end */
    waitpid(pid, NULL, 0);               /* wait child completion */
    /* Second fork: use child to run ghostscript */
    if ( (pid = fork()) == -1) {
        WriteMess("child creation error");
        exit(0);
    }
    /* second child, convert PS to JPEG  */
    if (pid == 0) {                     
        close(pipeout[1]);              /* close write end */
        dup2(pipeout[0], STDIN_FILENO); /* remap read end to stdin */
        /* send mime type */
        write(STDOUT_FILENO, content, strlen(content));
        execlp("gs", "gs", "-q", "-sDEVICE=jpeg", "-sOutputFile=-", "-", NULL);
    }
    /* still parent */
    close(pipeout[1]); 
    waitpid(pid, NULL, 0);
    exit(0);
}
