/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.common.util;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class General {
    static final String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
    private static final Pattern CHINSEE_PATTERN = Pattern.compile(REGEX_CHINESE);
    private static final HashMap<String, String> REPLACE_MAP = new HashMap<String, String>() {{
        put("_", " ");
        put("&", " ");
        put("+", " ");
    }};

    public static String removeChinese(String str) {
        Matcher mat = CHINSEE_PATTERN.matcher(str);
        return mat.replaceAll(" ");
    }

    public static boolean haveChinese(String str) {
        Matcher m = CHINSEE_PATTERN.matcher(str);
        return m.find();
    }

    public static String replacementCharacter(String str) {
        for (Map.Entry<String, String> entry : REPLACE_MAP.entrySet()) {
            str = str.replace(entry.getKey(), entry.getValue());
        }
        // "."因为有浮点数影响，不能单纯的看作英文连接词，在有数字的情况下特殊处理。
        char key = '.';
        int a = str.indexOf(key);
        while (a != -1) {
            int ind = a;

            if (ind < 1 || ind > str.length() - 2) {
                a = str.indexOf(key, a + 1);
                continue;
            }
            if (!Character.isDigit(str.charAt(ind - 1)) || !Character.isDigit(str.charAt(ind + 1))) {
                str = str.substring(0, ind) + " " + str.substring(ind + 1);
            }

            a = str.indexOf(key, a + 1);

        }

        return insertSpaceBetweenNumbersAndLetters(str);
    }


    public static String insertSpaceBetweenNumbersAndLetters(String str) {
        String regex = "(\\d+\\.)+\\d*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuilder output = new StringBuilder();
        while (matcher.find()) {
            String match = matcher.group();
            matcher.appendReplacement(output, " " + match + " ");
        }

        matcher.appendTail(output);
        return output.toString();
    }

}
