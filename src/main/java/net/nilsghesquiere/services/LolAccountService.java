package net.nilsghesquiere.services;

import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;

public interface LolAccountService {
	LolAccount read(Long id);
	Optional <LolAccount> findById(Long id);
	List<LolAccount> findAll();
	LolAccount create(LolAccount lolAccount);
	void delete(LolAccount lolAccount);
	LolAccount update(LolAccount lolAccount);
	List<LolAccount> findByUser(AppUser user);
	List<LolAccount> findByUserId(Long userId);
	List<LolAccount> findByUserUsername(String username);
	void deleteById(Long id);
}
