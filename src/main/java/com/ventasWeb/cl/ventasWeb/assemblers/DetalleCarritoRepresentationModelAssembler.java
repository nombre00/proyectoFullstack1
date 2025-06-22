package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.DetalleCarritoControllerV2;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;

public class DetalleCarritoRepresentationModelAssembler implements RepresentationModelAssembler<DetalleCarrito, EntityModel<DetalleCarrito>>{

    @Override
    public EntityModel<DetalleCarrito> toModel(DetalleCarrito detalle) {
        return EntityModel.of(detalle,
        // Linkeamos a si mismo.
        linkTo(methodOn(DetalleCarritoControllerV2.class).buscarPorId(detalle.getId_detalleCarrito())).withSelfRel()
        );
    }

}
