package com.utn.ia.recital.examples;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.internal.util.require;
import io.jenetics.util.ISeq;

import java.io.Serializable;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;
import static java.util.Objects.requireNonNull;

public final class Knapsack implements Problem<ISeq<Item>, BitGene, Double> {


    private final Codec<ISeq<Item>, BitGene> _codec;
    private final double _knapsackSize;

    /**
     * Create a new {@code Knapsack} definition with the given
     *
     * @param items the basic {@link java.util.Set} of knapsack items.
     * @param knapsackSize the maximal knapsack size
     * @throws NullPointerException if the {@code items} set is {@code null}
     */
    public Knapsack(final ISeq<Item> items, final double knapsackSize) {
        _codec = Codecs.ofSubSet(items);
        _knapsackSize = knapsackSize;
    }

    @Override
    public Function<ISeq<Item>, Double> fitness() {
        return items -> {
            final Item sum = items.stream().collect(Item.toSum());
            return sum.getSize() <= _knapsackSize ? sum.getValue() : 0;
        };
    }

    @Override
    public Codec<ISeq<Item>, BitGene> codec() {
        return _codec;
    }

    /**
     * Factory method for creating <i>same</i> Knapsack problems for testing
     * purpose.
     *
     * @param itemCount the number of knapsack items in the basic set
     * @param random the random engine used for creating random knapsack items.
     *        This allows to create reproducible item sets and reproducible
     *        {@code Knapsack} problems, respectively.
     * @return a {@code Knapsack} problem object (for testing purpose).
     */
    public static Knapsack of(final int itemCount, final Random random) {
        requireNonNull(random);

        return new Knapsack(
                Stream.generate(() -> Item.random(random))
                        .limit(itemCount)
                        .collect(ISeq.toISeq()),
                itemCount*100.0/3.0
        );
    }

    public static void main(final String[] args) {
        final Knapsack knapsack = Knapsack.of(15, new Random(123));

        // Configure and build the evolution engine.
        final Engine<BitGene, Double> engine = Engine.builder(knapsack)
                .populationSize(500)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(0.115),
                        new SinglePointCrossover<>(0.16))
                .build();

        // Create evolution statistics consumer.
        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        final Phenotype<BitGene, Double> best = engine.stream()
                // Truncate the evolution stream after 7 "steady"
                // generations.
                .limit(bySteadyFitness(7))
                // The evolution will stop after maximal 100
                // generations.
                .limit(100)
                // Update the evaluation statistics after
                // each generation
                .peek(statistics)
                // Collect (reduce) the evolution stream to
                // its best phenotype.
                .collect(toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
    }

}