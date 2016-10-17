package zttc.ital.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name="t_user")
public class User {
	
	@GeneratedValue
	@Id
	private int id;
	private String username;
	private String password;
	private String nickname;
	private String email;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//prototype
	public User(String username,String password,String nickname,String email){
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.email = email;
	}
	
	//single
	public User(){
		super();
	}
	
	@NotEmpty(message="用户名不为空")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Size(min=1,max=19,message="长度不符合")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Email(message="邮箱格式")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
 
}
