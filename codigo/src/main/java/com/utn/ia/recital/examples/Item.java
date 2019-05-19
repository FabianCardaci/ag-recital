package com.utn.ia.recital.examples;

import io.jenetics.internal.util.require;

import java.io.Serializable;
import java.util.Random;
import java.util.stream.Collector;

/**
 * This class represents a knapsack item with the specific <i>size</i> and
 * <i>value</i>.
 */
public final class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double _size;
    private final double _value;

    private Item(final double size, final double value) {
        _size = require.nonNegative(size);
        _value = require.nonNegative(value);
    }

    /**
     * Return the item size.
     *
     * @return the item size
     */
    public double getSize() {
        return _size;
    }

    /**
     * Return the item value.
     *
     * @return the item value
     */
    public double getValue() {
        return _value;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        long bits = Double.doubleToLongBits(_size);
        hash = 31 * hash + (int) (bits ^ (bits >>> 32));

        bits = Double.doubleToLongBits(_value);
        return 31 * hash + (int) (bits ^ (bits >>> 32));
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Item &&
                Double.compare(_size, ((Item) obj)._size) == 0 &&
                Double.compare(_value, ((Item) obj)._value) == 0;
    }

    @Override
    public String toString() {
        return String.format("Item[size=%f, value=%f]", _size, _value);
    }

    /**
     * Return a new knapsack {@code Item} with the given {@code size} and
     * {@code value}.
     *
     * @param size  the item size
     * @param value the item value
     * @return a new knapsack {@code Item}
     * @throws IllegalArgumentException if one of the parameters is smaller
     *                                  then zero
     */
    public static Item of(final double size, final double value) {
        return new Item(size, value);
    }

    /**
     * Create a new <i>random</i> knapsack item for testing purpose.
     *
     * @param random the random engine used for creating the knapsack item
     * @return a new <i>random</i> knapsack item
     * @throws NullPointerException if the random engine is {@code null}
     */
    public static Item random(final Random random) {
        return new Item(random.nextDouble() * 100, random.nextDouble() * 100);
    }

    /**
     * Return a {@link Collector}, which sums the size and value of knapsack
     * items.
     *
     * @return a knapsack item sum {@link Collector}
     */
    public static Collector<Item, ?, Item> toSum() {
        return Collector.of(
                () -> new double[2],
                (a, b) -> {
                    a[0] += b._size;
                    a[1] += b._value;
                },
                (a, b) -> {
                    a[0] += b[0];
                    a[1] += b[1];
                    return a;
                },
                r -> new Item(r[0], r[1])
        );
    }
}
