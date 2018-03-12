package net.nilsghesquiere.persistence.dao;


import net.nilsghesquiere.entities.ClientStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientStatusRepository extends JpaRepository<ClientStatus, Long> {

}
