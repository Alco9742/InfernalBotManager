package net.nilsghesquiere.security;

public interface IUserSecurityService {

	String validatePasswordResetToken(long id, String token);

}