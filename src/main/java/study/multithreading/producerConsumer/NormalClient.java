package study.multithreading.producerConsumer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 名称：
 * 功能：单一队列的通道实现客户端
 * 条件：
 * Created by wq on 2017/8/20.
 */
public class NormalClient {

    public static void main(String[] args) {
        AttachmentProcessor ap = new AttachmentProcessor();
        ap.init();
        InputStream in = new ByteArrayInputStream("Hello".getBytes());
        try {
            ap.saveAttachment(in, "000887282", "测试文档");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
