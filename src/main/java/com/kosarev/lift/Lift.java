package com.kosarev.lift;

import java.util.concurrent.atomic.AtomicBoolean;

public class Lift {
    private final int height;
    private final AtomicBoolean[] targetLevels;
    private final Thread liftThread;

    public Lift(LiftConfig config) {
        this.height = config.getHeight();
        targetLevels = new AtomicBoolean[height];
        for (int i = 0; i < targetLevels.length; i++) {
            targetLevels[i] = new AtomicBoolean(false);
        }
        liftThread = new Thread(new LiftWorker(this, config));
        liftThread.start();
    }

    public void submitTarget(int targetLevel) {
        setTargetLevelValue(targetLevel, true);
    }

    public void completeTarget(int level) {
        setTargetLevelValue(level, false);
    }

    public int refreshTarget(int level, int targetLevel) {
        if (level == targetLevel) {
            return targetLevel;
        }
        return findTarget(level, targetLevel);
    }

    public int findNewTarget(int level, boolean movingUp) {
        int targetLevel;
        int limit = movingUp ? height : 1;
        targetLevel = findTarget(level, limit);
        if (targetLevel == 0) {
            limit = height + 1 - limit; // 1 -> height, height -> 1
            targetLevel = findTarget(level, limit);
        }
        return targetLevel;
    }

    public void shutdown() {
        liftThread.interrupt();
    }

    private void setTargetLevelValue(int level, boolean value) {
        targetLevels[level - 1].set(value);
    }

    private int findTarget(int level, int limit) {
        int dst = level <= limit ? 1 : -1;
        int i = level;
        do {
            if (targetLevels[i - 1].get()) {
                return i;
            }
            if (i == limit) {
                return 0;
            }
            i += dst;
        } while (true);
    }
}
