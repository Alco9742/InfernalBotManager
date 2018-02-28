package net.nilsghesquiere.service.web;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;

public interface IInfernalSettingsService {
	InfernalSettings read(Long id);
	InfernalSettings create(InfernalSettings infernalSettings);
	InfernalSettings update(InfernalSettings infernalSettings);
	void deleteById(Long id);
}
