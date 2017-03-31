package net.nilsghesquiere.services;

import java.util.List;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;

public interface LolAccountService {
	LolAccount read(Long id);
	List<LolAccount> findAll();
	void create(LolAccount lolAccount);
	void delete(LolAccount lolAccount);
	void update(LolAccount lolAccount);
	List<LolAccount> findByUser(AppUser user);
}
