package net.nilsghesquiere.repositories;

import net.nilsghesquiere.entities.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

//@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
}
