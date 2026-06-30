package com.example.pearlphysics;

import com.example.pearlphysics.input.KeyInputHandler;
import com.example.pearlphysics.render.DebugHudOverlay;
import com.example.pearlphysics.render.TrajectoryRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class PearlPhysicsMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Core handler setup routines
        KeyInputHandler.register();

        // Setup ticking phase event hook registrations
        ClientTickEvents.END_CLIENT_TICK.register(KeyInputHandler::handleClientTick);

        // Map rendering events directly to underlying visualization managers
        WorldRenderEvents.LAST.register(TrajectoryRenderer::render); //
        HudRenderCallback.EVENT.register(DebugHudOverlay::draw);
    }
}