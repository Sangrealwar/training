package study.multithreading.threadSpecificStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 名称：
 * 功能：ThreadLocal的伪内存泄露
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class MemoryPseduoLeakingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final static ThreadLocal<AtomicInteger> TL_COUNTER = new ThreadLocal<AtomicInteger>() {
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger();
        }

    };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        PrintWriter pwr = resp.getWriter();
        pwr.write(TL_COUNTER.get().getAndIncrement());
        pwr.close();

    }

}
