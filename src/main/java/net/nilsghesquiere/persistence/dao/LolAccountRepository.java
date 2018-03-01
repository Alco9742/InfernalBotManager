package net.nilsghesquiere.persistence.dao;


import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.util.enums.Region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LolAccountRepository extends JpaRepository<LolAccount, Long> {
	Optional<LolAccount> findById(@Param("id") Long id);
	List <LolAccount> findByUser(User user);
	List <LolAccount> findByUserId(@Param("userid") Long userid);
	List <LolAccount> findByUserEmail(@Param("email") String email);
	void deleteById(@Param("id") Long id);
	@Query("SELECT TOP 5 l FROM lolaccounts l WHERE p.id = :userid AND p.region = :region AND p.accountstatus = READY_FOR_USE AND p.active = true ORDER BY p.priority ASC, p.level DESC")
	List <LolAccount> findUsableAccounts(@Param("userid") Long userid, @Param("region") Region region);
}
