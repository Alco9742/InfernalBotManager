package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.User;

public interface ClientSettingsService {
	ClientSettings read(Long id);
	ClientSettings create(ClientSettings clientSettings);
	void delete(ClientSettings clientSettings);
	ClientSettings update(ClientSettings clientSettings);
	List<ClientSettings> findByUser(User user);
	List<ClientSettings> findByUserId(Long userId);
	void deleteById(Long id);
	ClientSettings findByUserIdAndName(Long userid, String name);
}
