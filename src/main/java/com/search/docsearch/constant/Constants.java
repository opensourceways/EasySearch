package com.search.docsearch.constant;

import com.search.docsearch.utils.Trie;

public class Constants {
    public static final long MILLISECONDS_OF_A_DAY = 86400000;
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SHANGHAI_TIME_ZONE = "Asia/Shanghai";
    public static final String CONFIG_PATH = "/vault/secrets/application.yml";
    public static Trie softwareTrie = new Trie();
    /**
     * Maxmimum es return length.
     */
    public static final int MAX_ES_RETURN_LENGHTH = 10000;

    /**
     * Maxmimum description length.
     */
    public static final int MAX_ES_DESC_LENGHTH = 200;


    public static final String HTTPS_PREFIX = "https://";

    /**
     * Maxsocre that used to normlize the result
     */
    public static final int MAX_SCORE = 10000000;

    /**
     * Min socre that used to normlize the result
     */
    public static final int MIN_SCORE = -1;

    /**
     * Google search start
     */
    public static final int GOOGLE_START = 1;

    /**
     * Google search num
     */
    public static final int GOOGLE_NUM = 10;

    /**
     * ES search start
     */
    public static final int ES_START = 1;

    /**
     * ES search num
     */
    public static final int ES_NUM = 100;

    /**
     * set score to CONSTANT_SCORE when normalize failed
     */
    public static final int CONSTANT_SCORE = 1;
}
