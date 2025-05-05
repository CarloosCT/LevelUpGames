package com.tfg.levelupgames.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tfg.levelupgames.entities.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionManager {

    @Autowired
    private HttpServletRequest request;

    public Usuario getUsername() {
        HttpSession session = request.getSession();
        Object o = session.getAttribute("user");
        return ( o!=null ? (Usuario)o : null );
    }
}
