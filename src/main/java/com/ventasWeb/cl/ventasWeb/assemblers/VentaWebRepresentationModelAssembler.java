package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.VentaWebControllerV2;
import com.ventasWeb.cl.ventasWeb.model.VentaWeb;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class VentaWebRepresentationModelAssembler implements RepresentationModelAssembler<VentaWeb, EntityModel<VentaWeb>> {

    @Override
    public EntityModel<VentaWeb> toModel(VentaWeb venta) {
        return EntityModel.of(venta,
        linkTo(methodOn(VentaWebControllerV2.class).BuscaPorId(venta.getId_ventaWeb())).withSelfRel()
        );
    }
}
