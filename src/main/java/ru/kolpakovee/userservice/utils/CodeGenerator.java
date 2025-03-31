package ru.kolpakovee.userservice.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class CodeGenerator {

    public Long generate() {
        return ThreadLocalRandom.current().nextLong(1, 1000000);
    }
}
