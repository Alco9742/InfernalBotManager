package net.nilsghesquiere.persistence.dao;


import java.util.List;

import net.nilsghesquiere.entities.ClientData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDataRepository extends JpaRepository<ClientData, Long> {
	void deleteById(Long id);
	List<ClientData> findAll();
	ClientData findByClientId(Long clientId);
	List<ClientData> findByClientUserId(Long userid);
}
