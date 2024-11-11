#include <unistd.h>   //pid_t, fork, pipe, read, write, close
#include <stdio.h>    //fgets, perror
#include <stdlib.h>   //printf, error macros, atoi, sizeof
#include <sys/wait.h> //wait
#include <ctype.h>    //isdigit

#define MAX 13

int isNumber(const char *string); //Checks whether string content is an integer or not

int main(int argc, char *argv[])
{
    pid_t pid;       //child pid
    int fd[2];       //pipe read and write fd
    int buf;         //int read from pipe
    char input[MAX]; //input string
    int r;           //input string converted to int (overflow if r>2,147,483,647 or r<-2,147,483,648)
    int s_int = sizeof(int);
    int status; //child exit status

    if (pipe(fd) < 0) //Opening pipe
        perror("pipe err"), exit(EXIT_FAILURE);

    if ((pid = fork()) < 0) //Parent generating child
        perror("fork err"), exit(EXIT_FAILURE);

    else if (pid > 0) //Parent code (producer)
    {
        close(fd[0]); //Closing read side
        do
        {
            do
            {
                fgets(input, sizeof(input), stdin); //Reading from command line..

            } while (isNumber(input) == 0); //..until it is a number

            r = atoi(input); //Converting string to int..
            r = r * r;       //and squaring it (could cause overflow)

            if (write(fd[1], &r, s_int) < 0) //Writing int on pipe..
                perror("write err"), exit(EXIT_FAILURE);
        } while (r != 0); //..until it isn't 0

        wait(&status);          //collecting child exit status
        if (!WIFEXITED(status)) //if child didn't terminate correctly
            perror("child err"), exit(EXIT_FAILURE);
    }
    else //Child code (consumer)
    {
        close(fd[1]); //Closing write side
        do
        {
            if (read(fd[0], &buf, s_int) != s_int) //Reading int from pipe
                perror("read err"), exit(EXIT_FAILURE);
            printf("%d\n", buf); //Printing int to stdout..
        } while (buf != 0);      //..Until it isn't 0
    }

    return 0;
}

int isNumber(const char *string)
{
    if (isdigit(string[0]) == 0 && string[0] != '-') //If the string doesn't start with a digit or with minus is not accepted
        return 0;
    if (string[0] == '-' && string[1] == '\n') //If the string is only "-" is not accepted
        return 0;
    for (unsigned int i = 1; string[i] != '\n'; i++)
    {
        if (isdigit(string[i]) == 0)
            return 0;
    }
    return 1;
}
