package com.ventasWeb.cl.ventasWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ventasWeb.cl.ventasWeb.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>{

}
