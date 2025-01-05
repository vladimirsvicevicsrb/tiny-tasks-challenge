package com.coyoapp.tinytask.logging;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Pointcut("@annotation(com.coyoapp.tinytask.logging.Timed)")
  public void timedMethod() {}

  @Pointcut("within(@com.coyoapp.tinytask.logging.Timed *)")
  public void timedClass() {}

  // Pointcut for methods annotated with @Mapping annotations
  @Pointcut(
      "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
  public void mappingAnnotations() {}

  @Pointcut("(timedMethod() || timedClass()) && mappingAnnotations()")
  public void timedEndpoints() {}

  /**
   * Aspect that logs the execution time of all methods annotated with @Timed or enclosed in a class
   * annotated with @Timed.
   *
   * <p>This aspect will log the execution time of all methods annotated with @Timed, as well as all
   * methods in classes annotated with @Timed. The execution time is logged as an INFO message.
   *
   * @param joinPoint the join point representing the method invocation
   * @return the result of the method invocation
   * @throws Throwable if any errors occur during method invocation
   */
  @Around("timedEndpoints()")
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
