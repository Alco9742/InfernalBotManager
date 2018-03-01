package net.nilsghesquiere.persistence.dao;


import net.nilsghesquiere.entities.InfernalSettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfernalSettingsRepository extends JpaRepository<InfernalSettings, Long> {
	InfernalSettings getById(@Param("id") Long id);
	InfernalSettings getByUserId(@Param("userid")Long userid);
	void deleteById(Long id);
}
