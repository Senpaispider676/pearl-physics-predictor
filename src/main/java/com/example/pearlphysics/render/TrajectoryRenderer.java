package com.example.pearlphysics.render;

import com.example.pearlphysics.config.ModConfig;
import com.example.pearlphysics.core.TrajectoryPredictor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;

public class TrajectoryRenderer {

    private static List<TrajectoryPredictor.PredictionStep> activeTrail = null;
    private static Vec3 targetIntercept = null;

    public static void updateData(List<TrajectoryPredictor.PredictionStep> trail, Vec3 intercept) {
        activeTrail = trail;
        targetIntercept = intercept;
    }

    public static void render(WorldRenderContext context) {
        if (!ModConfig.debugMode || activeTrail == null || activeTrail.isEmpty()) return;

        Vec3 camPos = context.camera().getPos();
        MatrixStack matrices = context.matrixStack();
        if (matrices == null) return;

        matrices.push();
        // Shift context reference framework relative to the active camera vector
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // 1. Draw Simulated Trajectory Line String
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        for (TrajectoryPredictor.PredictionStep step : activeTrail) {
            buffer.vertex(matrix, (float) step.position.x, (float) step.position.y, (float) step.position.z)
                    .color(0.2f, 0.8f, 1.0f, 0.8f).next();
        }
        tessellator.draw();

        // 2. Draw Cross Intercept Intersect Indicator Box
        if (targetIntercept != null) {
            buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            float s = 0.15f; // Box half-size dimensions
            float x = (float) targetIntercept.x, y = (float) targetIntercept.y, z = (float) targetIntercept.z;

            // Drawing simple bounding reference framework lines
            buffer.vertex(matrix, x - s, y, z).color(1f, 0.2f, 0.2f, 1f).next();
            buffer.vertex(matrix, x + s, y, z).color(1f, 0.2f, 0.2f, 1f).next();
            buffer.vertex(matrix, x, y - s, z).color(1f, 0.2f, 0.2f, 1f).next();
            buffer.vertex(matrix, x, y + s, z).color(1f, 0.2f, 0.2f, 1f).next();
            buffer.vertex(matrix, x, y, z - s).color(1f, 0.2f, 0.2f, 1f).next();
            buffer.vertex(matrix, x, y, z + s).color(1f, 0.2f, 0.2f, 1f).next();
            tessellator.draw();
        }

        matrices.pop();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}