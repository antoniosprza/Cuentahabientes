package com.springboot.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.api.model.Cuentahabientes;

@Repository
public interface CuentahabientesRepository extends JpaRepository<Cuentahabientes, Long>{
	
	

}
