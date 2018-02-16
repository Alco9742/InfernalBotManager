package net.nilsghesquiere.repositories;

import net.nilsghesquiere.entities.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@RepositoryRestResource(collectionResourceRel = "users", path = "users")
@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{
	Optional<AppUser> findByUsername(@Param("username") String username);
	Optional<AppUser> findById(@Param("Id") Long Id);
}
 