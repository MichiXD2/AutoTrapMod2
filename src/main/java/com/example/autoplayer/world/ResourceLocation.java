package com.example.autoplayer.world;

import com.example.autoplayer.ai.Vec3i;

/**
 * Represents a located block or resource in the world.
 */
public class ResourceLocation {
    private final String resourceId;
    private final Vec3i position;
    private final int richness;

    public ResourceLocation(String resourceId, Vec3i position, int richness) {
        this.resourceId = resourceId;
        this.position = position;
        this.richness = richness;
    }

    public String getResourceId() {
        return resourceId;
    }

    public Vec3i getPosition() {
        return position;
    }

    public int getRichness() {
        return richness;
    }
}
