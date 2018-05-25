package net.nilsghesquiere.persistence.dao;


import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
	UserSettings getByUser(User user);
	UserSettings getByUserId(@Param("userid")Long userid);
	void deleteById(Long id);;
}
