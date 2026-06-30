package com.example.pearlphysics.render;

import com.example.pearlphysics.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.phys.Vec3;

public class DebugHudOverlay {
    private static String statusText = "SYSTEM IDLE";
    private static Vec3 activePearlPos = Vec3.ZERO;
    private static int calculatedTick = 0;

    public static void updateMetrics(String status, Vec3 pearlPos, int tick) {
        statusText = status;
        activePearlPos = pearlPos;
        calculatedTick = tick;
    }

    public static void draw(DrawContext context, float tickDelta) {
        if (!ModConfig.debugMode) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) return;

        int y = 10;
        context.drawTextWithShadow(client.textRenderer, "=== PHYSICS METRICS ===", 10, y, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, "Status: " + statusText, 10, y += 10, 0x00FF00);
        context.drawTextWithShadow(client.textRenderer, String.format("Pearl Pos: %.2f, %.2f, %.2f",
                activePearlPos.x, activePearlPos.y, activePearlPos.z), 10, y += 10, 0xAAAAAA);
        context.drawTextWithShadow(client.textRenderer, "Intercept Target Tick: " + calculatedTick, 10, y + 10, 0x55FFFF);
    }
}