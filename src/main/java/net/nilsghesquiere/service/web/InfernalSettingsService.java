package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;

public interface InfernalSettingsService {
	InfernalSettings read(Long id);
	InfernalSettings create(InfernalSettings infernalSettings);
	InfernalSettings update(InfernalSettings infernalSettings);
	void delete(InfernalSettings infernalSettings);
	void deleteById(Long id);
	InfernalSettings getByUserId(Long userid);
	List<InfernalSettings> findByUser(User currentUser);
	InfernalSettings findByUserIdAndSets(Long id, String sets);
}
