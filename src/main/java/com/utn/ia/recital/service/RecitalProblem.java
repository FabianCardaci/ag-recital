package com.utn.ia.recital.service;

import com.utn.ia.recital.pojo.RecitalTO;
import io.jenetics.EnumGene;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Problem;
import io.jenetics.util.ISeq;

import java.util.function.Function;

public class RecitalProblem implements Problem<RecitalTO, EnumGene<RecitalTO>, Integer> {

    private ISeq<RecitalTO> recital;

    @Override
    public Codec<RecitalTO, EnumGene<RecitalTO>> codec() {
        return null;
    }

    @Override
    public Function<RecitalTO, Integer> fitness() {
        return recital -> recital.getFitness();
    }

}
