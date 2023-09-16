sig_atomic_t flag;
int main()
{
    flag = 0;
    ...
    if (flag) {         /* test if signal occurred */
        flag = 0;       /* reset flag */ 
        do_response();  /* do things */
    } else {
        do_other();     /* do other things */
    }
    ...
}
void alarm_hand(int sig) 
{
    /* set the flag */
    flag = 1;
    return;
}
