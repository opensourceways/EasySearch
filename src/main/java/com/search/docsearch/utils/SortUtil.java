package com.search.docsearch.utils;

import com.search.docsearch.dto.software.*;
import com.search.docsearch.entity.software.SoftwareDocsAllResponce;
import com.search.docsearch.entity.software.SoftwareSearchCondition;
import com.search.docsearch.entity.software.SoftwareSearchResponce;
import org.apache.http.client.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
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

    private static final List<String> RESPONCEORDERS = Arrays.asList("rpmpkg", "apppkg", "epkgpkg", "oepkg");

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

    public static void sortByName(SoftwareSearchResponce softwareSearchResponce, SoftwareSearchCondition condition) {
        List<SoftwareAllDto> all = softwareSearchResponce.getAll();
        List<SoftwareAppChildrenDto> apppkg = softwareSearchResponce.getApppkg();
        List<SoftwareAppVersionDto> appversion = softwareSearchResponce.getAppversion();
        List<SoftwareEpkgDto> epkgpkg = softwareSearchResponce.getEpkgpkg();
        List<SoftwareOepkgDto> oepkg = softwareSearchResponce.getOepkg();
        List<SoftwareRpmDto> rpmpkg = softwareSearchResponce.getRpmpkg();
        if ("asc".equals(condition.getNameOrder())) {
            Collections.sort(all, Comparator.comparing((SoftwareBaseDto p) -> stripHtmlTags(p.getName())));
            Collections.sort(apppkg, Comparator.comparing((SoftwareBaseDto p) -> stripHtmlTags(p.getName())));
            Collections.sort(appversion, Comparator.comparing((SoftwareAppVersionDto p) -> stripHtmlTags(p.getName())));
            Collections.sort(epkgpkg, Comparator.comparing((SoftwareBaseDto p) -> stripHtmlTags(p.getName())));
            Collections.sort(oepkg, Comparator.comparing((SoftwareOepkgDto p) -> stripHtmlTags(p.getName())));
            Collections.sort(rpmpkg, Comparator.comparing((SoftwareRpmDto p) -> stripHtmlTags(p.getName())));
        }

        if ("desc".equals(condition.getNameOrder())) {
            Collections.sort(all, Comparator.comparing((SoftwareBaseDto p) -> stripHtmlTags(p.getName())).reversed());
            Collections.sort(apppkg, Comparator.comparing((SoftwareBaseDto p) -> stripHtmlTags(p.getName())).reversed());
            Collections.sort(appversion, Comparator.comparing((SoftwareAppVersionDto p) -> stripHtmlTags(p.getName())).reversed());
            Collections.sort(epkgpkg, Comparator.comparing((SoftwareBaseDto p) -> stripHtmlTags(p.getName())).reversed());
            Collections.sort(oepkg, Comparator.comparing((SoftwareOepkgDto p) -> stripHtmlTags(p.getName())).reversed());
            Collections.sort(rpmpkg, Comparator.comparing((SoftwareRpmDto p) -> stripHtmlTags(p.getName())).reversed());

        }

        int start = (condition.getPageNum() - 1) * condition.getPageSize();
        int end = start + condition.getPageSize();

        softwareSearchResponce.setAll(subList(start, end, all));
        softwareSearchResponce.setApppkg(subList(start, end, apppkg));
        softwareSearchResponce.setAppversion(subList(start, end, appversion));
        softwareSearchResponce.setEpkgpkg(subList(start, end, epkgpkg));
        softwareSearchResponce.setOepkg(subList(start, end, oepkg));
        softwareSearchResponce.setRpmpkg(subList(start, end, rpmpkg));
        if (softwareSearchResponce.getTotal() > 100) {
            softwareSearchResponce.setTotal(100);
        }
    }


    public static List subList(Integer start, Integer end, List originList) {
        List subList = null;
        if (start < originList.size() && end <= originList.size()) {
            subList = originList.subList(start, end);
        } else if (start < originList.size() && end > originList.size()) {
            subList = originList.subList(start, originList.size());
        } else {
            subList = new ArrayList();
        }
        return subList;
    }

    public static void sortByTime(SoftwareSearchResponce softwareSearchResponce, String timeOrder) {


        List<SoftwareEpkgDto> epkgpkg = softwareSearchResponce.getEpkgpkg();
        List<SoftwareOepkgDto> oepkg = softwareSearchResponce.getOepkg();
        List<SoftwareRpmDto> rpmpkg = softwareSearchResponce.getRpmpkg();
        if ("asc".equals(timeOrder)) {
            Collections.sort(epkgpkg, Comparator.comparing((SoftwareEpkgDto p) -> getTimeStamp(p.getEpkgUpdateAt())));
            Collections.sort(oepkg, Comparator.comparing((SoftwareOepkgDto p) -> getTimeStamp(p.getRpmUpdateAt())));
            Collections.sort(rpmpkg, Comparator.comparing((SoftwareRpmDto p) -> getTimeStamp(p.getRpmUpdateAt())));
        }

        if ("desc".equals(timeOrder)) {
            Collections.sort(epkgpkg, Comparator.comparing((SoftwareEpkgDto p) -> getTimeStamp(p.getEpkgUpdateAt())).reversed());
            Collections.sort(oepkg, Comparator.comparing((SoftwareOepkgDto p) -> getTimeStamp(p.getRpmUpdateAt())).reversed());
            Collections.sort(rpmpkg, Comparator.comparing((SoftwareRpmDto p) -> getTimeStamp(p.getRpmUpdateAt())).reversed());

        }

    }

    // 去除 HTML 标签的辅助方法
    private static String stripHtmlTags(String input) {
        return input.replaceAll("<[^>]*>", "");
    }


    private static long getTimeStamp(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = dateFormat.parse(dateStr);
            long timestamp = date.getTime() / 1000;  // 转换为秒级时间戳
            return timestamp;
        } catch (Exception e) {

        }
        return 0;
    }


    private static long getISOTimeStamp(String dateStr) {
        // 解析 ISO 8601 格式的日期时间字符串
        Instant instant = Instant.parse(dateStr);

        // 转换为时间戳（毫秒级）
        long timestampMillis = instant.toEpochMilli();

        // 如果需要秒级时间戳，可以将毫秒级时间戳除以 1000
        long timestampSeconds = instant.getEpochSecond();
        return timestampSeconds;
    }
}