package com.utn.ia.recital.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgResTO {

    private Double fitness;

    private List<DayResTO> chromosome;

}