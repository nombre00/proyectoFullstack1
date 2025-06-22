package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.CarritoControllerV2;
import com.ventasWeb.cl.ventasWeb.controller.ClienteControllerV2;
import com.ventasWeb.cl.ventasWeb.model.Carrito;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


public class CarritoRepresentationModelAssembler implements RepresentationModelAssembler<Carrito, EntityModel<Carrito>>{

    @Override
    public EntityModel<Carrito> toModel(Carrito carrito) {

        return EntityModel.of(carrito,
        linkTo(methodOn(CarritoControllerV2.class).buscarPorRun(carrito.getCliente().getRun_cliente())).withSelfRel(),
        linkTo(methodOn(ClienteControllerV2.class).buscarPorRun(carrito.getCliente().getRun_cliente())).withSelfRel()
        );
    }
}
