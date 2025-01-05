package com.coyoapp.tinytask.logging;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  /**
   * Log the execution time of a method annotated with {@link Timed}.
   *
   * <p>This aspect logs the execution time of the method, in milliseconds.
   *
   * @param joinPoint the annotated method
   * @return the result of the method execution
   * @throws Throwable if the method execution fails
   */
  @Around("@annotation(com.coyoapp.tinytask.logging.Timed)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    // Extract class and method names
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();

    // Start timer
    long startTime = System.currentTimeMillis();

    // Proceed with method execution
    Object result = joinPoint.proceed();

    // Measure elapsed time
    long elapsedTime = System.currentTimeMillis() - startTime;

    // Log the execution time
    log.info(
        "Timed: Executing {}.{}() took {} milliseconds",
        className,
        methodName,
        Duration.ofMillis(elapsedTime).toMillis());

    return result;
  }
}
