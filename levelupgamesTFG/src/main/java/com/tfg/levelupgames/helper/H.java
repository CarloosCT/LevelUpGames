package com.tfg.levelupgames.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;

@Component
public class H {

    @Autowired
    private SessionManager sm;

    public void checkRol(List<String> nombresDeRol) throws DangerException {
        Usuario user = sm.getUsername();
        if (user == null) {
            throw new DangerException("Sólo usuarios autenticados pueden hacer eso");
        }

        if (!nombresDeRol.contains(user.getRol().getNombre())) {
            throw new DangerException("El usuario no tiene permiso para hacer la tarea");
        }
    }
}
