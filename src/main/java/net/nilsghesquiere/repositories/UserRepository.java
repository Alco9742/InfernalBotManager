package net.nilsghesquiere.repositories;

import net.nilsghesquiere.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(@Param("username") String username);
	User findById(@Param("Id") Long Id);
	User findByEmail(String email);
}
 