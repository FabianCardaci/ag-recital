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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Function;
import java.util.stream.IntStream;


@Service
public class FestivalProblem implements Problem<ISeq<DayTO>, EnumGene<DayTO>, Double> {

    private static final EasyRandom RANDOM = new EasyRandom();

    @Autowired
    private FitnessService fitnessService;

    private ISeq<DayTO> _days;


    @PostConstruct
    public void init() {
        _days = initialPopulation();
    }

    @Override
    public Codec<ISeq<DayTO>, EnumGene<DayTO>> codec() {
        return Codecs.ofPermutation(_days);
    }

    @Override
    public Function<ISeq<DayTO>, Double> fitness() {
        return days -> fitnessService.fitness(days);
    }

    private ISeq<DayTO> initialPopulation() {
        return IntStream.iterate(0, i -> i+1)
                .limit(7)
                .mapToObj(i -> new DayTO(randomBands()))
                .collect(ISeq.toISeq());
    }

    private ISeq<BandTO> randomBands() {
        return IntStream.iterate(0, i -> i+1)
                .limit(4)
                .mapToObj(i -> RANDOM.nextObject(BandTO.class))
                .collect(ISeq.toISeq());
    }

}
