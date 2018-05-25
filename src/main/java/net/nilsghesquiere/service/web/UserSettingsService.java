package net.nilsghesquiere.service.web;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;

public interface UserSettingsService {
	UserSettings read(Long id);
	UserSettings create(UserSettings userSettings);
	void delete(UserSettings userSettings);
	UserSettings update(UserSettings userSettings);
	UserSettings getByUser(User user);
	UserSettings getByUserId(Long userId);
	void deleteById(Long id);
}
