package com.kosarev.lift;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.annotation.Nullable;

@Immutable
public class LiftTarget {

    private final int level;

    @Nullable
    private final Boolean up;

    public LiftTarget(int level, @Nullable Boolean up) {
        this.level = level;
        this.up = up;
    }

    public Boolean getUp() {
        return up;
    }

    public int getLevel() {
        return level;
    }

    public void onComplete(LiftState liftState) {
        if (up != null) {
            liftState.setMovingUp(up);
        }
    }

    public boolean isMatchedByDirection(LiftTarget another) {
        if (level != another.level) {
            return false;
        }
        return up == null || another.up == null || up.equals(another.up);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LiftTarget{");
        sb.append("level=").append(level);
        sb.append(", up=").append(up);
        sb.append('}');
        return sb.toString();
    }
}
