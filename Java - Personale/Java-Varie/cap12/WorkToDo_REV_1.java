package com.pellegrinoprincipe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// metadati
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface WorkToDo_REV_1
{
    String msg();
    String start_date();
    String developer();
    int uid() default 0;
}
