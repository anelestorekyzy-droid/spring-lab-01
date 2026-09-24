package kz.iitu.springlab;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lab3")
public class Lab3Controller {

    private final AppProperties appProperties;
    private final Environment environment;
    private final String banner;

    public Lab3Controller(
            AppProperties appProperties,
            Environment environment,
            String banner
    ) {
        this.appProperties = appProperties;
        this.environment = environment;
        this.banner = banner;
    }

    @GetMapping("/config")
    public Map<String, Object> config() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("owner", appProperties.owner());
        result.put("group", appProperties.group());
        result.put("mailFrom", appProperties.mail().from());
        result.put("retryCount", appProperties.mail().retryCount());
        result.put("timeout", appProperties.mail().timeout());
        result.put("enabled", appProperties.mail().enabled());
        result.put("serverPort", environment.getProperty("server.port"));
        result.put("activeProfiles",
                Arrays.asList(environment.getActiveProfiles()));
        result.put("banner", banner);

        return result;
    }
}