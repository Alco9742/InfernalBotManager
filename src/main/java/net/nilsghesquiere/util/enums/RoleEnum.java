package net.nilsghesquiere.util.enums;

public enum RoleEnum {
	ADMIN("ADMIN"),
	USER("USER"),
	PAID_USER("PAID_USER");
	
	private String name;

	private RoleEnum(String name){
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

