package study.multithreading.serialThredConfinement;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/16.
 */
public class SampleClient {
    private static final MessageFileDownloader DOWNLOADER;

    static {
        DOWNLOADER = new MessageFileDownloader("D:\\tmp\\ftp", "192.168.1.1", "admin", "123456");
        DOWNLOADER.init();
    }

    public static void main(String[] args) {
//        DOWNLOADER.downloadFile("abc.xml");
    }
}
