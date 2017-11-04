package spittr.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spittr.model.Spitter;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping({"/", "/homepage"})       //匹配多个url
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    //region 前期代码

    /**
     * 异步请求，GET请求
     * 请求的是json格式，返回的也是json格式，ReponseBody将Map翻译成json作为ajax请求的响应
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ask/{id}", method = RequestMethod.GET)
    public Map<String, Object> ajaxAsk(@PathVariable String id) {
        Map<String, Object> modelMap = new HashMap<String, Object>(1);
        if ("jbauer".equals(id)) {
            modelMap.put("msg", "GET成功");
        } else {
            modelMap.put("msg", "GET失败");
        }
        return modelMap;
    }

    /**
     * 异步请求，POST请求，由于遵循REST设计标准，资源请求在客户端通过异步调用，但csrf拒绝POST请求，必须关闭csrf跨域访问
     * 请求的是json格式，返回的也是json格式，ReponseBody将Map翻译成json作为ajax请求的响应
     *
     * @param spittr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ask", method = RequestMethod.POST)
    public Map<String, Object> ajaxAsk(@RequestBody Spitter spittr) {
        Map<String, Object> modelMap = new HashMap<String, Object>(1);
        if ("jbauer".equals(spittr.getUsername())) {
            modelMap.put("msg", "POST成功");
        } else {
            modelMap.put("msg", "POST失败");
        }
        return modelMap;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    //endregion

    /**
     * 登陆后返回home界面
     *
     * @return
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public String login_do() {
        if (true) {
            return "redirect:/view/home.html";
        } else {
            return "error";
        }
    }
}
