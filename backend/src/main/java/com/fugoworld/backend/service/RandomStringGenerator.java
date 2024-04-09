/*
 * Decompiled with CFR 0.152.
 */
package com.fugoworld.backend.service;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 20;
    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; ++i) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
