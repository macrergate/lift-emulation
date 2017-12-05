package com.kosarev.lift;

import java.time.LocalTime;

import static com.kosarev.lift.Utils.check;

public class LiftState {

    private boolean doorsOpen;

    private boolean moving;

    private boolean movingUp;

    private int level = 1;

    public boolean isDoorsOpen() {
        return doorsOpen;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public int getLevel() {
        return level;
    }

    public void stopAndOpenDoors() {
        check(!doorsOpen);
        moving = false;
        doorsOpen = true;
        printDoorsOpen();
    }

    public void closeDoors() {
        check(doorsOpen);
        check(!moving);
        doorsOpen = false;
        printDoorsClosed();
    }

    public void nextLevel() {
        check(moving);
        if (movingUp) {
            level++;
        } else {
            level--;
        }
        printLevel();
    }

    public void startMoving(boolean movingUp) {
        check(!doorsOpen);
        this.movingUp = movingUp;
        this.moving = true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LiftState{");
        sb.append("doorsOpen=").append(doorsOpen);
        sb.append(", moving=").append(moving);
        sb.append(", movingUp=").append(movingUp);
        sb.append(", level=").append(level);
        sb.append('}');
        return sb.toString();
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
