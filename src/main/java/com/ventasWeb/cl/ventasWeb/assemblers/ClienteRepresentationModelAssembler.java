package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.ClienteControllerV2;
import com.ventasWeb.cl.ventasWeb.model.Cliente;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class ClienteRepresentationModelAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>>{

    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
        linkTo(methodOn(ClienteControllerV2.class).buscarPorRun(cliente.getRun_cliente())).withSelfRel()
        );
    }
}
