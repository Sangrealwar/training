package study.multithreading.masterSlave;

import java.util.Map;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/17.
 */
public class SmsRequestFactory extends AbstractRequestFactory {

    public SmsRequestFactory(Map<String, Integer> respDelayConf,
                             int timestampMaxOffset) {
        super(respDelayConf, timestampMaxOffset);

    }

    @Override
    public SimulatedRequest newRequest() {

        return new SimulatedRequest() {

            @Override
            public void printLogs(Logger logger) {
                final int internalDelay = genInternalDelay();
                LogEntry entry = new LogEntry();
                long timeStamp = nextRequestTimestamp();
                entry.timeStamp = timeStamp;
                entry.recordType = "request";

                entry.interfaceType = "SOAP";
                entry.interfaceName = "SMS";
                entry.operationName = "sendSms";
                entry.srcDevice = "OSG";
                entry.dstDevice = "ESB";

                int traceId = seq.getAndIncrement();

                entry.traceId = "0020" + traceIdFormatter.format(traceId);

                logger.printLog(entry);

                timeStamp += internalDelay;
                entry.timeStamp = timeStamp;
                entry.recordType = "response";
                entry.operationName = "sendSmsRsp";
                entry.srcDevice = "ESB";
                entry.dstDevice = "OSG";
                logger.printLog(entry);

                timeStamp += internalDelay;
                entry.timeStamp = timeStamp;
                entry.recordType = "request";
                entry.operationName = "sendSms";
                entry.srcDevice = "ESB";
                entry.dstDevice = "NIG";
                traceId = traceId + 1;
                entry.traceId = "0021" + traceIdFormatter.format(traceId);
                logger.printLog(entry);

                timeStamp += genResponseDelay("NIG");
                entry.timeStamp = timeStamp;
                entry.recordType = "response";
                entry.operationName = "sendSmsRsp";
                entry.srcDevice = "NIG";
                entry.dstDevice = "ESB";
                traceId = traceId + 3;
                entry.traceId = "0021" + traceIdFormatter.format(traceId);
                logger.printLog(entry);

            }

            @Override
            public String getInterfaceName() {
                return "SMS";
            }

        };
    }
}
