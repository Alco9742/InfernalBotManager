package net.nilsghesquiere.persistence.dao;


import net.nilsghesquiere.entities.GlobalVariable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalVariableRepository extends JpaRepository<GlobalVariable, Long> {
	GlobalVariable getById(@Param("id") Long id);
	GlobalVariable getByName(@Param("name")String name);
	void deleteById(Long id);
}
