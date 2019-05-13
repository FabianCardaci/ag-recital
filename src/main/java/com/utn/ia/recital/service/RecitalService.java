package com.utn.ia.recital.service;


import com.utn.ia.recital.pojo.AgResTO;
import com.utn.ia.recital.pojo.RecitalTO;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class RecitalService {

    public AgResTO getFitness() {

        RecitalProblem problem = new RecitalProblem();

        Engine<EnumGene<RecitalTO>, Integer> engine = Engine
                .builder(problem)
                .optimize(Optimize.MINIMUM)
                .populationSize(20)
                .alterers(
                        new SwapMutator<>(0.2),
                        new PartiallyMatchedCrossover<>(0.35))
                .build();

        EvolutionStatistics<Integer, ?>
                statistics = EvolutionStatistics.ofNumber();

        Phenotype<EnumGene<RecitalTO>,Integer> best =
                engine. stream ( )
                        //.limit(Limits.bySteadyFitness(80))
                        .limit(40000)
                        .peek(statistics)
                        .collect(EvolutionResult.toBestPhenotype());

        log.info("Estadísticas:"+statistics);
        log.info("Minimo: " + best.getFitness());
        log.info("Orden: "+ best.getGenotype().toString());


        return new AgResTO(best.getFitness());
    }

}