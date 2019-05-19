package com.utn.ia.recital.pojo;

import lombok.Getter;

import static com.utn.ia.recital.pojo.Language.ENGLISH;
import static com.utn.ia.recital.pojo.Language.SPANISH;

@Getter
public enum Country {

    ARG(SPANISH),

    USA(ENGLISH),

    ENG(ENGLISH),

    URU(SPANISH);

    private Language language;

    Country(final Language language) {
        this.language = language;
    }

}
