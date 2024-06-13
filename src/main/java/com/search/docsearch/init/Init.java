package com.search.docsearch.init;

import com.search.docsearch.config.MySystem;
import com.search.docsearch.service.SearchService;
import com.search.docsearch.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

@Component
@Slf4j
public class Init implements ApplicationRunner {

    @Autowired
    private SearchService searchService;

    @Autowired
    @Qualifier("setConfig")
    private MySystem s;

    /**
     * 该方法在项目启动时就会运行
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        String applictionPath = System.getenv("APPLICATION_PATH");
        if (StringUtils.hasText(applictionPath)) {
            if (FileUtils.deleteFile(applictionPath)) {
                log.info("delete application success");
            } else {
                log.info("delete application fail");
            }
        } else {
            log.info("application path is null");
        }

        try {
            searchService.saveWord();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
