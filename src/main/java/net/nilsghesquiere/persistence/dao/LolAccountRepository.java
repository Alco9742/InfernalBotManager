package net.nilsghesquiere.persistence.dao;


import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.util.enums.Region;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LolAccountRepository extends JpaRepository<LolAccount, Long> {
	LolAccount findById(@Param("id") Long id);
	List <LolAccount> findByUser(User user);
	List <LolAccount> findByUserId(@Param("userid") Long userid);
	List <LolAccount> findByUserEmail(@Param("email") String email);
	void deleteById(@Param("id") Long id);
	//Methods for client below
	List <LolAccount> findAllByAccountIgnoreCase(@Param("account") String account);
	@Query("SELECT l FROM LolAccount l WHERE l.user.id = :userid AND l.region = :region AND (l.accountStatus = 'READY_FOR_USE' OR l.accountStatus= 'NEW') AND l.active = true ORDER BY l.priority ASC, l.level DESC")
	List <LolAccount> findUsableAccounts(@Param("userid") Long userid, @Param("region") Region region, Pageable pageable);
	@Query("SELECT l FROM LolAccount l WHERE l.user.id = :userid AND l.region = :region AND (l.accountStatus = 'READY_FOR_USE' OR l.accountStatus= 'NEW') AND l.active = true ORDER BY l.priority DESC, l.level ASC")
	List <LolAccount> findBufferAccounts(@Param("userid") Long userid, @Param("region") Region region, Pageable pageable);
	LolAccount findByAccountIgnoreCaseAndRegionAndUserId (@Param("account") String account, @Param("region") Region region, @Param("userid") Long userId);
	LolAccount findByAccountIgnoreCaseAndRegion(@Param("account") String account, @Param("region") Region region);
}
