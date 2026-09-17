package kz.iitu.springlab.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class LifecycleDemo {

    private final DateTimeFormatter formatter;
    private final List<String> events = new ArrayList<>();

    public LifecycleDemo(DateTimeFormatter formatter) {
        this.formatter = formatter;
        events.add("1. Constructor called");
    }

    @PostConstruct
    public void init() {
        events.add("2. @PostConstruct executed");
        System.out.println(
                "LIFECYCLE >> " +
                        LocalDateTime.now().format(formatter) +
                        " 2. @PostConstruct executed"
        );
    }

    @PreDestroy
    public void destroy() {
        events.add("3. @PreDestroy executed");
        System.out.println(
                "LIFECYCLE >> " +
                        LocalDateTime.now().format(formatter) +
                        " 3. @PreDestroy executed"
        );
    }

    public List<String> events() {
        return new ArrayList<>(events);
    }
}