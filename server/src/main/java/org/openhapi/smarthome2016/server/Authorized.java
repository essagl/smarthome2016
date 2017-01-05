package org.openhapi.smarthome2016.server;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface Authorized {
    boolean admin() default false;
}
