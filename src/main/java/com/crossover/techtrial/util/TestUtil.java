package com.crossover.techtrial.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;

public class TestUtil {
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
