package study.multithreading.masterSlave;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/17.
 */
public class CaseRunner {
    public static void main(String[] args) throws Exception {

        String logFileBaseDir = System.getProperty("java.io.tmpdir") + "/tps/";
        final Pattern pattern;

        String matchingRegExp;

        // 用于选择要进行统计的接口日志文件的正则表达式，请根据实际情况修改。
        matchingRegExp = "20150420131[0-9]";

        pattern = Pattern.compile("ESB_interface_" + matchingRegExp + ".log");
        PipedInputStream pipeIn = new PipedInputStream();
        final PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);

        File dir = new File(logFileBaseDir);
        dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Matcher matcher = pattern.matcher(name);
                boolean toAccept = matcher.matches();
                if (toAccept) {
                    try {

                        // 向TPSStat输出待统计的接口日志文件名
                        pipeOut.write((name + "\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return toAccept;
            }
        });

        pipeOut.flush();
        pipeOut.close();
        System.setIn(pipeIn);
        TPSStat.main(new String[] { logFileBaseDir });
    }
}
