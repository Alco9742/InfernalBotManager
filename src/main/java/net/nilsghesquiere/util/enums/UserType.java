package net.nilsghesquiere.util.enums;

public enum UserType {
	ADMIN("ADMIN"),
	USER("USER");
	
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

