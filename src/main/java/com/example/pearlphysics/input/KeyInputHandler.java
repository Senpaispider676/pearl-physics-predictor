package com.example.pearlphysics.input;

import com.example.pearlphysics.config.ModConfig;
import com.example.pearlphysics.core.InterceptSolver;
import com.example.pearlphysics.core.TrajectoryPredictor;
import com.example.pearlphysics.render.DebugHudOverlay;
import com.example.pearlphysics.render.TrajectoryRenderer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.text.Text;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyInputHandler {
    private static KeyBinding actionKeybind;
    private static EnderPearlEntity monitoredPearl = null;

    public static void register() {
        actionKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pearlphysics.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.pearlphysics"
        ));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> monitoredPearl = null);
    }

    public static void handleClientTick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;

        // Listen for standard initial action activation criteria triggers
        if (actionKeybind.wasPressed()) {
            // Locate an active pearl in the surrounding entity tracker ecosystem context
            EnderPearlEntity found = client.world.getEntitiesByClass(EnderPearlEntity.class,
                            client.player.getBoundingBox().expand(12.0), entity -> true)
                    .stream().findFirst().orElse(null);

            if (found != null) {
                monitoredPearl = found;
                client.player.sendMessage(Text.literal("§aTarget Entity Acquired. Executing Math Track Trace."), true);
            } else {
                client.player.sendMessage(Text.literal("§cNo active Pearl entities found nearby."), true);
            }
        }

        // Process analytical tracking operations continuously across valid frames
        if (monitoredPearl != null && monitoredPearl.isAlive()) {
            Vec3 currentPos = monitoredPearl.getPos();
            Vec3 currentVelocity = monitoredPearl.getVelocity();

            // Extrapolate aerodynamic physical update loops step paths
            List<TrajectoryPredictor.PredictionStep> predictionPath =
                    TrajectoryPredictor.extrapolatePearl(client.world, currentPos, currentVelocity);

            Vec3 eyePos = client.player.getEyePos();
            InterceptSolver.InterceptResult solution = InterceptSolver.solve(eyePos, predictionPath);

            if (solution != null) {
                TrajectoryRenderer.updateData(predictionPath, solution.interceptPoint);
                DebugHudOverlay.updateMetrics("COMPUTING STABLE VECTOR", currentPos, solution.targetTick);
            } else {
                TrajectoryRenderer.updateData(predictionPath, null);
                DebugHudOverlay.updateMetrics("NO VALID INTERCEPT AREA", currentPos, 0);
            }
        } else if (monitoredPearl != null) {
            monitoredPearl = null;
            DebugHudOverlay.updateMetrics("TARGET SEPARATED / IMPACTED", Vec3.ZERO, 0);
        }
    }
}