package net.nilsghesquiere.preconditions;

import net.nilsghesquiere.exceptions.MyResourceNotFoundException;

public class RestPreconditions {
	public static <T> T checkFound(T resource) {
		if (resource == null) {
			throw new MyResourceNotFoundException();
		}
		return resource;
	}
}
