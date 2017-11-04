package spittr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 用户
 *
 * @author wq
 * repository：2016-11-20
 *
 */
@Entity
public class Spitter {
	@Id
	private Long id;
	
	@NotNull
	@Size(min=5,max=16,message="用户名出错啦")
	private String username;

	@NotNull
	@Size(min=5,max=25,message="密码出错啦")
	private String password;

	@NotNull
	@Size(min=2,max=30,message="姓出错啦")
	private String firstName;

	@NotNull
	@Size(min=2,max=30,message="名出错啦")
	private String lastName;	
	
	public Spitter(){}
	
	public Spitter(String username, String password, String firstName,
                   String lastName) {
		this.id=null;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Spitter(Long id, String username, String password, String firstName,
                   String lastName) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	

	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this,that,"username");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "username");
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
