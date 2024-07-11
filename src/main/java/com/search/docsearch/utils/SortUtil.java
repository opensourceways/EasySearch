package com.search.docsearch.utils;

import com.search.docsearch.entity.software.SoftwareDocsAllResponce;

import java.util.*;

public final class SortUtil {
    // Private constructor to prevent instantiation of the utility class
    private SortUtil() {
        // private constructor to hide the implicit public one
        throw new AssertionError("QueryWrapperUtil class cannot be instantiated.");
    }

    /**
     * The order of tags.
     */
    private static final List<String> ORDERS = List.of("RPM", "IMAGE", "EPKG");

    private static final List<String> RESPONCEORDERS = Arrays.asList("rpmpkg", "apppkg", "epkgpkg","oepkg");

    /**
     * sort the tags.
     *
     * @param tags The origin tags
     * @return A list sorted
     */
    public static List<String> sortTags(Collection<String> tags) {
        List<String> list = new ArrayList<>(tags);
        Comparator<String> listCompare = (s1, s2) -> {
            Integer i1 = ORDERS.indexOf(s1);
            Integer i2 = ORDERS.indexOf(s2);
            return i1.compareTo(i2);
        };

        Collections.sort(list, listCompare);
        return list;
    }

    public static void sortResponce(List<SoftwareDocsAllResponce> responce) {
        responce.sort(Comparator.comparingInt(a -> RESPONCEORDERS.indexOf(a.getKey())));
    }
}
