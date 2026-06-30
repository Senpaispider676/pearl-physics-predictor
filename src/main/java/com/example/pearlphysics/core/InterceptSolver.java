package com.example.pearlphysics.core;

import com.example.pearlphysics.config.ModConfig;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class InterceptSolver {

    public static class InterceptResult {
        public final Vec3 interceptPoint;
        public final int targetTick;
        public final double travelTime;

        public InterceptResult(Vec3 point, int tick, double travelTime) {
            this.interceptPoint = point;
            this.targetTick = tick;
            this.travelTime = travelTime;
        }
    }

    /**
     * Solves kinematics optimization by comparing projectile step lists
     * against linear tracking distances.
     */
    public static InterceptResult solve(Vec3 shooterEyePos, List<TrajectoryPredictor.PredictionStep> targetPath) {
        for (TrajectoryPredictor.PredictionStep step : targetPath) {
            double distance = shooterEyePos.distanceTo(step.position);
            // Wind charges maintain linear speed with zero drag/gravity decay constants
            double requiredTravelTicks = distance / ModConfig.WIND_CHARGE_SPEED;

            // Intercept evaluates true if travel requirements line up with step timing metrics
            if (requiredTravelTicks <= step.tick) {
                return new InterceptResult(step.position, step.tick, requiredTravelTicks);
            }
        }
        return null; // Evaluates null if no overlapping spatial vectors exist within bounds
    }
}