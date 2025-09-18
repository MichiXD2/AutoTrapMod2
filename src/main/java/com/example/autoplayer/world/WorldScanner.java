package com.example.autoplayer.world;

import com.example.autoplayer.ai.Vec3i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Responsible for world analysis to power high level automation decisions.
 */
public class WorldScanner {

    private final int scanRadius;
    private Vec3i playerPosition = new Vec3i(0, 64, 0);
    private final Random random = new Random();
    private final List<Vec3i> knownHazards = new ArrayList<>();

    public WorldScanner(int scanRadius) {
        this.scanRadius = scanRadius;
    }

    public void updatePlayerPosition(Vec3i position) {
        this.playerPosition = position;
    }

    public Vec3i getPlayerPosition() {
        return playerPosition;
    }

    public ResourceLocation scanForResource(String resourceId) {
        int offsetX = random.nextInt(scanRadius * 2 + 1) - scanRadius;
        int offsetZ = random.nextInt(scanRadius * 2 + 1) - scanRadius;
        int offsetY = Math.max(-scanRadius, Math.min(scanRadius, random.nextInt(16) - 8));
        Vec3i position = playerPosition.add(offsetX, offsetY, offsetZ);
        return new ResourceLocation(resourceId, position, random.nextInt(8) + 1);
    }

    public boolean isPositionSafe(Vec3i position) {
        if (position.y() < 4) {
            return false;
        }
        return !knownHazards.contains(position);
    }

    public void markHazard(Vec3i position) {
        if (!knownHazards.contains(position)) {
            knownHazards.add(position);
        }
    }

    public List<Vec3i> getKnownHazards() {
        return Collections.unmodifiableList(knownHazards);
    }
}
