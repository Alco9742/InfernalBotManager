package net.nilsghesquiere.web.dto;

import java.io.Serializable;
import java.util.Set;

import lombok.Data;

@Data
public class LoginAttemptDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private String ip;
	private int attempts;
	private Set<String> usernames;
	private String usernamesString;
	

	public LoginAttemptDTO(){
		super();
	}
	
	public void setUsernames(Set<String> usernames){
		this.usernames = usernames;
		String prefix = "";
		StringBuilder builder = new StringBuilder();
		for (String username : usernames){
			builder.append(prefix);
			prefix = ", ";
			builder.append(username);
		}
		this.usernamesString = builder.toString();
	}
}
