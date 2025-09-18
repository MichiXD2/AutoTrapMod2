package com.example.autoplayer.safety;

import com.example.autoplayer.ai.Vec3i;
import com.example.autoplayer.world.WorldScanner;
import com.example.autoplayer.world.WorldStateSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates dangerous terrain and surfaces hazards to the safety manager.
 */
public class HazardDetector {

    private final WorldScanner worldScanner;

    public HazardDetector(WorldScanner worldScanner) {
        this.worldScanner = worldScanner;
    }

    public List<String> detectHazards(WorldStateSnapshot snapshot) {
        List<String> hazards = new ArrayList<>();
        Vec3i playerPos = snapshot.getPlayerPosition();
        if (playerPos.y() < 12) {
            hazards.add("Void proximity");
        }
        for (Vec3i hazard : snapshot.getHazards()) {
            if (distanceSquared(playerPos, hazard) < 9) {
                hazards.add("Nearby hazard at " + hazard);
            }
        }
        return hazards;
    }

    private int distanceSquared(Vec3i a, Vec3i b) {
        int dx = a.x() - b.x();
        int dy = a.y() - b.y();
        int dz = a.z() - b.z();
        return dx * dx + dy * dy + dz * dz;
    }
}
