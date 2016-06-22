package cn.shper.okhttppan.constant;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-7 C 创建
 */
public class HttpError extends RuntimeException {

    /**
     * 未知错误类型*
     */
    public static final int ERR_CODE_UNKNOWN = -1;
    public static final String ERR_CODE_UNKNOWN_MSG = "呜～我真的不知道发生了什么...";

    /**
     * 登录超时
     */
    public static final int ERR_CODE_LOGING_TIMEOUT = -2;
    public static final String ERR_CODE_LOGING_TIMEOUT_MSG = "呀～据说你要重新登录了...";

    /**
     * 网络错误*
     */
    public static final int ERR_CODE_NETWORK_ERROR = 1001;
    public static final String ERR_CODE_NETWORK_ERROR_MSG = "呜～网络被妖怪抓走啦";

    /**
     * 服务器异常*
     */
    public static final int ERR_CODE_SERVER_ERROR = 1002;
    public static final String ERR_CODE_SERVER_ERROR_MSG = "呀～服务器打了个嗝...";

    /**
     * 连接超时
     */
    public static final int ERR_CODE_CONNECT_TIMEOUT = 1003;
    public static final String ERR_CODE_CONNECT_TIMEOUT_MSG = "呜～服务器被妖怪抓走啦...";

    /**
     * SOCKET 超时
     */
    public static final int ERR_CODE_SOCKET_TIMEOUT = 1004;
    public static final String ERR_CODE_SOCKET_TIMEOUT_MSG = "呜～服务器被妖怪抓走啦...";

    /**
     * 数据为空
     */
    public static final int ERR_CODE_DATA_EMPTY = 9001;
    public static final String ERR_CODE_DATA_EMPTY_MSG = "哇哦～没有数据耶...";

    /**
     * 链接被取消
     */
    public static final int ERR_CODE_CANCELED = 9002;
    public static final String ERR_CODE_CANCELED_MSG = "咦～你刚才是不是点了取消...";

    /**
     * 操作失败
     */
    public static final int ERR_CODE_FAILED = 9003;
    public static final String ERR_CODE_FAILED_MSG = "咦～失败了...";

    private int code = ERR_CODE_UNKNOWN;
    private String msg = ERR_CODE_UNKNOWN_MSG;
    private String title;
    private JSONObject info = new JSONObject();

    public HttpError(int code) {
        this.code = code;
    }

    public HttpError(int code, String message) {
        super(message);
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JSONObject getInfo() {
        return info;
    }

    public void setInfo(JSONObject info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return getMessage(code);
    }

    @Override
    public String getMessage() {
        String errorMessage = getMessage(code);
        if (TextUtils.isEmpty(errorMessage) && !TextUtils.isEmpty(super.getMessage())) {
            return super.getMessage();
        }
        return errorMessage;
    }

    public static String getMessage(int code) {
        switch (code) {
            case ERR_CODE_LOGING_TIMEOUT:
                return ERR_CODE_LOGING_TIMEOUT_MSG;
            case ERR_CODE_NETWORK_ERROR:
                return ERR_CODE_NETWORK_ERROR_MSG;
            case ERR_CODE_SERVER_ERROR:
                return ERR_CODE_SERVER_ERROR_MSG;
            case ERR_CODE_CONNECT_TIMEOUT:
                return ERR_CODE_CONNECT_TIMEOUT_MSG;
            case ERR_CODE_SOCKET_TIMEOUT:
                return ERR_CODE_SOCKET_TIMEOUT_MSG;
            case ERR_CODE_DATA_EMPTY:
                return ERR_CODE_DATA_EMPTY_MSG;
            case ERR_CODE_CANCELED:
                return ERR_CODE_CANCELED_MSG;
            default:
                return ERR_CODE_UNKNOWN_MSG;
        }
    }

}
