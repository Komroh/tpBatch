package com.example.tpbatch.utils;

import java.nio.charset.StandardCharsets;
import net.openhft.hashing.LongHashFunction;

public class HashCalcul {

    public static long calculHash(Hashable hashable) {

        return LongHashFunction.xx3().hashBytes(hashable.HashContent().getBytes(StandardCharsets.UTF_8));
    }

    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
