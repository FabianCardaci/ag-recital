package com.utn.ia.recital.controller;

import com.google.common.collect.Lists;
import com.utn.ia.recital.service.RecitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/ag", produces = APPLICATION_JSON_VALUE)
public class RecitalController {

    @Autowired
    private RecitalService service;

    @GetMapping("/run")
    public List<String> run() {
        log.info("Request to run AG");
        final String response = service.getSomething();
        log.info("Response to run AG {}", response);
        return Lists.newArrayList(response);
    }

}