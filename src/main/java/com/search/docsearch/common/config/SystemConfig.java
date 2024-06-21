package com.search.docsearch.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
@EnableConfigurationProperties()
@Slf4j
public class SystemConfig {
    /**
     * system.
     */
    @Value("${system}")
    private String system;

    /**
     * dep.
     */
    @Value("${dep}")
    private String dep;

    @Bean
    public MySystem setConfig() {
        log.info("system -> " + system);
        system = system.toLowerCase(Locale.ROOT);
        dep = dep.toLowerCase(Locale.ROOT);
        MySystem mySystem = new MySystem();

        mySystem.setSystem(system);

        if (dep.equals("test")) {
            mySystem.setIndex(system + "_test");
        } else {
            mySystem.setIndex(system + "_articles");
        }
        mySystem.setSearchWordIndex(system + "_search_word");
        mySystem.setPageViewsIndex(system + "_page_views");
        return mySystem;
    }
}
