package study.multithreading.immutableObject;


import java.util.Date;

/**
 * 名称：不可变对象-ImmutableObject
 * 功能：彩信中心
 * 条件：
 * Created by wq on 2017/8/5.
 */
public final class MMSCInfo {

    /**
     * 设备编号
     */
    private final String deviceID;

    /**
     * 彩信中心url
     */
    private final String url;

    /**
     * 彩信中心最大附件大小
     */
    private final int maxAttachmentSizeInBytes;

    private final Date date;

    public MMSCInfo(String deviceID, String url, int maxAttachmentSizeInBytes) {
        this.deviceID = deviceID;
        this.url = url;
        this.maxAttachmentSizeInBytes = maxAttachmentSizeInBytes;
        this.date = new Date();
    }

    public MMSCInfo(MMSCInfo prototype) {
        this.deviceID = prototype.getDeviceID();
        this.url = prototype.getUrl();
        this.maxAttachmentSizeInBytes = prototype.getMaxAttachmentSizeInBytes();
        this.date = new Date();
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getUrl() {
        return url;
    }

    public int getMaxAttachmentSizeInBytes() {
        return maxAttachmentSizeInBytes;
    }

    @Override
    public String toString() {
        return "MMSCInfo{" +
                "deviceID='" + deviceID + '\'' +
                ", url='" + url + '\'' +
                ", maxAttachmentSizeInBytes=" + maxAttachmentSizeInBytes +
                ", date=" + date +
                '}';
    }
}
