int endian(void)
{
/*
 * Variables definition
 */
    short magic, test;
    char * ptr;
   
    magic = 0xABCD;                     /* endianess magic number */
    ptr = (char *) &magic;              
    test = (ptr[1]<<8) + (ptr[0]&0xFF); /* build value byte by byte */
    return (magic == test);             /* if the same is little endian */ 
}
