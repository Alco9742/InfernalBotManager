package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.Metric;

public interface MetricService {
	Metric read(Long id);
	Metric create(Metric metric);
	Metric update(Metric metric);
	void deleteById(Long id);
	Metric findByName(String name);
	List<Metric> findAll();
	void delete(Metric metric);
}
