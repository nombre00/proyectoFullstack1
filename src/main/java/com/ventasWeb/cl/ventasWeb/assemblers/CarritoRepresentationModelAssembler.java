package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.CarritoControllerV2;
import com.ventasWeb.cl.ventasWeb.controller.ClienteControllerV2;
import com.ventasWeb.cl.ventasWeb.controller.VentaWebControllerV2;
import com.ventasWeb.cl.ventasWeb.model.Carrito;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


public class CarritoRepresentationModelAssembler implements RepresentationModelAssembler<Carrito, EntityModel<Carrito>>{

    @Override
    public EntityModel<Carrito> toModel(Carrito carrito) {

        // Cuando el carrito no tiene cliente.
        if (carrito.getCliente() == null){
            return EntityModel.of(carrito,
            // Linkeamos a si mismo.
            linkTo(methodOn(CarritoControllerV2.class).buscarTodos()).withSelfRel());
        }

        // Cuando el carrito tiene cliente.
        return EntityModel.of(carrito,
        // Linkeamos a si mismo y al dueño del carrito.
        linkTo(methodOn(CarritoControllerV2.class).buscarPorRun(carrito.getCliente().getRun_cliente())).withSelfRel(),
        linkTo(methodOn(ClienteControllerV2.class).
        buscarPorRun(carrito.getCliente().getRun_cliente())).withRel("Cliente."),
        linkTo(methodOn(VentaWebControllerV2.class).
        valorCarrito(carrito.getCliente().getRun_cliente())).withRel("Revisar costo del carrito.")
        );
    }
}
