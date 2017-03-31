package net.nilsghesquiere.repositories;

import net.nilsghesquiere.entities.AppUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends CrudRepository<AppUser, Long>{
	AppUser findByUsername(@Param("username") String username);
}
 