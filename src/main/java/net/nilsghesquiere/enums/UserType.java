package net.nilsghesquiere.enums;

public enum UserType {
	ADMIN("admin"),
	USER("user");
	
	private String name;

	private UserType(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String toString(){
		return this.name;
	}
}

