package org.agaray.pruebas.pap.helper;

import org.agaray.pruebas.pap.entities.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionManager {

    @Autowired
    private HttpServletRequest request;

    public Persona getUsername() {
        HttpSession session = request.getSession();
        Object o = session.getAttribute("user");
        return ( o!=null ? (Persona)o : null );
    }
}
