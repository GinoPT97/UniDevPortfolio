/*Input: 3 file(f1 f2 f3), di cui il primo deve già esistere*/
/*Cosa fa il programma: 
Il parent fa una prima fork. Il figlio effettua una exec di revout su f1 e f2 (salva in f2 f1 rovesciato).
Quando questo termina, il parent effettua una ulteriore fork. Il nuovo figlio fa una exec di revout su f2 e f3 (salva in f3 f2 rovesciato cioè f1)*/

#include <unistd.h>    /*fork, execl, dup2*/
#include <stdio.h>     /*printf*/
#include <err.h>       /*err*/
#include <stdlib.h>    /*error macros*/
#include <sys/wait.h>  /*wait*/
#include <sys/types.h> /*pid_t*/
#include <fcntl.h>     /*open*/
#include <libgen.h>    /*basename*/

int main(int argc, char *argv[])
{

    pid_t pid;                         //child process identifier
    int status;                        //exit status of child processes
    int fd;                            //file descriptor
    char program_path[] = "./rev.out"; //program name is extracted using 'basename' function

    if (argv[1] == NULL)
        err(EXIT_FAILURE, "File1 missing");
    if (argv[2] == NULL)
        err(EXIT_FAILURE, "File2 missing");
    if (argv[3] == NULL)
        err(EXIT_FAILURE, "File3 missing");

    if ((fd = open(argv[2], O_WRONLY | O_CREAT | O_TRUNC, 0644)) < 0) //Opening argv[2] in w only; creating new file if it doesn't exist, truncating to 0 bytes otherwise; mode: -rw-r--r--
        err(EXIT_FAILURE, "Open error");

    if ((pid = fork()) < 0) //Parent generating first child
    {
        err(EXIT_FAILURE, "Fork error");
    }
    else if (pid == 0)
    {
        /*First child code*/
        if (dup2(fd, STDOUT_FILENO) < 0) //STDOUT_FILENO refers to fd (Only visible in child process)
            err(EXIT_FAILURE, "Dup error");

        execl(program_path, basename(program_path), argv[1], (char *)0); //File1 is reversed and saved into File2
        exit(EXIT_FAILURE); //Only if execl fails
    }
    /*Parent code*/
    waitpid(pid, &status, 0); //Parent waiting for first child

    if (close(fd) < 0) //Closing File2
        err(EXIT_FAILURE, "Close error");

    if (WIFEXITED(status) && WEXITSTATUS(status) == 0) //If child terminates correctly..
        printf("%s successfully written!\n", argv[2]);
    else
        err(EXIT_FAILURE, "Exec error");

    if ((fd = open(argv[3], O_WRONLY | O_CREAT | O_TRUNC, 0644)) < 0) //Opening argv[2] in w only; creating new file if it doesn't exist, truncating to 0 bytes otherwise; mode: -rw-r--r--
        err(EXIT_FAILURE, "Open error");

    if ((pid = fork()) < 0) //Parent generating second child
    {
        err(EXIT_FAILURE, "Fork error");
    }
    else if (pid == 0)
    {
        /*Second child code*/
        if (dup2(fd, STDOUT_FILENO) < 0) //STDOUT_FILENO refers to fd (Only visible in child process)
            err(EXIT_FAILURE, "Dup error");

        char *args[] = {basename(program_path), argv[2], NULL};
        execv(program_path, args); //File2 is reversed and saved into File3
        exit(EXIT_FAILURE); //Only if execv fails
    }
    /*Parent code*/
    waitpid(pid, &status, 0); //Parent waiting for second child

    if (close(fd) < 0) //Closing File3
        err(EXIT_FAILURE, "Close error");

    if (WIFEXITED(status) && WEXITSTATUS(status) == 0) //If child terminates correctly..
        printf("%s successfully written!\n", argv[3]);
    else
        err(EXIT_FAILURE, "Exec error");

    return 0;
}