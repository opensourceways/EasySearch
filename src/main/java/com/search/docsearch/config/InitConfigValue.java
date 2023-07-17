package com.search.docsearch.config;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitConfigValue {
    
    public static void deletEvnformApplication() {
        try{
            File buildConfig = new File("/EaseSearch/target/classes/application.yml");
            if(buildConfig.delete()){
                log.info(buildConfig.getName() + " file has been deleted!");
            }else{
                log.info("file delete failed!");
            }
            
            File originConfig = new File("/EaseSearch/src/main/resources/application.yml");
            if(originConfig.delete()){
                log.info(originConfig.getName() + " file has been deleted!");
            }else{
                log.info("file delete failed!");
            }
        }catch(Exception e){
            log.error(e.getMessage());
        }
        
        
    }

    
}
