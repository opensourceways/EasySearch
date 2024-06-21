package com.search.docsearch.common.constant;

import com.search.docsearch.common.utils.Trie;

public class Constants {
    /**
     * Milliseconds of a day.
     */
    public static final long MILLISECONDS_OF_A_DAY = 86400000;
    /**
     * Date format.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Time zone.
     */
    public static final String SHANGHAI_TIME_ZONE = "Asia/Shanghai";
    /**
     * Config path.
     */
    public static final String CONFIG_PATH = "/vault/secrets/application.yml";
    /**
     * Tupple.
     */
    public static Trie softwareTrie = new Trie();
    /**
     * Maxmimum es return length.
     */
    public static final int MAX_ES_RETURN_LENGHTH = 10000;

    /**
     * Maxmimum description length.
     */
    public static final int MAX_ES_DESC_LENGHTH = 200;
}
