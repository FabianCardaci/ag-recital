package com.utn.ia.recital.pojo;

import lombok.Getter;

@Getter
public enum Price {

    MILLION_1(1),
    MILLION_2(2),
    MILLION_3(3),
    MILLION_4(4),
    MILLION_5(5),
    MILLION_6(6),
    MILLION_7(7),
    MILLION_8(8);

    private Integer value;

    Price(final Integer value) {
        this.value = value;
    }

}
