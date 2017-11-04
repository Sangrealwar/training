package study.activemq.request_reply;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/3/21.
 */
public class Config {
    public static final java.lang.String AUTHOR = "Mike Tang";

    public static final java.lang.String QUEUE_NAME = "REQUEST AND REPLY";

    public static void introduce() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("This is a simple example to show how to write a \n");
        builder.append("request and reply pattern program with Apache ActiveMQ.\n");
        builder.append("which is not support such pattern.\n\n");
        builder.append("                       -------- By Mike Tang\n");
        builder.append("                   2014-4-17 at Soochow University\n");
        System.out.println(builder.toString());
    }

    public static void introduceServer() {
        System.out.println("Wait for the client send messages, and you will see something");
    }

    public static void introduceClient() {
        System.out.println("Input something and ENTER, you will get the reply.\n"
                + "and input 'exit' stop the program.");
    }
}
