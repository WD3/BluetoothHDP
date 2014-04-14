/*
 * Copyright 2008 Santiago Carot Nemesio (sancane@gmail.com).
 */
package org.bn.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ASN1Float {
    String name();
}
