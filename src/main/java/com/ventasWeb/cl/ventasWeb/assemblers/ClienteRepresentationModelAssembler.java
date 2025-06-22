package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.CarritoControllerV2;
import com.ventasWeb.cl.ventasWeb.controller.ClienteControllerV2;
import com.ventasWeb.cl.ventasWeb.controller.ProductoControllerV2;
import com.ventasWeb.cl.ventasWeb.model.Cliente;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class ClienteRepresentationModelAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>>{

    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
        // Linkeamos a si mismo, a buscar todos los productos y a comprar.
        linkTo(methodOn(ClienteControllerV2.class).buscarPorRun(cliente.getRun_cliente())).withSelfRel(),
        linkTo(methodOn(ProductoControllerV2.class).buscarTodos()).withRel("Productos diponibles."),
        linkTo(methodOn(CarritoControllerV2.class).
        agregarProductoCarrito(0, cliente.getRun_cliente(), 0)).withRel("Comprar.")
        );
    }
}
