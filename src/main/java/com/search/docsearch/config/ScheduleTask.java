package com.search.docsearch.config;

import com.search.docsearch.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    private SearchService searchService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void importWord() {
        try {
            searchService.saveWord();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
