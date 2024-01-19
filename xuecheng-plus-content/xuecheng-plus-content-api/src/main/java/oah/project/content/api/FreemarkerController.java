package oah.project.content.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName FreemarkerController
 * @Description Freemarker入门程序
 * @Author _oah
 * @Date 2023.12.08 16:16
 * @Version 1.0
 */
@Controller
public class FreemarkerController {

	@GetMapping("/testfreemarker")
	public ModelAndView test() {
		ModelAndView modelAndView = new ModelAndView();
		// 指定模型
		modelAndView.addObject("name", "小明");
		// 指定模板
		modelAndView.setViewName("test"); // 根据视图名称加上.ftl找到模板
		return modelAndView;
	}
}
