package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.CarritoControllerV2;
import com.ventasWeb.cl.ventasWeb.controller.ProductoControllerV2;
import com.ventasWeb.cl.ventasWeb.model.Producto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class ProductoRepresentationModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>>{

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
        // Linkeamos el producto a si mismo y la acción para comprar.
            linkTo(methodOn(ProductoControllerV2.class).buscarPorRun(producto.getId_producto())).withSelfRel(),
            linkTo(methodOn(CarritoControllerV2.class).
            agregarProductoCarrito(producto.getId_producto(), 0, 0)).withRel("Comprar:")
        );
    }
} 
