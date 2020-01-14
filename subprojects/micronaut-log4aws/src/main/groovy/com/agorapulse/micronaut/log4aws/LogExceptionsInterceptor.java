package com.agorapulse.micronaut.log4aws;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class LogExceptionsInterceptor implements MethodInterceptor<Object, Object> {

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        Optional<AnnotationValue<LogError>> annotation = Optional.ofNullable(context.getAnnotation(LogError.class));

        boolean mute = annotation.flatMap(a -> a.booleanValue("mute")).orElse(false);
        Class<?> filter = annotation.flatMap(a -> a.classValue("only")).orElse(Throwable.class);

        try {
            return context.proceed();
        } catch (Throwable th) {
            if (filter.isAssignableFrom(th.getClass())) {
                Logger logger = LoggerFactory.getLogger(context.getTargetMethod().getDeclaringClass());
                logger.error("Exception executing method " + context.getTargetMethod(), th);
                if (mute) {
                    return null;
                }
            }
            throw th;
        }
    }
}
