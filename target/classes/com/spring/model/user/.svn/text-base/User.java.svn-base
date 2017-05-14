package com.spring.model.user;

public class User {
	
	//@Autowired
	private String username;
	//@Autowired
	private String password;
	public void setUsername(String username) {
		this.username=username;
	}
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.password=password;
	}
	
	
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

}
