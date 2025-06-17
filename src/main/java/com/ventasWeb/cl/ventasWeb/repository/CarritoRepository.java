package com.ventasWeb.cl.ventasWeb.repository;

import com.ventasWeb.cl.ventasWeb.model.Carrito;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long>{
}