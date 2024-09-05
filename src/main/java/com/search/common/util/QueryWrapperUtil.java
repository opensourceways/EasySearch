///* Copyright (c) 2024 openEuler Community
// EasySoftware is licensed under the Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//     http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.
//*/
//
//package com.search.common.util;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.easysoftware.common.entity.MessageCode;
//import com.easysoftware.common.exception.ParamErrorException;
//import com.easysoftware.common.exception.enumvalid.TimeOrderEnum;
//import com.power.common.util.StringUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
//public final class QueryWrapperUtil {
//
//
//    // Private constructor to prevent instantiation of the utility class
//    private QueryWrapperUtil() {
//        // private constructor to hide the implicit public one
//        throw new AssertionError("QueryWrapperUtil class cannot be instantiated.");
//    }
//
//    /**
//     * Logger instance for QueryWrapperUtil.
//     */
//    private static final Logger LOGGER = LoggerFactory.getLogger(QueryWrapperUtil.class);
//
//    /**
//     * get field value.
//     *
//     * @param field    The field
//     * @param u        The second generic type U
//     * @param <U>      the type of array elements
//     * @return A String
//     */
//    private static <U> String getFieldValue(Field field, U u) {
//        field.setAccessible(true);
//
//        Object value = null;
//        try {
//            value = field.get(u);
//        } catch (Exception e) {
//            LOGGER.error(MessageCode.EC00011.getMsgEn(), e.getMessage());
//        }
//        if (!(value instanceof String)) {
//            return "";
//        }
//
//        String vStr = (String) value;
//        return StringUtils.trimToEmpty(vStr);
//    }
//
//    /**
//     * setColumnOrder.
//     *
//     * @param wrapper    The wrapper
//     * @param column     The column
//     * @param vStr       The vStr
//     * @param orderName  The orderName
//     * @param <T>      the type of array elements
//     */
//    private static <T> void setColumnOrder(QueryWrapper<T> wrapper, String column, String vStr, String orderName) {
//        if (StringUtils.isBlank(vStr)) {
//            return;
//        } else if (TimeOrderEnum.DESC.getAlias().equals(vStr)) {
//            wrapper.orderByDesc(column);
//        } else if (TimeOrderEnum.ASC.getAlias().equals(vStr)) {
//            wrapper.orderByAsc(column);
//        } else {
//            throw new ParamErrorException("the value of " + orderName + "can be one of: asc, desc");
//        }
//    }
//
//    /**
//     * Create a QueryWrapper for given generic types T and U, with an updateAt parameter.
//     *
//     * @param t        The first generic type T
//     * @param u        The second generic type U
//     * @param updateAt The updateAt parameter
//     * @param <T>      the type of array elements
//     * @param <U>      the type of array elements
//     * @return A QueryWrapper instance
//     */
//    public static <T, U> QueryWrapper<T> createQueryWrapper(final T t, final U u, final String updateAt) {
//        QueryWrapper<T> wrapper = new QueryWrapper<>();
//
//        Field[] fields = u.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            String vStr = getFieldValue(field, u);
//
//            if ("timeOrder".equals(field.getName())) {
//                setColumnOrder(wrapper, updateAt, vStr, "timeOrder");
//                continue;
//            }
//
//            if ("nameOrder".equals(field.getName())) {
//                setColumnOrder(wrapper, "name", vStr, "nameOrder");
//                continue;
//            }
//
//            String undLine = StringUtil.camelToUnderline(field.getName());
//
//            //","代表该字段有多个参数
//            List<String> items = splitStr(vStr);
//            if (items.size() >= 2) {
//                wrapper.in(undLine, items);
//            } else if (items.size() == 1) {
//                wrapper.eq(undLine, items.get(0));
//            }
//        }
//        return wrapper;
//    }
//
//    /**
//     * Split a string into a list of substrings.
//     *
//     * @param vStr The string to split
//     * @return A list of substrings
//     */
//    public static List<String> splitStr(final String vStr) {
//        List<String> res = new ArrayList<>();
//
//        String[] sps = StringUtils.split(vStr, ",");
//        if (sps.length == 0) {
//            return res;
//        }
//
//        for (String sp : sps) {
//            if (StringUtils.isBlank(StringUtils.trimToEmpty(sp))) {
//                continue;
//            }
//            res.add(sp);
//        }
//        return res;
//    }
//
//    /**
//     * Sorts the list of strings representing operating systems in ascending order.
//     *
//     * @param colList The list of strings representing operating systems to be sorted
//     * @return A sorted list of strings representing operating systems in ascending order
//     */
//    public static List<String> sortOsColumn(List<String> colList) {
//        Map<Boolean, List<String>> partMap = colList.stream()
//                .collect(Collectors.partitioningBy(e -> !e.contains("preview")));
//        List<String> con = partMap.get(true);
//        List<String> don = partMap.get(false);
//        Collections.sort(con, Collections.reverseOrder());
//        con.addAll(don);
//        return con;
//    }
//
//    /**
//     * Sorts the list of strings representing category in ascending order.
//     *
//     * @param colList The list of strings representing category to be sorted
//     * @return A sorted list of strings representing category in ascending order
//     */
//    public static List<String> sortCategoryColumn(List<String> colList) {
//        if (colList.contains("其他")) {
//            colList.remove("其他");
//            colList.add("其他");
//        }
//        return colList;
//    }
//}
