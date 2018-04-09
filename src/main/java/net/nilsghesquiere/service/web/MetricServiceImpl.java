package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import net.nilsghesquiere.entities.Metric;
import net.nilsghesquiere.persistence.dao.MetricRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

@Service
@Transactional
public class MetricServiceImpl implements MetricService{
	private final MetricRepository metricRepository;
	
	@Autowired
	public MetricServiceImpl(MetricRepository metricRepository){
		this.metricRepository = metricRepository;
	}
	
	public Metric read(Long id){
		return metricRepository.findOne(id);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Metric create(Metric metric) {
		return metricRepository.save(metric);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Metric update(Metric metric) {
		return metricRepository.save(metric);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteById(Long id) {
		metricRepository.deleteById(id);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Metric findByName(String name) {
		return metricRepository.findByName(name);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Metric> findAll() {
		return metricRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(Metric globalVariable) {
		metricRepository.delete(globalVariable);
	}
}
