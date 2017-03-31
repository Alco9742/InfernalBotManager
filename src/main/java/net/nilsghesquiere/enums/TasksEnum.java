package net.nilsghesquiere.enums;

public enum TasksEnum {
	FOLLOW("FollowUsersJob"),
	UNFOLLOW("UnfollowUsersJob");

	private String name;

	private TasksEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
