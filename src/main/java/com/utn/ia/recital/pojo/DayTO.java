package com.utn.ia.recital.pojo;

import io.jenetics.util.ISeq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayTO {

    private ISeq<BandTO> bands;

}