package gov.nasa.jpf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface SequenceObject {
  String id();
  String object() default "<field>";
}
