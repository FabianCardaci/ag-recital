package com.utn.ia.recital.service;

import com.utn.ia.recital.pojo.DayTO;
import com.utn.ia.recital.pojo.Genre;
import com.utn.ia.recital.pojo.Language;
import io.jenetics.util.ISeq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.utn.ia.recital.pojo.Category.LEGEND;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.apache.commons.lang3.math.NumberUtils.DOUBLE_ZERO;

@Service
public class FitnessService {

    @Value("${festival.days}")
    private Integer festivalDays;

    @Value("${day.bands}")
    private Integer dayBands;

    @Value("${budget.max}")
    private Integer maximumBudget;

    @Value("${penalty.budget}")
    private Double budgetPenalty;

    @Value("${fitness.language}")
    private Double languageFitness;

    @Value("${fitness.legend}")
    private Double legendFitness;

    @Value("${fitness.genre}")
    private Double genreFitness;


    /**
     * Check individual and calculate fitness
     */
    public Double fitness(final ISeq<DayTO> days) {
        checkArgument(requiredDays(days), format("Require %s days", festivalDays));
        checkArgument(requiredBands(days), format("Require %s bands by day", dayBands));

        return safeFitness(days);
    }

    private boolean requiredDays(final ISeq<DayTO> days) {
        return size(days) == festivalDays;
    }

    private boolean requiredBands(final ISeq<DayTO> days) {
        return days.stream().allMatch(day -> size(day.getBands()) == dayBands);
    }



    /**
     * Calculate fitness
     */
    private Double safeFitness(final ISeq<DayTO> days) {
        double result = 0;

        result += fitnessForLegend(days);
        result += fitnessForSameGenre(days);
        result += fitnessForSameLanguage(days);
        result -= penaltyForBudget(days);

        return result;
    }

    /**
     * Reward by each day with a legend band
     */
    private double fitnessForLegend(final ISeq<DayTO> days) {
        long daysWithLegend = days.stream().filter(day ->
                day.getBands().stream().anyMatch(band -> LEGEND.equals(band.getCategory()))
        ).count();
        return (daysWithLegend * legendFitness);
    }

    /**
     * Reward by each day with 3 bands of same genre
     */
    private double fitnessForSameGenre(final ISeq<DayTO> days) {
        long daysWithSameGenre = days.stream().filter(
                this::sameGenre
        ).count();
        return (daysWithSameGenre * genreFitness);
    }

    private boolean sameGenre(final DayTO day) {
        return stream(Genre.values()).anyMatch(genre ->
            day.getBands().stream().filter(band -> genre.equals(band.getGenre())).count() >= 3
        );
    }

    /**
     * Reward by each day with all bands in same language
     */
    private double fitnessForSameLanguage(final ISeq<DayTO> days) {
        long daysWithSameLanguage = days.stream().filter(
                this::sameLanguage
        ).count();
        return (daysWithSameLanguage * languageFitness);
    }

    private boolean sameLanguage(final DayTO day) {
        return stream(Language.values()).anyMatch(genre ->
                day.getBands().stream().filter(band -> genre.equals(band.getCountry().getLanguage())).count() == dayBands
        );
    }

    /**
     * Penalize if festival overcome the maximum budget
     */
    private Double penaltyForBudget(final ISeq<DayTO> days) {
        int budget = days.stream().map(DayTO::getBands).flatMap(Collection::stream).mapToInt(it -> it.getPrice().getValue()).sum();
        return (budget < maximumBudget) ? DOUBLE_ZERO : budgetPenalty;
    }

}