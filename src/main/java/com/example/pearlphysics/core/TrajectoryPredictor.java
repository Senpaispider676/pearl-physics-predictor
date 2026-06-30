package com.example.pearlphysics.core;

import com.example.pearlphysics.config.ModConfig;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryPredictor {

    public static class PredictionStep {
        public final Vec3 position;
        public final Vec3 velocity;
        public final int tick;

        public PredictionStep(Vec3 position, Vec3 velocity, int tick) {
            this.position = position;
            this.velocity = velocity;
            this.tick = tick;
        }
    }

    /**
     * Iteratively simulates aerodynamic tracking paths through 3D world space.
     */
    public static List<PredictionStep> extrapolatePearl(Level level, Vec3 startPos, Vec3 startVel) {
        List<PredictionStep> steps = new ArrayList<>();
        Vec3 currentPos = startPos;
        Vec3 currentVel = startVel;

        steps.add(new PredictionStep(currentPos, currentVel, 0));

        for (int t = 1; t <= ModConfig.maxPredictionTicks; t++) {
            // Apply Minecraft internal physics tick updates
            currentVel = currentVel.subtract(0, ModConfig.PEARL_GRAVITY, 0).scale(ModConfig.PEARL_DRAG);
            Vec3 nextPos = currentPos.add(currentVel);

            // Verify if a collision boundary blocks the projection step
            ClipContext context = new ClipContext(
                    currentPos, nextPos,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null
            );
            BlockHitResult hit = level.clip(context);
            if (hit.getType() != HitResult.Type.MISS) {
                steps.add(new PredictionStep(hit.getLocation(), currentVel, t));
                break; // Vector calculation terminates upon block collision impact
            }

            currentPos = nextPos;
            steps.add(new PredictionStep(currentPos, currentVel, t));
        }
        return steps;
    }
}