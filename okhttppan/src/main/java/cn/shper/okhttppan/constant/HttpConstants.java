package cn.shper.okhttppan.constant;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-8 C 创建
 */
public interface HttpConstants {

    interface Timeout {
        int DEFAULT_CONNECT = 15;
        int DEFAULT_READ = 35;
        int DEFAULT_WRITE = 35;

        int DOWNLOAD_CONNECT = 15;
        int DOWNLOAD_READ = 180;
        int DOWNLOAD_WRITE = 35;

        int UPLOAD_CONNECT = 15;
        int UPLOAD_READ = 35;
        int UPLOAD_WRITE = 180;
    }

    interface Method {
        String GET = "GET";
        String POST = "POST";
        String DOWNLOAD = "DOWNLOAD";
        String UPLOAD = "UPLOAD";
    }

}
