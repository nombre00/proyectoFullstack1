package com.ventasWeb.cl.ventasWeb.service;

import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.repository.ClienteRepository;

import java.util.List;

import jakarta.transaction.Transactional; //  Sirve para las consultas
import org.springframework.beans.factory.annotation.Autowired; // Para no nstanciar nuevos objetos si ya existen. 
import org.springframework.stereotype.Service; //  Las llamadas de los servicios ocupa esto.


@Service
@Transactional
public class ClienteService {

    // LLamamos al repositorio de la clase.
    @Autowired
    private ClienteRepository cr;

    // Métodos.

    // Buscar todos.
    public List<Cliente> buscarTodos(){
        return cr.findAll();
    }
    // Buscar por id.
    public Cliente buscarPorId(long id){
        return cr.findById(id).get();
    }
    // Guardar.
    public Cliente guardar(Cliente c){
        return cr.save(c);
    }
    // Borrar.
    public void borrar(long id){
        cr.deleteById(id);
    }

    // Iniciar sesión.
    public boolean ingresar(String nombre, String clave){
        // Creamos un flag y lo ponemos en falso.
        boolean flag = false;
        // Buscamos los clientes y los guardamos en una variable.
        List<Cliente> clientes = cr.findAll();
        // Iteramos los clientes.
        for (Cliente c : clientes){
            // Revisamos el id.
            if (nombre.equals(c.getNombre_completo())){
                // Comparamos la clave.
                if (clave.equals(c.getClave())){
                    // Si ambas coinciden cambiamos el flac a true;
                    flag = true;
                }
            }
        }
        return flag;
    }
}
