package com.search.docsearch.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientIPUtil {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientIPUtil.class);

    public static String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = { "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP" };

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (isValidIp(ip)) {
                return extractIp(ip);
            }
        }

        return request.getRemoteAddr();
    }

    public static String getClientIpAdd(final HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String headerName = "x-forwarded-for";
        String ip = request.getHeader(headerName);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个IP才是真实IP,它们按照英文逗号','分割
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (checkIp(ip)) {
            headerName = "Proxy-Client-IP";
            ip = request.getHeader(headerName);
        }
        if (checkIp(ip)) {
            headerName = "WL-Proxy-Client-IP";
            ip = request.getHeader(headerName);
        }
        if (checkIp(ip)) {
            headerName = "HTTP_CLIENT_IP";
            ip = request.getHeader(headerName);
        }
        if (checkIp(ip)) {
            headerName = "HTTP_X_FORWARDED_FOR";
            ip = request.getHeader(headerName);
        }
        if (checkIp(ip)) {
            headerName = "X-Real-IP";
            ip = request.getHeader(headerName);
        }
        if (checkIp(ip)) {
            ip = request.getRemoteAddr();
            // 127.0.0.1 ipv4, 0:0:0:0:0:0:0:1 ipv6
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    LOGGER.error("get local host error: " + e.getMessage());
                    return ip;
                }
                ip = inet.getHostAddress();
            }
        }
        return ip;
    }

    /**
     * Check if the provided string is a valid IP address.
     *
     * @param ip The IP address to check.
     * @return true if the IP address is valid, false otherwise.
     */
    private static boolean checkIp(final String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    private static boolean isValidIp(String ip) {
        return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip);
    }

    private static String extractIp(String ip) {
        if (ip.contains(",")) {
            return ip.split(",")[0];
        }
        return ip;
    }

}
