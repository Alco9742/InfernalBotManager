package net.nilsghesquiere.persistence.dao;


import java.util.List;

import net.nilsghesquiere.entities.ClientSettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientSettingsRepository extends JpaRepository<ClientSettings, Long> {
	ClientSettings getById(@Param("id") Long id);
	List<ClientSettings> getByUserId(@Param("userid")Long userid);
	void deleteById(Long id);
}
