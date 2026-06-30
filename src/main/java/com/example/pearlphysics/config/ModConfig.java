package com.example.pearlphysics.config;

public class ModConfig {
    public static boolean debugMode = true;
    public static float rotationSpeed = 15.0f; // Degrees per tick
    public static int maxPredictionTicks = 100;
    public static double microSlideDistance = 0.2;

    // Physical Engine Constants for Minecraft 1.21.11
    public static final double PEARL_GRAVITY = 0.03;
    public static final double PEARL_DRAG = 0.99; // Velocity multiplication per tick
    public static final double WIND_CHARGE_SPEED = 1.5; // Blocks per tick constant
}