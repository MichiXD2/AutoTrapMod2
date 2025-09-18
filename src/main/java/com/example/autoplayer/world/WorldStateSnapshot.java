package com.example.autoplayer.world;

import com.example.autoplayer.ai.Vec3i;

import java.util.Collections;
import java.util.List;

/**
 * Immutable snapshot of the world state for subsystems to inspect.
 */
public class WorldStateSnapshot {

    private final Vec3i playerPosition;
    private final List<Vec3i> hazards;

    public WorldStateSnapshot(Vec3i playerPosition, List<Vec3i> hazards) {
        this.playerPosition = playerPosition;
        this.hazards = List.copyOf(hazards);
    }

    public Vec3i getPlayerPosition() {
        return playerPosition;
    }

    public List<Vec3i> getHazards() {
        return Collections.unmodifiableList(hazards);
    }
}
