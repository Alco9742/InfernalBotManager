package net.nilsghesquiere.persistence.dao;


import net.nilsghesquiere.entities.Queuer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueuerRepository extends JpaRepository<Queuer, Long> {

}
