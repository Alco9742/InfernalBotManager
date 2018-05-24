package net.nilsghesquiere.persistence.dao;


import java.util.List;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfernalSettingsRepository extends JpaRepository<InfernalSettings, Long> {
	InfernalSettings getById(@Param("id") Long id);
	InfernalSettings getByUserId(@Param("userid")Long userid);
	void deleteById(Long id);
	List<InfernalSettings> findByUser(User user);
	InfernalSettings findByUserIdAndSets(@Param("userid")Long userid, @Param("sets")String sets);
}
