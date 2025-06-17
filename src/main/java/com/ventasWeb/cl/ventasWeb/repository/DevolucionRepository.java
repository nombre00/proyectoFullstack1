package com.ventasWeb.cl.ventasWeb.repository;

import com.ventasWeb.cl.ventasWeb.model.Devolucion;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Long>{

}