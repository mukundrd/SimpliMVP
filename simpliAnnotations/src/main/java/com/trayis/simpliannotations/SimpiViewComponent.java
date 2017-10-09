package com.trayis.simpliannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mudesai on 10/9/17.
 */

@Target(ElementType.TYPE) // on class level
@Retention(RetentionPolicy.SOURCE) // not needed at runtime
public @interface SimpiViewComponent {
}
