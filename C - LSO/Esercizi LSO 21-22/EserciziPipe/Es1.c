#include <unistd.h>   //pid_t, fork, pipe, read, write, close
#include <stdio.h>    //perror
#include <stdlib.h>   //printf, error macros, sizeof
#include <time.h>     //srand, rand
#include <sys/wait.h> //wait

#define SIZE 10 

void getNRandIntegers(int *array, unsigned int n); //fills array with n random integers 

int main(int argc, char *argv[])
{
    int array[SIZE]; //array of random integers
    pid_t pid;     //child pid
    int fd[2];     //pipe read and write fd
    int buf;       //int read from pipe
    int status;    //child exit status
    int s_int = sizeof(int);

    getNRandIntegers(array, SIZE); //Populating array with 10 random integers

    if (pipe(fd) < 0) //Opening pipe
        perror("pipe err"), exit(EXIT_FAILURE);

    if ((pid = fork()) < 0) //Parent generating child
        perror("fork err"), exit(EXIT_FAILURE);
    else if (pid > 0) //Parent code
    {
        close(fd[0]);                //Closing read side
        for (int i = 0; i < 10; i++) //Stops as soon as 10 numbers are written
        {
            if (write(fd[1], &(array[i]), s_int) != s_int) //Writing a number on pipe..
                perror("write err"), exit(EXIT_FAILURE);
            sleep(1); //..and waiting one second
        }
        wait(&status);          //Collecting child exit status
        if (!WIFEXITED(status)) //if child didn't terminate correctly
            perror("child err"), exit(EXIT_FAILURE);
    }
    else
    {
        close(fd[1]);                //Closing write side
        for (int i = 0; i < 10; i++) //Stopping as soon as 10 numbers are read
        {
            if (read(fd[0], &buf, s_int) != s_int) //Reading a number from pipe..  (waiting if pipe is empty)
                perror("read err"), exit(EXIT_FAILURE);
            printf("%d\n", buf); //.. and printing it on stdout
        }
    }

    return 0;
}

void getNRandIntegers(int *array, unsigned int n) 
{
    srand(time(NULL));
    for (unsigned int j = 0; j < n; j++)
        array[j] = rand();
    return;
}