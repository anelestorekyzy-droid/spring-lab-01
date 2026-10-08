package kz.iitu.springlab;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

//@RestController
@RequestMapping("/api/lab3")
public class Lab3Controller {

    private final AppProperties props;
    private final Environment environment;

    public Lab3Controller(AppProperties props, Environment environment) {
        this.props = props;
        this.environment = environment;
    }

    @GetMapping("/config")
    public Map<String, Object> config() {
        return Map.of(
                "owner", props.owner(),
                "group", props.group(),
                "mailFrom", props.mail().from(),
                "retryCount", props.mail().retryCount(),
                "timeout", props.mail().timeout().toString(),
                "enabled", props.mail().enabled(),
                "defaultLocale", props.locale().defaultLocale(),
                "supportedLocales", props.locale().supported(),
                "serverPort", environment.getProperty("server.port", "8080"),
                "activeProfiles", Arrays.asList(environment.getActiveProfiles())
        );
    }
}