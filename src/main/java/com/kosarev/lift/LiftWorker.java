package com.kosarev.lift;

import java.time.LocalTime;

public class LiftWorker implements Runnable {

    private final Lift lift;
    private final long levelIntervalInMillis;
    private final long doorsTimeInMillis;

    private int level;
    private int targetLevel;
    private long lastActionTimeInMillis;
    private boolean movingUp;
    private LiftState liftState;

    public LiftWorker(Lift lift, double floorHeight, double speed, double doorsTime) {
        this.lift = lift;
        doorsTimeInMillis = (long) (doorsTime * 1000);
        levelIntervalInMillis = (long) (floorHeight / speed * 1000);

        level = 1;
        targetLevel = 0;
        movingUp = true;
        liftState = LiftState.STOP_DOORS_CLOSED;
    }

    public LiftWorker(Lift lift, LiftConfig liftConfig) {
        this(lift, liftConfig.getFloorHeight(), liftConfig.getSpeed(), liftConfig.getDoorsTime());
    }

    @Override
    public void run() {
        printLevel();
        while (!Thread.interrupted()) {

            switch (liftState) {
                case MOVING:
                    if (targetLevel == level) {
                        stop();
                    } else {
                        int shift = movingUp ? 1 : -1;
                        targetLevel = lift.refreshTarget(level + shift, targetLevel);
                        if (!isNextLevel()) {
                            Thread.yield();
                        }
                    }
                    break;
                case STOP_DOORS_OPEN:
                    if (!isDoorsClosed()) {
                        lift.completeTarget(level);
                        Thread.yield();
                    }
                    break;
                case STOP_DOORS_CLOSED:
                    targetLevel = lift.findNewTarget(level, movingUp);
                    if (targetLevel == level) {
                        openDoors();
                    } else if (targetLevel != 0) {
                        startMoving();
                    } else {
                        Thread.yield();
                    }
                    break;
            }
        }
    }

    private void startMoving() {
        sync();
        if (targetLevel > level)
            movingUp = true;
        else if (targetLevel < level) {
            movingUp = false;
        }
        doStartMoving();
    }

    private boolean isNextLevel() {
        if (System.currentTimeMillis() >= lastActionTimeInMillis + levelIntervalInMillis) {
            lastActionTimeInMillis = lastActionTimeInMillis + levelIntervalInMillis;
            if (movingUp) {
                level++;
            } else {
                level--;
            }
            printLevel();
            return true;
        }
        return false;
    }

    private void doStartMoving() {
        liftState = LiftState.MOVING;
    }

    private void stop() {
        liftState = LiftState.STOP_DOORS_CLOSED;
    }

    private void doOpenDoors() {
        liftState = LiftState.STOP_DOORS_OPEN;
    }

    private void openDoors() {
        sync();
        doOpenDoors();
        printDoorsOpen();
        completeTarget();
    }

    private void completeTarget() {
        lift.completeTarget(targetLevel);
        targetLevel = 0;
    }

    private boolean isDoorsClosed() {
        if (System.currentTimeMillis() >= lastActionTimeInMillis + doorsTimeInMillis) {
            stop();
            printDoorsClosed();
            return true;
        }
        return false;
    }

    private void sync() {
        lastActionTimeInMillis = System.currentTimeMillis();
    }

    private void printDoorsClosed() {
        System.out.println(String.format("%s: doors closed", LocalTime.now()));
    }

    private void printDoorsOpen() {
        System.out.println(String.format("%s: doors opened", LocalTime.now()));
    }

    private void printLevel() {
        System.out.println(String.format("%s: level %d", LocalTime.now(), level));
    }
}
