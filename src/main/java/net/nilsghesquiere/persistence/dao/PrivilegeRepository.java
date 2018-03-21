package net.nilsghesquiere.persistence.dao;

import net.nilsghesquiere.entities.Privilege;
import net.nilsghesquiere.entities.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
	Privilege findByName(String name);
}
