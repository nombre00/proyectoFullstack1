package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.ProductoControllerV2;
import com.ventasWeb.cl.ventasWeb.model.Producto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class ProductoRepresentationModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>>{

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoControllerV2.class).buscarPorRun(producto.getId_producto())).withSelfRel()
            // Abajo estaría el link para comprar.
            //linkTo(methodOn(VentaWebControllerV2.class).comprar(producto.getId_producto())).withRel("agregar-al-carrito")
        );
    }
}
