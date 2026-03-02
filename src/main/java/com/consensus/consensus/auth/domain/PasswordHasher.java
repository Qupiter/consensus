package com.consensus.consensus.auth.domain;

public interface PasswordHasher {
    String hash(String raw);
    boolean verify(String raw, String hashed);
}