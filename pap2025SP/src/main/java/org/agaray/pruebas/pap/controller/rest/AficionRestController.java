package org.agaray.pruebas.pap.controller.rest;

import java.util.List;

import org.agaray.pruebas.pap.entities.Aficion;
import org.agaray.pruebas.pap.services.AficionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aficion")
public class AficionRestController {
    
    @Autowired
    private AficionService aficionService;


    @RequestMapping("r")
    public List<Aficion> r() {
        return aficionService.r();
    }
}
