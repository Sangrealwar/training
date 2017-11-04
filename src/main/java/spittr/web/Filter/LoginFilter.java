package spittr.web.Filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/3/6.
 */
public class LoginFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        filterChain.doFilter(request,response);

        //不过滤的uri
        String[] notFilter=new String[]{"http://localhost:82/view/spitter/list.html"};
        //请求的URI
        String uri=request.getRequestURI();
        //uri中包含frame时才进行过滤
//        if(uri.indexOf("public")!=-1 || uri.indexOf("pub")!=-1){
        if(true){
            //是否过滤
            boolean doFilter=true;
            for(String s:notFilter){
                if(uri.indexOf(s)!=-1){
                    //如果uri中包含不过滤的uri，则不进行过滤
                    doFilter=false;
                    break;
                }
            }
            if(doFilter){
                //执行过滤
                //获取登陆者实体
/*
                String LoginUserName= LoginUserInfo.getLoginUserName();
*/
                request.getSession().getAttribute("loginer");
//                String LoginUserName=request.getSession().getAttribute("loginer").toString();
                String LoginUserName="";
                if(null==LoginUserName || "".equals(LoginUserName.trim())){
//                    response.sendRedirect("http://www.baidu.com");
                    filterChain.doFilter(request,response);
                }
                else{
//                    如果session中存在登陆者实体，则继续
                    filterChain.doFilter(request,response);
                }
            }
            else{
                //如果不执行，则继续
                filterChain.doFilter(request,response);
            }
        }
        else{
            //如果uri中不包括pub，则继续
            filterChain.doFilter(request,response);
        }
    }
}
