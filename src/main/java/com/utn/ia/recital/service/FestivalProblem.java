package com.utn.ia.recital.service;

import com.utn.ia.recital.pojo.BandTO;
import com.utn.ia.recital.pojo.DayTO;
import io.jenetics.EnumGene;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Problem;
import io.jenetics.util.ISeq;
import org.jeasy.random.EasyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Function;
import java.util.stream.IntStream;

import static io.jenetics.util.ISeq.toISeq;


@Service
public class FestivalProblem implements Problem<ISeq<DayTO>, EnumGene<DayTO>, Double> {

    private static final EasyRandom RANDOM = new EasyRandom();

    @Autowired
    private FitnessService fitnessService;

    @Value("${festival.days}")
    private Integer festivalDays;

    @Value("${day.bands}")
    private Integer dayBands;

    @Value("${universe.size}")
    private Integer universeSize;

    private ISeq<DayTO> universe;


    @PostConstruct
    public void init() {
        universe = createUniverse();
    }

    @Override
    public Codec<ISeq<DayTO>, EnumGene<DayTO>> codec() {
        return Codecs.ofSubSet(universe, festivalDays);
    }

    @Override
    public Function<ISeq<DayTO>, Double> fitness() {
        return days -> fitnessService.fitness(days);
    }

    private ISeq<DayTO> createUniverse() {
        return IntStream.iterate(0, i -> i+1)
                .limit(universeSize)
                .mapToObj(i -> new DayTO(randomBands()))
                .collect(toISeq());
    }

    private ISeq<BandTO> randomBands() {
        return IntStream.iterate(0, i -> i+1)
                .limit(dayBands)
                .mapToObj(i -> RANDOM.nextObject(BandTO.class))
                .collect(toISeq());
    }

}
