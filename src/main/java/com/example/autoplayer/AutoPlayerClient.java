package com.example.autoplayer;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fabric entrypoint that boots the automation mod when the Minecraft client starts.
 */
public final class AutoPlayerClient implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoPlayerClient.class);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing AutoPlayer automation client");
        new AutoPlayerMod().start();
    }
}
