package com.search.docsearch.utils;

import com.search.docsearch.config.MySystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
@Component
public class ParseData {

    private static final String OPENEULER = "openeuler";
    private static final String OPENGAUSS = "opengauss";



    @Autowired
    @Qualifier("setConfig")
    private MySystem s;

    public Map<String, Object> parse(String lang, String deleteType, File mdFile) throws Exception {
        switch (s.getSystem()) {
            case OPENEULER:
                return EulerParse.parse(lang, deleteType, mdFile);
            case OPENGAUSS:
                return EulerParse.parse(lang, deleteType, mdFile);
            default:
                return null;
        }
    }

}
