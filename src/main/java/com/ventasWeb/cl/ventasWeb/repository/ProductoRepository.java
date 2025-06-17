package com.ventasWeb.cl.ventasWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ventasWeb.cl.ventasWeb.model.Producto;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{

}
