package com.example.pearlphysics.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

public class VectorMathUtil {

    /**
     * Converts a 3D target direction into pitch and yaw rotation coordinates.
     */
    public static Vector2f getRotations(Vec3 origin, Vec3 target) {
        double diffX = target.x - origin.x;
        double diffY = target.y - origin.y;
        double diffZ = target.z - origin.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new Vector2f(pitch, yaw);
    }

    /**
     * Wraps angles smoothly to avoid instant snapping behavior across boundaries.
     */
    public static float clampAngle(float current, float target, float speed) {
        float delta = Math.floorMod((int)(target - current + 180), 360) - 180;
        if (delta > speed) return current + speed;
        if (delta < -speed) return current - speed;
        return target;
    }
}