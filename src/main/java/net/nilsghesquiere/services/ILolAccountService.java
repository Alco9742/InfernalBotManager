package net.nilsghesquiere.services;

import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.LolAccount;

public interface ILolAccountService {
	LolAccount read(Long id);
	Optional <LolAccount> findById(Long id);
	List<LolAccount> findAll();
	LolAccount create(LolAccount lolAccount);
	void delete(LolAccount lolAccount);
	LolAccount update(LolAccount lolAccount);
	List<LolAccount> findByUser(User user);
	List<LolAccount> findByUserId(Long userId);
	List<LolAccount> findByUserUsername(String username);
	void deleteById(Long id);
}
