package net.nilsghesquiere.persistence.dao;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findById(@Param("Id") Long Id);
	User findByEmailIgnoreCase(@Param("email")String email);
	User getUserByClients(Client client);
}
 