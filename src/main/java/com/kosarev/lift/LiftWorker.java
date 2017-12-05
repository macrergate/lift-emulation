package com.kosarev.lift;

import javax.annotation.Nullable;

public class LiftWorker implements Runnable {

    private final Lift lift;
    private final long levelIntervalInMillis;
    private final long doorsTimeInMillis;

    @Nullable
    private LiftTarget target;

    private long lastActionTimeInMillis;

    private LiftState liftState;

    public LiftWorker(Lift lift, double floorHeight, double speed, double doorsTime) {
        this.lift = lift;

        doorsTimeInMillis = (long) (doorsTime * 1000);
        levelIntervalInMillis = (long) (floorHeight / speed * 1000);

        liftState = new LiftState();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {

            if (liftState.isMoving()) {
                if (target.getLevel() == liftState.getLevel()) {
                    stopAndOpenDoors();
                } else {
                    final int shift = liftState.isMovingUp() ? 1 : -1;
                    LiftTarget newTarget = lift.findTarget(liftState.getLevel() + shift, liftState.isMovingUp());
                    Utils.check(newTarget != null && newTarget.getLevel() > liftState.getLevel() == liftState.isMovingUp(), liftState, newTarget, target);
                    target = newTarget;
                    if (!isNextLevel()) {
                        Thread.yield();
                    }
                }
            } else if (liftState.isDoorsOpen()) {
                if (!isDoorsJustClosed()) {
                    lift.unmarkMatchingTargets();
                } else {
                    lift.completeAndMarkMatchingTargets(new LiftTarget(liftState.getLevel(), liftState.isMovingUp()));
                    Thread.yield();
                }
            } else {//Doors closed and stopped
                target = lift.findTarget(liftState.getLevel(), liftState.isMovingUp());
                if (target == null) {
                    target = lift.findTarget(liftState.getLevel(), !liftState.isMovingUp());
                }
                if (target == null) {
                    Thread.yield();
                } else if (target.getLevel() == liftState.getLevel()) {
                    stopAndOpenDoors();
                } else {
                    startMoving();
                }
            }
        }
    }

    private void startMoving() {
        sync();
        liftState.startMoving(target.getLevel() > liftState.getLevel());
    }

    private boolean isNextLevel() {
        if (System.currentTimeMillis() >= lastActionTimeInMillis + levelIntervalInMillis) {
            lastActionTimeInMillis = lastActionTimeInMillis + levelIntervalInMillis;
            liftState.nextLevel();
            return true;
        }
        return false;
    }

    private void stopAndOpenDoors() {
        sync();
        liftState.stopAndOpenDoors();
        completeTargets();
    }

    private void completeTargets() {
        target.onComplete(liftState); // possibly change direction
        lift.completeAndMarkMatchingTargets(new LiftTarget(liftState.getLevel(), liftState.isMovingUp()));
        target = null;
    }

    private boolean isDoorsJustClosed() {
        if (System.currentTimeMillis() >= lastActionTimeInMillis + doorsTimeInMillis) {
            liftState.closeDoors();
            return true;
        }
        return false;
    }

    private void sync() {
        lastActionTimeInMillis = System.currentTimeMillis();
    }

}
