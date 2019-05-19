package com.utn.ia.recital.service;


import com.utn.ia.recital.pojo.AgResTO;
import com.utn.ia.recital.pojo.DayTO;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
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

    private Phenotype<EnumGene<DayTO>,Double> bestOflastGeneration;
    
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
                        .peek(this::updateBestInLastRun)
                        .collect(toBestPhenotype());

        log.info("Estadísticas: " + statistics);
        log.info("Aptitud del mejor individuo de todas las generaciones: " + best.getFitness());
        log.info("Mejor individuo de todas las generaciones: " + best.getGenotype().toString());
        log.info("Aptitud del mejor individuo de la última generación: " + bestOflastGeneration.getFitness());
        log.info("Mejor individuo de la última generación: " + bestOflastGeneration.getGenotype().toString());

        List<DayTO> chromosome = best.getGenotype().getChromosome().stream().map(EnumGene::getAllele).collect(toList());

        return new AgResTO(best.getFitness(), chromosome);
    }
        
    private void updateBestInLastRun(final EvolutionResult<EnumGene<DayTO>, Double> result) {
        bestOflastGeneration = result.getBestPhenotype();
    }

}