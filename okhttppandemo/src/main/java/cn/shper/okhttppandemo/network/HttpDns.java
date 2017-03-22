package cn.shper.okhttppandemo.network;

import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import cn.shper.okhttppandemo.util.Logger;
import okhttp3.Dns;

/**
 * Description: TODO
 * Author: xupan.shi
 * Version: V0.1 2017/3/22
 */
public class HttpDns implements Dns {

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        String ip = DNSHelper.getIpByHost(hostname);
        Logger.d("hostname: " + hostname + " [" + ip + "]");

        if (TextUtils.isEmpty(ip)) {
            return SYSTEM.lookup(hostname);
        }

        return Arrays.asList(InetAddress.getAllByName(ip));
    }

}
