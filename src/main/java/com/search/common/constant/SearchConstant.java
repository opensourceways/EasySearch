package com.search.common.constant;

public class SearchConstant {


    /**
     * Maximum page number allowed.
     */
    public static final int MAX_PAGE_NUM = 100000;

    /**
     * Minimum page number allowed.
     */
    public static final int MIN_PAGE_NUM = 1;

    /**
     * Maximum page size allowed.
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * Minimum page size allowed.
     */
    public static final int MIN_PAGE_SIZE = 5;

    /**
     * Maximum field length allowed.
     */
    public static final int MAX_FIELD_LENGTH = 1000;

    /**
     * VALID_STR_REG used to match input string.
     */
    public static final String VALID_STR_REG = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$";

    /**
     * VALID_MESSAGE, error message.
     */
    public static final String VALID_MESSAGE = "Null or string. String includes only letters, digits, and special "
            + "characters(_-+()$.,)";

    /**
     * INDEX_CONNECT, ES index  connect.
     */
    public static final String INDEX_CONNECT = "_articles_";

    public static final String SOFTWARE_VALID_DATATYPE = "((^all$|^rpmpkg$|^apppkg$|^epkgpkg$|^appversion$|^$))";



    public static final String SOFTWARE_VALID_NAMEORDER= "((^desc$|^asc$|^$))";
}



