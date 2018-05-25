package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.ImportSettings;
import net.nilsghesquiere.entities.User;

public interface ImportSettingsService {
	ImportSettings read(Long id);
	ImportSettings create(ImportSettings importSettings);
	void delete(ImportSettings importSettings);
	ImportSettings update(ImportSettings importSettings);
	List<ImportSettings> findByUser(User user);
	List<ImportSettings> findByUserId(Long userId);
	void deleteById(Long id);
	ImportSettings findByUserIdAndName(Long userid, String name);
}
