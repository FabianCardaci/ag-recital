package com.utn.ia.recital.service;

import com.utn.ia.recital.pojo.DayTO;
import com.utn.ia.recital.pojo.Genre;
import com.utn.ia.recital.pojo.Language;
import io.jenetics.util.ISeq;
import io.jenetics.util.Seq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.utn.ia.recital.pojo.Category.LEGEND;
import static java.util.Arrays.stream;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.apache.commons.lang3.math.NumberUtils.DOUBLE_ZERO;

@Service
public class FitnessService {

    @Value("${budget.max}")
    private Integer maximumBudget;

    @Value("${penalty.budget}")
    private Double budgetPenalty;


    public Double fitness(final ISeq<DayTO> days) {
        if (thereIs7Days(days) && thereIs4Bands(days)) {
            return DOUBLE_ZERO;
        }
        return safeFitness(days);
    }


    private boolean thereIs7Days(final ISeq<DayTO> days) {
        return size(days) != 7;
    }

    private boolean thereIs4Bands(final ISeq<DayTO> days) {
        return days.stream().allMatch(day -> size(day.getBands()) == 4);
    }

    private Double safeFitness(final ISeq<DayTO> days) {
        double result = 0;

        result += fitnessForLegend(days);
        result += fitnessForSameGenre(days);
        result += fitnessForSameLanguage(days);
        result -= penaltyForBudget(days);

        return result;
    }

    private double fitnessForLegend(final ISeq<DayTO> days) {
        long daysWithLegend = days.stream().filter(day ->
                day.getBands().stream().anyMatch(band -> LEGEND.equals(band.getCategory()))
        ).count();
        return (double) (daysWithLegend * 15);
    }

    private double fitnessForSameGenre(final ISeq<DayTO> days) {
        long daysWithSameGenre = days.stream().filter(
                this::sameGenre
        ).count();
        return (double) (daysWithSameGenre * 30);
    }

    private boolean sameGenre(final DayTO day) {
        return stream(Genre.values()).anyMatch(genre ->
            day.getBands().stream().filter(band -> genre.equals(band.getGenre())).count() >= 3
        );
    }

    private double fitnessForSameLanguage(final ISeq<DayTO> days) {
        long daysWithSameLanguage = days.stream().filter(
                this::sameLanguage
        ).count();
        return (double) (daysWithSameLanguage * 30);
    }

    private boolean sameLanguage(final DayTO day) {
        return stream(Language.values()).anyMatch(genre ->
                day.getBands().stream().filter(band -> genre.equals(band.getCountry().getLanguage())).count() >= 4
        );
    }

    private Double penaltyForBudget(final ISeq<DayTO> days) {
        int budget = days.stream().map(DayTO::getBands).flatMap(Seq::stream).mapToInt(it -> it.getPrice().getValue()).sum();
        return (budget < maximumBudget) ? DOUBLE_ZERO : budgetPenalty;
    }

}