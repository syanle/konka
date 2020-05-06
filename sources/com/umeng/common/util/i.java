package com.umeng.common.util;

import com.tencent.stat.common.StatConstants;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/* compiled from: NetUtil */
public class i {
    public static String a(Map<String, Object> map, String str) {
        String obj;
        if (map == null || map.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        Set<String> keySet = map.keySet();
        if (!str.endsWith("?")) {
            sb.append("?");
        }
        for (String str2 : keySet) {
            StringBuilder append = new StringBuilder(String.valueOf(URLEncoder.encode(str2))).append("=");
            if (map.get(str2) == null) {
                obj = StatConstants.MTA_COOPERATION_TAG;
            } else {
                obj = map.get(str2).toString();
            }
            sb.append(append.append(URLEncoder.encode(obj)).append("&").toString());
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
