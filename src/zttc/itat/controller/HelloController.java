package zttc.itat.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping({"/hello","/"})
	public String hello(String username,Model model){
		System.out.println("hello");
		model.addAttribute("username",username);
		System.out.println(username);
		return "hello";
	}
	
	/* @RequestMapping("/welcome")
	public String welcome(){
		return "welcome";
		
	} */
}
