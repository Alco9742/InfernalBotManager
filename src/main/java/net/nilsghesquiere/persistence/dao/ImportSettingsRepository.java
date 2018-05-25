package net.nilsghesquiere.persistence.dao;


import java.util.List;

import net.nilsghesquiere.entities.ImportSettings;
import net.nilsghesquiere.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportSettingsRepository extends JpaRepository<ImportSettings, Long> {
	ImportSettings getById(@Param("id") Long id);
	List<ImportSettings> findByUser(User user);
	List<ImportSettings> findByUserId(@Param("userid")Long userid);
	void deleteById(Long id);
	ImportSettings findByUserIdAndName(@Param("userid")Long userid, @Param("name")String name);
}
