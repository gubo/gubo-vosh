
package com.gubo.vosh;

import javax.inject.*;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Jeff on 12/7/2016.
 */
@Scope
@Retention( RUNTIME )
public @interface AppScope {}
