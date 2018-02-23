package net.nilsghesquiere.persistence.dao;


import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.LolAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface LolAccountRepository extends JpaRepository<LolAccount, Long> {
	Optional<LolAccount> findById(@Param("id") Long id);
	List <LolAccount> findByUser(User user);
	List <LolAccount> findByUserId(@Param("userid") Long userid);
	List <LolAccount> findByUserUsername(@Param("userid") String username);
	void deleteById(@Param("id") Long id);
}
