package com.utn.ia.recital.service;


import com.utn.ia.recital.pojo.AgResTO;
import com.utn.ia.recital.pojo.DayTO;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FestivalService {

    @Autowired
    private FestivalProblem problem;

    public AgResTO runAg() {


        final Engine<EnumGene<DayTO>, Double> engine = Engine.builder(problem)
                .maximizing()
                .populationSize(500)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(0.115),
                        new SinglePointCrossover<>(0.16))
                .build();


        EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

        Phenotype<EnumGene<DayTO>,Double> best =
                engine.stream()
                        .limit(40000)
                        .peek(statistics)
                        .collect(EvolutionResult.toBestPhenotype());

        log.info("Estadísticas:"+statistics);
        log.info("Minimo: " + best.getFitness());
        log.info("Orden: "+ best.getGenotype().toString());


        List<DayTO> chromosome = best.getGenotype().getChromosome().stream().map(EnumGene::getAllele).collect(toList());

        return new AgResTO(best.getFitness(), chromosome);
    }

}