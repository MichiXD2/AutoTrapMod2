package com.example.autoplayer.task;

import com.example.autoplayer.ai.Pathfinder;
import com.example.autoplayer.ai.State;
import com.example.autoplayer.ai.Vec3i;
import com.example.autoplayer.logic.InventoryManager;
import com.example.autoplayer.world.ResourceLocation;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Performs a simple branch mining routine to secure enough diamonds for end-game gear.
 */
public class BranchMineForDiamondsTask extends AbstractAutoPlayerTask {

    private static final int TARGET_DIAMONDS = 27;
    private static final String PREFIX = "branchMine";
    private boolean returning;

    @Override
    public String getName() {
        return "Branch Mine for Diamonds";
    }

    @Override
    protected void doOnStart(TaskContext context) {
        updateStatus("Descending to diamond level");
        context.getStateMachine().register(new DescendState());
        context.getStateMachine().register(new BranchState());
        context.getStateMachine().register(new ReturnState());
        context.getStateMachine().transition(PREFIX + ":descend", context);
    }

    @Override
    public void tick(TaskContext context) {
        InventoryManager inventory = context.getInventoryManager();
        if (inventory.getCount("diamond") >= TARGET_DIAMONDS) {
            updateStatus("Diamonds secured");
            if (!returning) {
                context.getStateMachine().transition(PREFIX + ":return", context);
            }
            return;
        }
        updateStatus("Branching tunnels (" + context.getStateMachine().getActiveStateName() + ")");
    }

    @Override
    public boolean isComplete(TaskContext context) {
        return returning && context.getInventoryManager().getCount("diamond") >= TARGET_DIAMONDS;
    }

    private class DescendState implements State {
        @Override
        public String getName() {
            return PREFIX + ":descend";
        }

        @Override
        public void onEnter(TaskContext context) {
            BranchMineForDiamondsTask.this.updateStatus("Descending towards Y11");
        }

        @Override
        public void tick(TaskContext context) {
            Vec3i current = context.getWorldScanner().getPlayerPosition();
            Vec3i destination = new Vec3i(current.x(), 11, current.z());
            Pathfinder pathfinder = context.getPathfinder();
            List<Vec3i> path = pathfinder.findPath(current, destination, context.getWorldScanner());
            pathfinder.followPath(path, context);
            context.getWorldScanner().updatePlayerPosition(destination);
            context.getStateMachine().transition(PREFIX + ":branch", context);
        }
    }

    private class BranchState implements State {
        @Override
        public String getName() {
            return PREFIX + ":branch";
        }

        @Override
        public void onEnter(TaskContext context) {
            BranchMineForDiamondsTask.this.updateStatus("Carving branch tunnels");
        }

        @Override
        public void tick(TaskContext context) {
            if (context.getSafetyManager().isEngaged()) {
                BranchMineForDiamondsTask.this.updateStatus("Paused due to hazard");
                return;
            }
            InventoryManager inventory = context.getInventoryManager();
            if (inventory.getCount("diamond") >= TARGET_DIAMONDS) {
                context.getStateMachine().transition(PREFIX + ":return", context);
                return;
            }
            ResourceLocation oreVein = context.getWorldScanner().scanForResource("diamond_ore");
            Pathfinder pathfinder = context.getPathfinder();
            List<Vec3i> path = pathfinder.findPath(context.getWorldScanner().getPlayerPosition(), oreVein.getPosition(), context.getWorldScanner());
            pathfinder.followPath(path, context);
            int diamonds = ThreadLocalRandom.current().nextInt(2, 6);
            inventory.addItem("diamond", diamonds);
            BranchMineForDiamondsTask.this.updateStatus("Mined " + diamonds + " diamonds");
        }
    }

    private class ReturnState implements State {
        @Override
        public String getName() {
            return PREFIX + ":return";
        }

        @Override
        public void onEnter(TaskContext context) {
            BranchMineForDiamondsTask.this.updateStatus("Returning to base");
        }

        @Override
        public void tick(TaskContext context) {
            Vec3i destination = new Vec3i(0, 64, 0);
            Pathfinder pathfinder = context.getPathfinder();
            List<Vec3i> path = pathfinder.findPath(context.getWorldScanner().getPlayerPosition(), destination, context.getWorldScanner());
            pathfinder.followPath(path, context);
            context.getWorldScanner().updatePlayerPosition(destination);
            returning = true;
        }
    }
}
