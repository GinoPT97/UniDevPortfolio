package com.pellegrinoprincipe;

public @interface WorkToDo
{
    String msg();
    String start_date();
    String developer();
    int uid() default 0;
}
