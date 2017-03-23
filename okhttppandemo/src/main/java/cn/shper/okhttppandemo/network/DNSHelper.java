package cn.shper.okhttppandemo.network;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import cn.shper.okhttppandemo.util.Logger;

/**
 * Description: TODO
 * Author: Shper
 * Version: V0.1 2017/3/22
 */
public class DNSHelper {

    private static final String HTTP_DNS_PARSE_URL = "http://119.29.29.29/d?dn=";

    private DNSHelper() {
    }

    public static String getIpByHost(String hostname) {
        String result = httpRequest(HTTP_DNS_PARSE_URL + hostname);

        if (TextUtils.isEmpty(result) || !result.contains(";")) {
            return result;
        }

        String[] ips = httpRequest(HTTP_DNS_PARSE_URL + hostname).split(";");
        return ips.length > 0 ? ips[0] : "";
    }

    public static String getIpUrl(String url, String host, String ip) {
        if (url == null || host == null || ip == null) {
            Logger.d("TAG", "params is Empty!!!");
            return null;
        }

        return url.replaceFirst(host, ip);
    }

    private static String httpRequest(String url) {
        String result = "";
        BufferedReader in = null;

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while (null != (line = in.readLine())) {
                result += line;
            }
        } catch (Exception e) {
            Logger.e("HttpRequest[GET] Error: " + e);
            e.printStackTrace();
        }

        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
