package com.ventasWeb.cl.ventasWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ventasWeb.cl.ventasWeb.model.Trabajador;


@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long>{

}
