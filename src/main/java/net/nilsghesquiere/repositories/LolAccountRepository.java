package net.nilsghesquiere.repositories;


import java.util.List;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "accounts", path = "accounts")
public interface LolAccountRepository extends JpaRepository<LolAccount, Long> {
	List<LolAccount> findByUser(AppUser user);
	List<LolAccount> findByUserId(@Param("userid") Long userid);
}
