package com.utn.ia.recital.controller;

import com.utn.ia.recital.pojo.AgResTO;
import com.utn.ia.recital.service.FestivalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/ag", produces = APPLICATION_JSON_VALUE)
public class RecitalController {

    @Autowired
    private FestivalService service;

    @GetMapping("/run")
    public AgResTO run() {
        log.info("Request to run AG");
        final AgResTO response = service.runAg();
        log.info("Response to run AG {}", response);
        return response;
    }

}