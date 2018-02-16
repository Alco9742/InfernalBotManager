package net.nilsghesquiere.repositories;


import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
//@RepositoryRestResource(collectionResourceRel = "accounts", path = "accounts")
public interface LolAccountRepository extends JpaRepository<LolAccount, Long> {
	Optional<LolAccount> findById(@Param("id") Long id);
	List <LolAccount> findByUser(AppUser user);
	List <LolAccount> findByUserId(@Param("userid") Long userid);
	List <LolAccount> findByUserUsername(@Param("userid") String username);
	void deleteById(@Param("id") Long id);
}
