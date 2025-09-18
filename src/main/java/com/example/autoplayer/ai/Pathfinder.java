package com.example.autoplayer.ai;

import com.example.autoplayer.task.TaskContext;
import com.example.autoplayer.world.WorldScanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simplified path finding implementation that produces deterministic paths.
 */
public class Pathfinder {

    public List<Vec3i> findPath(Vec3i start, Vec3i goal, WorldScanner scanner) {
        if (start.equals(goal)) {
            return Collections.singletonList(start);
        }
        List<Vec3i> path = new ArrayList<>();
        path.add(start);
        Vec3i cursor = start;
        while (!cursor.equals(goal)) {
            int dx = Integer.compare(goal.x(), cursor.x());
            int dy = Integer.compare(goal.y(), cursor.y());
            int dz = Integer.compare(goal.z(), cursor.z());
            Vec3i next = cursor.add(dx, dy, dz);
            if (scanner.isPositionSafe(next)) {
                path.add(next);
                cursor = next;
            } else {
                // sidestep vertically until safe
                Vec3i sidestep = cursor.add(0, 1, 0);
                if (!scanner.isPositionSafe(sidestep)) {
                    sidestep = cursor.add(0, -1, 0);
                }
                path.add(sidestep);
                cursor = sidestep;
            }
        }
        return path;
    }

    public void followPath(List<Vec3i> path, TaskContext context) {
        if (path.isEmpty()) {
            return;
        }
        context.getHud().setNavigationPath(path);
    }
}
