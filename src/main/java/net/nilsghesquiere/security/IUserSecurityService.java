package net.nilsghesquiere.security;

import net.nilsghesquiere.entities.User;

public interface IUserSecurityService {

	User validatePasswordResetToken(long id, String token);

}