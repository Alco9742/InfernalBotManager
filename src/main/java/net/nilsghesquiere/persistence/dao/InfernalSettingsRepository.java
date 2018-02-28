package net.nilsghesquiere.persistence.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.nilsghesquiere.entities.InfernalSettings;

@Repository
public interface InfernalSettingsRepository extends JpaRepository<InfernalSettings, Long> {
	InfernalSettings findById(@Param("id") Long id);
	void deleteById(Long id);
}
