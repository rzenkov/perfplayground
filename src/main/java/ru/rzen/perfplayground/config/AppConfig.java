package ru.rzen.perfplayground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;

@Configuration
public class AppConfig {
    @Bean(name = "virtual")
    public VirtualThreadTaskExecutor virtual() {
        return new VirtualThreadTaskExecutor("VIRT-EX-1");
    }
}
