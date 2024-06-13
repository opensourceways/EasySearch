package com.search.docsearch.constant;

import com.search.docsearch.utils.Trie;

public class Constants {
    public static final long MILLISECONDS_OF_A_DAY = 86400000;
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SHANGHAI_TIME_ZONE = "Asia/Shanghai";
    public static final String CONFIG_PATH = "/vault/secrets/application.yml";
    public static Trie softwareTrie = new Trie();
}



