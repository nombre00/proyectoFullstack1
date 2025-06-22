package com.ventasWeb.cl.ventasWeb.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.ventasWeb.cl.ventasWeb.controller.EnvioControllerV2;
import com.ventasWeb.cl.ventasWeb.model.Envio;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class EnvioRepresentationModelAssembler implements RepresentationModelAssembler<Envio, EntityModel<Envio>>{

    @Override
    public EntityModel<Envio> toModel(Envio envio) {
        return EntityModel.of(envio,
        linkTo(methodOn(EnvioControllerV2.class).buscarPorId(envio.getId_envio())).withSelfRel()
        );
    }
}
