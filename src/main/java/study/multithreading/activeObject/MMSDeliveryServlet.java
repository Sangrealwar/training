package study.multithreading.activeObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


/*
*
* ActiveObject 通过将方法的调用和执行分离，有利的提高了并发性，从而提高了系统的吞吐率
*
* 好处是将任务（MethodRequest）的提交（调用异步方法）和任务的执行策略（Execution Policy）分离
* Scheduler封装任务的执行策略（包括执行顺序，多少任务并发，如果有任务由于系统过载被拒绝，选中哪个任务牺牲等）
*
*
*
* */



/**
 * 名称：彩信请求的入口类
 * 功能：相当于客户端代码
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class MMSDeliveryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //将请求中的数据解析为内部对象
        MMSDeliverRequest mmsDeliverRequest = this.parseRequest(req.getInputStream());
        Recipient shortNumberRecipient = mmsDeliverRequest.getRecipient();
        Recipient originalNumberRecipient = null;

        try {
            //将接收方短号转换为长号
            originalNumberRecipient = convertShortNumber(shortNumberRecipient);
        } catch (Exception e) {
            //接收方短号转换成长号时发生数据库异常，触发请求消息缓存
            AsyncRequestPersistence.getInstance().store(mmsDeliverRequest);


            resp.setStatus(202);
        }

        super.doPost(req, resp);
    }

    private MMSDeliverRequest parseRequest(InputStream reqInputStream) {
        MMSDeliverRequest mmsDeliverRequest = new MMSDeliverRequest();

        return mmsDeliverRequest;
    }

    private Recipient convertShortNumber(Recipient shortNumberRecipient) {
        Recipient recipient = null;

        return recipient;
    }
}
