package org.agaray.pruebas.pap.helper;

import java.util.List;

import org.agaray.pruebas.pap.entities.Persona;
import org.agaray.pruebas.pap.entities.Rol;
import org.agaray.pruebas.pap.exception.DangerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class H {

    @Autowired
    private SessionManager sm;

    public void checkRol(List<String> nombresDeRol) throws DangerException {
        boolean contieneAlgunRol = false;
        Persona user = sm.getUsername();
        if (user == null) {
            throw new DangerException("Sólo usuarios autenticados pueden hacer eso");
        }

        for (String nombreDeRol : nombresDeRol) {
            contieneAlgunRol = (contieneAlgunRol || user.getRoles().contains(new Rol(nombreDeRol)));
        }

        if (!contieneAlgunRol) {
            throw new DangerException("El usuario no tiene permiso para hacer la tarea");
        }
    }
}
