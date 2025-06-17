package com.ventasWeb.cl.ventasWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ventasWeb.cl.ventasWeb.model.Especialista_almacen;


@Repository
public interface Especialista_almacenRepository extends JpaRepository<Especialista_almacen, Long>{

}
