package net.nilsghesquiere.persistence.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.nilsghesquiere.entities.Metric;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
	Metric findById(@Param("id") Long id);
	Metric findByName(@Param("name")String name);
	void deleteById(Long id);
}
