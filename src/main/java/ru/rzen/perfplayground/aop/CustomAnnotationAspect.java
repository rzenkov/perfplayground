package ru.rzen.perfplayground.aop;


import com.zaxxer.hikari.HikariPoolMXBean;
import java.lang.management.ManagementFactory;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "app", name = "enable-measure", havingValue = "true")
public class CustomAnnotationAspect {
    private static final MBeanServer MBEAN = ManagementFactory.getPlatformMBeanServer();

    /**
     * Логирование медленных методов, требует включенных опций
     * app.enable-measure = true
     * spring.datasource.hikari.register-mbeans: false
     *
     *
     * @param joinPoint точка соединения
     * @return результат
     * @throws Throwable исключение
     */
    @Around("@annotation(ru.rzen.perfplayground.aop.MeasureAndLogSlow)"
        + " && @annotation(measure)")
    public Object handleSlowMethods(
        ProceedingJoinPoint joinPoint, MeasureAndLogSlow measure
    ) throws Throwable {
        var poolName = new ObjectName("com.zaxxer.hikari:type=Pool (HikariPool-1)");
        var poolProxy = JMX.newMXBeanProxy(MBEAN, poolName, HikariPoolMXBean.class);

        int idleConnections = poolProxy.getIdleConnections();
        var limit = measure.limit();
        var start = System.currentTimeMillis();
        var result = joinPoint.proceed();
        var measured = System.currentTimeMillis() - start;

        if (measured > limit) {
            var value = measure.value();
            var target = ObjectUtils.isEmpty(value)
                ? joinPoint.getTarget().getClass().getName()
                : value;

            RequestContextHolder.currentRequestAttributes()
                .registerDestructionCallback(
                    "request-finalizer", () -> log.info("request finished"), 0
                );

            log.warn("Время работы {} превысило {}ms и составило {}ms", target, limit, measured);
            log.warn("Количество свободных jdbc соединений: {}", idleConnections);
        }

        return result;
    }
}
