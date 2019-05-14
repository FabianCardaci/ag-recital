package com.utn.ia.recital.service;


import com.utn.ia.recital.pojo.AgResTO;
import com.utn.ia.recital.pojo.BandTO;
import com.utn.ia.recital.pojo.DayTO;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.ISeq;
import io.jenetics.util.Seq;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FestivalService {

    private static final EasyRandom RANDOM = new EasyRandom();



    public AgResTO runAg() {

        FestivalProblem problem = new FestivalProblem(initialPopulation());


        final Engine<BitGene, Double> engine = Engine.builder(problem)
                .populationSize(500)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(0.115),
                        new SinglePointCrossover<>(0.16))
                .build();


        EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

        Phenotype<BitGene,Double> best =
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

    private ISeq<DayTO> initialPopulation() {
        return IntStream.iterate(0, i -> i+1)
                .limit(7)
                .mapToObj(i -> new DayTO(randomBands()))
                .collect(ISeq.toISeq());
    }

    private List<BandTO> randomBands() {
        return IntStream.iterate(0, i -> i+1)
                .limit(4)
                .mapToObj(i -> RANDOM.nextObject(BandTO.class))
                .collect(toList());
    }

}