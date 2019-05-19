package com.utn.ia.recital.service;


import com.utn.ia.recital.pojo.AgResTO;
import com.utn.ia.recital.pojo.DayResTO;
import com.utn.ia.recital.pojo.DayTO;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FestivalService {

    @Autowired
    private FestivalProblem problem;

    @Value("${population.size}")
    private Integer populationSize;

    @Value("${mutation.probability}")
    private Double mutationProbability;

    @Value("${crossover.probability}")
    private Double crossoverProbability;

    @Value("${bad-generations.max}")
    private Integer badGenerationsMax;


    public AgResTO runAg() {


        final Engine<EnumGene<DayTO>, Double> engine = Engine.builder(problem)
                .maximizing()
                .populationSize(populationSize)
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(mutationProbability),
                        new SinglePointCrossover<>(crossoverProbability))
                .build();


        EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

        Phenotype<EnumGene<DayTO>,Double> best =
                engine.stream()
                        .limit(bySteadyFitness(badGenerationsMax))
                        .peek(statistics)
                        .collect(toBestPhenotype());

        log.info("Estadísticas: " + statistics);
        log.info("Aptitud: " + best.getFitness());
        log.info("Solución: " + best.getGenotype().toString());


        List<DayTO> chromosome = best.getGenotype().getChromosome().stream().map(EnumGene::getAllele).collect(toList());

        List<DayResTO> dayResponses = chromosome.stream().map(day -> {

            DayResTO dayResponse = new DayResTO();
            dayResponse.setBands(day.getBands().stream().collect(toList()));
            return dayResponse;

        }).collect(toList());

        return new AgResTO(best.getFitness(), dayResponses);
    }

}