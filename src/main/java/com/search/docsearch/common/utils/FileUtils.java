package com.search.docsearch.common.utils;

import java.io.File;

public class FileUtils {

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

}
