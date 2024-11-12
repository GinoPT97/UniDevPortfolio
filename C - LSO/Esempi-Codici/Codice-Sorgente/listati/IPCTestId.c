int main(int argc, char *argv[])
{
    ...
    switch (type) {
    case 'q':   /* Message Queue */
        debug("Message Queue Try\n");
        for (i=0; i<n; i++) {
            id = msgget(IPC_PRIVATE, IPC_CREAT|0666);
            printf("Identifier Value %d \n", id);
            msgctl(id, IPC_RMID, NULL);
        }
        break;
    case 's':   /* Semaphore */
        debug("Semaphore\n");
        for (i=0; i<n; i++) {
            id = semget(IPC_PRIVATE, 1, IPC_CREAT|0666);
            printf("Identifier Value %d \n", id);
            semctl(id, 0, IPC_RMID);
        }
        break;
    case 'm':   /* Shared Memory */
        debug("Shared Memory\n");
        for (i=0; i<n; i++) {
            id = shmget(IPC_PRIVATE, 1000, IPC_CREAT|0666);
            printf("Identifier Value %d \n", id);
            shmctl(id, IPC_RMID, NULL);
        }
        break;
    default:    /* should not reached */
        return -1;
    }
    return 0;
}
