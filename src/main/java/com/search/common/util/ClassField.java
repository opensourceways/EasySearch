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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ClassField {


    // Private constructor to prevent instantiation of the utility class
    private ClassField() {
        // private constructor to hide the implicit public one
        throw new AssertionError("ClassField class cannot be instantiated.");
    }

    /**
     * Retrieve the field names of an object.
     *
     * @param obj The object for which to retrieve field names
     * @return A list of field names as strings
     */
    public static List<String> getFieldNames(final Object obj) {
        List<String> columnNames = new ArrayList<>();

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (List.class.isAssignableFrom(field.getType())) {
                continue;
            }
            String fieldName = field.getName();
            String columnName = fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
            columnNames.add(columnName);
        }

        return columnNames;
    }




}
