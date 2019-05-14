package com.utn.ia.recital.service;

import com.utn.ia.recital.examples.Item;
import com.utn.ia.recital.pojo.DayTO;
import io.jenetics.BitGene;
import io.jenetics.EnumGene;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Problem;
import io.jenetics.util.ISeq;

import java.util.function.Function;

public class FestivalProblem implements Problem<ISeq<DayTO>, BitGene, Double> {

    private final Codec<ISeq<DayTO>, BitGene> codec;

    public FestivalProblem(final ISeq<DayTO> days) {
        codec = Codecs.ofSubSet(days);
    }

    @Override
    public Codec<ISeq<DayTO>, BitGene> codec() {
        return codec;
    }

    @Override
    public Function<ISeq<DayTO>, Double> fitness() {
        return recital -> 1.0;
    }

}
