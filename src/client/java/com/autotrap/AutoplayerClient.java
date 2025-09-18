package com.autotrap;

import com.example.autoplayer.AutoPlayerMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Fabric client entrypoint for the AutoPlayer automation system.
 */
public final class AutoplayerClient implements ClientModInitializer {

    private AutoPlayerMod autoPlayerMod;
    private KeyBinding toggleAutomationKey;

    @Override
    public void onInitializeClient() {
        autoPlayerMod = new AutoPlayerMod();
        autoPlayerMod.initialize();
        registerKeybindings();
        registerEvents();
    }

    private void registerKeybindings() {
        toggleAutomationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autoplayer.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.autoplayer.controls"));
    }

    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (autoPlayerMod.isInitialized()) {
                autoPlayerMod.renderHud();
            }
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            if (autoPlayerMod.isInitialized()) {
                autoPlayerMod.saveState();
            }
        });
    }

    private void onClientTick(MinecraftClient client) {
        if (!autoPlayerMod.isInitialized()) {
            return;
        }
        if (toggleAutomationKey.wasPressed()) {
            autoPlayerMod.toggleAutomation();
        }
        autoPlayerMod.tick();
    }
}
