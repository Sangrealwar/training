package spittr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spittr.model.Spittle;
import spittr.repository.IRepository.SpittleRepository;

import java.util.Date;
import java.util.List;

/**
 * 用户更新信息控制器
 * 
 * 接手请求的输入
 * 1.查询参数
 *    标记@RequestParam参数设置，地址栏传值
 * 2.表单参数
 * 3.路径变量
 *    标记@PathVariable路径参数
 * 
 *
 * @author wq
 * repository：2016-11-19
 *
 */
@Controller
@RequestMapping("/spittles")
public class SpittleController {
	/**
	 * Long最大值
	 */
	private static final String PAGE="1";
	
	private SpittleRepository repository;
	
	@Autowired()
	public SpittleController(SpittleRepository spittleRepository){
		this.repository = spittleRepository;
	}



	//region 前期代码
	/**
	 * 
	 * 查询参数传值
	 * 1.当addAttribute的key不填时，key会根据类型做判断
	 * 2.当返回值为指定类型时，默认返回的视图名为逻辑路径名，即 spittles 
	 * 
	 * @param page 表单参数，总数据最大值
	 * @param count 表单参数，每一页的数量
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String spittles(
			@RequestParam(value="page",defaultValue=PAGE) int page,
			@RequestParam(value="count",defaultValue="50") int count,
			Model model){
		List<Spittle> spittles = repository.findSpittles(page, count);
		model.addAttribute("spittles",spittles);
		return "spittles";
	}
	
	/**
	 * 路径变量传值，对url进行了解析
	 * @param spittleId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{spittleId}",method=RequestMethod.GET)
	public String spittle(@PathVariable("spittleId") long spittleId,
			Model model){
		model.addAttribute(repository.findOne(spittleId));
		Spittle expectedSpittle = new Spittle("Hello",new Date());
		model.addAttribute(expectedSpittle);
		return  "spittle";
	}
	//endregion

	//region 搭配vue前端项目提供的后台api
	@ResponseBody
	@RequestMapping(value = "spittlesApi", method = RequestMethod.GET)
	public List<Spittle> spittlesApi(
			@RequestParam(value = "page", defaultValue = PAGE) int page,
			@RequestParam(value = "count", defaultValue = "50") int count,
			Model model) {

		List<Spittle> spittles = repository.findSpittles(page, count);
		return spittles;
	}
	//endregion
}
