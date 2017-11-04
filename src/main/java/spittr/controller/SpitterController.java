package spittr.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spittr.model.Spitter;
import spittr.repository.IJpaRepository.JpaSpitterRepository;
import spittr.repository.IRepository.BaseRepository;
import spittr.repository.IRepository.SpitterRepository;
import spittr.web.SpittleNotFoundException;

/**
 * 应用用户
 *
 * @author wq
 *         repository：2016-11-19
 */
@Controller
@RequestMapping("/spitter")
public class SpitterController {
    /**
     * Long最大值
     */
    private static final String PAGE = "1";

    //引入jpa的Repository
    private spittr.repository.IJpaRepository.JpaSpitterRepository spitterRepository;

    private SpitterRepository normalSpitterRepository;
    private BaseRepository baseRepository;

    @Autowired
    public SpitterController(SpitterRepository normalSpitterRepository, BaseRepository baseRepository, JpaSpitterRepository spitterRepository) {
        this.normalSpitterRepository = normalSpitterRepository;
        this.baseRepository = baseRepository;
        this.spitterRepository = spitterRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String spitters(
            @RequestParam(value = "page", defaultValue = PAGE) int page,
            @RequestParam(value = "count", defaultValue = "50") int count,
            Model model) {
        List<Spitter> spitters = normalSpitterRepository.findSpitters(page, count);
        model.addAttribute("spitters", spitters);
        return "spitters";
    }

    //region 前期代码
    @RequestMapping(value = {"/register", "/"}, method = RequestMethod.GET)
    public String showRegistertionForm(Model model) {
        //TODO 这里查不出数据
        System.out.println(spitterRepository.findByUsername("boy"));
        //前台Thymleaf的haserror('*')
        model.addAttribute("spitter", new Spitter());
        return "registerForm";
    }

    @RequestMapping(value = {"/register", "/"}, method = RequestMethod.POST)
    public String processRegistration(
            @RequestPart("profilePicture") MultipartFile profilePicture,
            @Valid Spitter spitter,
            Errors errors, RedirectAttributes model) throws IllegalStateException, IOException {
        //JSR（java specification requests）303  Bean validation是JavaBean的一种验证模型
        //出于分层导致验证逻辑出现在不同层，且代码冗余，存在语义一致性问题
        //需要引入实现了这个标准Api的类 Hibernate Validator是其中的一种实现
        if (errors.hasErrors())
            return "registerForm";

        if (spitter == null)
            return "redirect:/register";

        //保存文件
//		if(!profilePicture.isEmpty())
//		{
//			//绝对路径可以，相对路径报错
//			profilePicture.transferTo(
////				new File("/repository/"+ profilePicture.getOriginalFilename()));
//			new File("E:/repository/spittr/"+ profilePicture.getOriginalFilename()));
//		}
        normalSpitterRepository.add(spitter);
        //1.通过url重定向传递参数
        //如果添加的参数没有在url上设置占位符，会在url上拼接?xxx=yyy
        model.addAttribute("username", spitter.getUsername());
        model.addAttribute("spitterId", spitter.getId());
        return "redirect:/spitter/{username}";  //重定向
        //2.使用flash，将变量存在会话中，重定向时从会话中获取
//		model.addAttribute("username",spitter.getUsername());
//		model.addFlashAttribute("spitter",spitter);
//		return "redirect:/spitter/"+spitter.getUsername();  //重定向
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showSpitterProfile(@PathVariable String username,
                                     Model model) {
        // 2.从flash中查找重定向前传过来的model
        if (!model.containsAttribute("spitter")) {
            Spitter spitter = normalSpitterRepository.findByUsername(username);
            if (spitter == null)
                throw new SpittleNotFoundException();
            model.addAttribute(spitter);
        }

        return "profile";
    }
    //endregion
}
