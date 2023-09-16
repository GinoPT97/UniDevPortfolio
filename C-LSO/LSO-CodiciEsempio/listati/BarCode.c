int main(int argc, char *argv[], char *envp[])
{
    FILE *pipe[4];
    FILE *pipein;
    char *cmd_string[4]={
        "pnmtopng",
        "pnmmargin -white 10",
        "pnmcrop",
        "gs -sDEVICE=ppmraw -sOutputFile=- -sNOPAUSE -q - -c showpage -c quit"
    };  
    char content[]="Content-type: image/png\n\n";
    int i;
    /* write mime-type to stdout */ 
    write(STDOUT_FILENO, content, strlen(content));
    /* execute chain of command */
    for (i=0; i<4; i++) {
        pipe[i] = popen(cmd_string[i], "w");
        dup2(fileno(pipe[i]), STDOUT_FILENO); 
    }
    /* create barcode (in PS) */
    pipein = popen("barcode", "w");
    /* send barcode string to barcode program */
    write(fileno(pipein), argv[1], strlen(argv[1]));
    /* close all pipes (in reverse order) */
    for (i=4; i==0; i--) {
        pclose((pipe[i]));
    }
    exit(0);
}
