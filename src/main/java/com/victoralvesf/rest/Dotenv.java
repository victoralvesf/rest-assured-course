package com.victoralvesf.rest;

public class Dotenv {
    final private static io.github.cdimascio.dotenv.Dotenv dotenv;

    static {
        dotenv = io.github.cdimascio.dotenv.Dotenv.load();
    }

    public String get(String key) {
        return dotenv.get(key);
    }
}
