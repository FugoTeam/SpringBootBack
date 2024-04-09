/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.jsonwebtoken.Jwts
 */
package com.fugoworld.backend.service;

import com.fugoworld.backend.model.User;
import io.jsonwebtoken.Jwts;

public class TokenService {
    public static String getToken(User user) {
        return Jwts.builder().claim("userId", (Object)user.getId()).claim("isAdmin", (Object)user.isAdmin()).compact();
    }
}
