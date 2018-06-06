package net.nilsghesquiere.persistence.dao;


import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.util.enums.ClientStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	Client getById(@Param("id") Long id);
	Client getByTag(@Param("tag")String tag);
	void deleteById(Long id);
	List<Client> findByUserId(Long userId);
	Client getByUserIdAndTag(Long userId, String tag);
	List<Client> findByClientStatus(ClientStatus status);
}
