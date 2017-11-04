package spittr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 名称：豆瓣的vue前端控制器
 * 功能：
 * 条件：
 * Created by wq on 2017/8/27.
 */
@Controller
@RequestMapping({"/api/movie"})       //匹配多个url
public class DoubanController {

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> search(@RequestParam("q") String query) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        List<Map<String,Object>> subjects = new ArrayList<Map<String, Object>>();
        Map<String,Object> subject = new HashMap<String,Object>();
        subject.put("title","大圣归来");
        subject.put("original_title","原大圣归来");
        subject.put("year","2015");

        Map<String, Object> rating = new HashMap<String, Object>();
        rating.put("average",4);
        subject.put("rating",rating);

        Map<String, Object> images = new HashMap<String, Object>();
        images.put("medium","http://img4.imgtn.bdimg.com/it/u=2447335290,1618961608&fm=214&gp=0.jpg");
        subject.put("images",images);

        subjects.add(subject);
        modelMap.put("subjects",subjects);

        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/in_theaters", method = RequestMethod.GET)
    public Map<String, Object> inTheaters(@RequestParam("city") String city) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        return modelMap;
    }
}
