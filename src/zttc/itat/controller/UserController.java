package zttc.itat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.service.*;
import org.hibernate.boot.registry.*;

import java.io.File;
import java.io.IOException;

import zttc.ital.model.User;
import zttc.ital.model.UserException;


@Controller
@RequestMapping("/user")
public class UserController {
	private Map<String ,User> users = new HashMap<String,User>();
	
	public UserController(){
		
	}
	
	@RequestMapping(value="/users",method=RequestMethod.GET)
	public String list(Model model){
		
		
		
		
		Configuration conf = new Configuration().configure();

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
			.applySettings(conf.getProperties()).build();
		// 以Configuration实例创建SessionFactory实例
		SessionFactory sf = conf.buildSessionFactory(serviceRegistry);
		// 创建Session
		Session sess = sf.openSession();
		// 开始事务
		Transaction tx = sess.beginTransaction();
		// 添加
		ScrollableResults userss = sess.createQuery("from User").setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		while(userss.next()) {
			User n = (User) userss.get(0);
			users.put(n.getUsername(), n);
		}
		
		model.addAttribute("users",users);
		
		// 提交事务
		tx.commit();
		// 关闭Session
		sess.close();
		sf.close();
	
		return "user/list";
	}

	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(@ModelAttribute("user") User user){
		//model.addAttribute(new User());
		
		return "user/add";
	}
	
	@RequestMapping(value="add",method=RequestMethod.POST)
	public String add(@Validated User user,BindingResult br,MultipartFile attach,HttpServletRequest req) throws IOException{
		if(br.hasErrors()){
			  List<ObjectError> ls=br.getAllErrors();  
		        for (int i = 0; i < ls.size(); i++) {  
		            System.out.println("error:"+ls.get(i));  
		        }  
			return "user/list";
		}
		
		String realpath = req.getSession().getServletContext().getRealPath("/resources/upload");
		
		System.out.println(realpath);
		
		File f=new File(realpath+"/"+attach.getOriginalFilename());
		FileUtils.copyInputStreamToFile(attach.getInputStream(), f);
		
		//System.out.println(attach.getName()+","+attach.getOriginalFilename()+","+attach.getContentType());
		users.put(user.getUsername(), user);
		
/*
 * 这个是对数据库的操作，新添加了数据		
 */
		Configuration conf = new Configuration().configure();

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
			.applySettings(conf.getProperties()).build();
		// 以Configuration实例创建SessionFactory实例
		SessionFactory sf = conf.buildSessionFactory(serviceRegistry);
		// 创建Session
		Session sess = sf.openSession();
		// 开始事务
		Transaction tx = sess.beginTransaction();
		// 添加
		User n = new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getEmail());
		// 保存消息
		sess.save(n);
		// 提交事务
		tx.commit();
		// 关闭Session
		sess.close();
		sf.close();
		
		
		return "redirect:/user/users";
	}
	
	@RequestMapping(value="/{username}",method=RequestMethod.GET)
	public String show(@PathVariable String username,Model model){
		model.addAttribute(users.get(username));
		return "user/show";
	}
	
	@RequestMapping(value="/{username}/update",method=RequestMethod.GET)
	public String update(@PathVariable String username,Model model){
		
		
		model.addAttribute(users.get(username));
		
		return "user/update";
	}
	
	@RequestMapping(value="/{id}/update",method=RequestMethod.POST)
	public String update(@PathVariable int id,@Validated User user, BindingResult br){
		if(br.hasErrors()){
			return "user/update";
		}
		//users.put(username,user);
		
		/*
		 * 这个是对数据库的操作，新添加了数据		
		 */
				Configuration conf = new Configuration().configure();

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(conf.getProperties()).build();
				// 以Configuration实例创建SessionFactory实例
				SessionFactory sf = conf.buildSessionFactory(serviceRegistry);
				// 创建Session
				Session sess = sf.openSession();
				// 开始事务
				Transaction tx = sess.beginTransaction();
				// 添加
				User n = (User) sess.load(User.class, id);
				n.setUsername(user.getUsername());
				n.setNickname(user.getNickname());
				n.setEmail(user.getEmail());
				n.setPassword(user.getPassword());
				// 提交事务
				tx.commit();
				// 关闭Session
				sess.close();
				sf.close();
		
		return "redirect:/user/users";
	}
	
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable int id,@Validated String username){
		
		/*
		 * 这个是对数据库的操作，新添加了数据		
		 */
				Configuration conf = new Configuration().configure();

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(conf.getProperties()).build();
				// 以Configuration实例创建SessionFactory实例
				SessionFactory sf = conf.buildSessionFactory(serviceRegistry);
				// 创建Session
				Session sess = sf.openSession();
				// 开始事务
				Transaction tx = sess.beginTransaction();
				// 添加
				User n = (User) sess.get(User.class, id);
				sess.delete(n);
				// 提交事务
				tx.commit();
				// 关闭Session
				sess.close();
		
		
		users.remove(username);
		return "redirect:/user/users";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(String username,String password,HttpSession session){
		if(!users.containsKey(username)){
			throw new UserException("用户名不存在");
		}
		User u = users.get(username);
		if(!u.getPassword().equals(password)){
			throw new UserException("用户密码不正确");
		}
		session.setAttribute("loginUser",u);
		return "redirect:/user/users";
	}
	
/*	@ExceptionHandler(value={UserException.class})
	public String handlerException(UserException e,HttpServletRequest req){
		req.setAttribute("e",e);
		
		
		
		return "error";
		
	}*/
	
	
	
	
	
}
