package com.utn.ia.recital.service;

import com.utn.ia.recital.pojo.DayTO;
import io.jenetics.util.ISeq;
import org.springframework.stereotype.Service;

import static com.utn.ia.recital.pojo.Category.LEGEND;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.apache.commons.lang3.math.NumberUtils.DOUBLE_ZERO;

@Service
public class FitnessService {

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

        return result;
    }

    private double fitnessForLegend(final ISeq<DayTO> days) {
        long daysWithLegend = days.stream().filter(day ->
                day.getBands().stream().anyMatch(band -> LEGEND.equals(band.getCategory()))
        ).count();
        return (double) (daysWithLegend * 15);
    }

}