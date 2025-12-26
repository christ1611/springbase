package com.springbase.core.security.util;

import com.springbase.core.exception.CoreException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class IpAddressUtils {

    public static String getForwardedIp(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");

        int commIdx = StringUtils.indexOf(ip, ",");
        if (commIdx > 0) {
            ip = StringUtils.substring(ip, 0, commIdx);
        }
        if (ip != null) {
            if (ip.length() > 15)
                return ip.substring(0, 15);
        }
        return ip;
    }

    public static String getUserIpaddress(HttpServletRequest request) throws UnknownHostException {
        // On docker environment, X-Forwarded-For is set with Nginx container ip address.
        // Therefore, to get real client ip address, X-Real-IP gives the connecting client ip address.
        String ip = request.getHeader("X-Real-IP");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        int commIdx = StringUtils.indexOf(ip, ",");
        if (commIdx > 0) {
            ip = StringUtils.substring(ip, 0, commIdx);
        }
        if (ip != null) {
            if (ip.length() > 15)
                return ip.substring(0, 15);
        }

        if(StringUtils.equals(ip, "0:0:0:0:0:0:0:1")){
            InetAddress address = InetAddress.getLocalHost();
            ip =  address.getHostAddress();
        }

        return ip;
    }

    public static void checkIp() throws CoreException {
        // IP Address Check LOGIC
//        if(StringUtils.isNotEmpty(SysInfo.getCtxUserIpAddr())){
//            String[] parts = SysInfo.getCtxUserIpAddr().split("\\.");
//            // IPv4 Check
//            if (parts.length != 4){
//                throw new OneQOnCoreException(CoreErrCode.ADMIN_ALL);
//            }
//            // IPv4 Range Check
//            for (String part : parts) {
//                int value = Integer.parseInt(part);
//                if (value < 0 || value > 255)
//                    throw new OneQOnCoreException(CoreErrCode.ADMIN_ALL);
//            }
//        }
    }
}
