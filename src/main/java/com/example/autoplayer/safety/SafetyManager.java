package com.example.autoplayer.safety;

import com.example.autoplayer.ai.Vec3i;
import com.example.autoplayer.task.TaskContext;
import com.example.autoplayer.world.WorldScanner;
import com.example.autoplayer.world.WorldStateSnapshot;

import java.util.List;

/**
 * Central safety coordinator that can pause automation when hazards are detected.
 */
public class SafetyManager {

    private final WorldScanner worldScanner;
    private final HazardDetector hazardDetector;
    private boolean engaged;

    public SafetyManager(WorldScanner worldScanner) {
        this.worldScanner = worldScanner;
        this.hazardDetector = new HazardDetector(worldScanner);
    }

    public void tick(TaskContext context) {
        WorldStateSnapshot snapshot = new WorldStateSnapshot(worldScanner.getPlayerPosition(), worldScanner.getKnownHazards());
        List<String> hazards = hazardDetector.detectHazards(snapshot);
        engaged = !hazards.isEmpty();
        if (engaged) {
            context.getHud().setSafetyWarning(String.join(", ", hazards));
        } else {
            context.getHud().setSafetyWarning(null);
        }
    }

    public boolean isEngaged() {
        return engaged;
    }

    public void markHazard(Vec3i position) {
        worldScanner.markHazard(position);
    }
}
