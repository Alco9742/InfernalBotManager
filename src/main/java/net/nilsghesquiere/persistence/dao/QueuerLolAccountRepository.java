package net.nilsghesquiere.persistence.dao;


import net.nilsghesquiere.entities.QueuerLolAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueuerLolAccountRepository extends JpaRepository<QueuerLolAccount, Long> {

}
