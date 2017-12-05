package com.kosarev.lift;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.TreeSet;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Lift {
    private final LiftConfig liftConfig;
    private Comparator<LiftTarget> MOVING_UP_COMPARATOR = (o1, o2) -> {
        if (FALSE.equals(o1.getUp()) && !FALSE.equals(o2.getUp())) {
            return 1;
        } else if (FALSE.equals(o2.getUp()) && !FALSE.equals(o1.getUp())) {
            return -1;
        } else if (FALSE.equals(o1.getUp()) && FALSE.equals(o2.getUp())) {
            return o2.getLevel() - o1.getLevel();
        } else {
            final int i = o1.getLevel() - o2.getLevel();
            if (i != 0) {
                return i;
            }
            return (o1.getUp() == null ? 1 : 0) - (o2.getUp() == null ? 1 : 0);
        }
    };

    private Comparator<LiftTarget> MOVING_DOWN_COMPARATOR = (o1, o2) -> {
        if (TRUE.equals(o1.getUp()) && !TRUE.equals(o2.getUp())) {
            return 1;
        } else if (TRUE.equals(o2.getUp()) && !TRUE.equals(o1.getUp())) {
            return -1;
        } else if (TRUE.equals(o1.getUp()) && TRUE.equals(o2.getUp())) {
            return o1.getLevel() - o2.getLevel();
        } else {
            final int i = o2.getLevel() - o1.getLevel();
            if (i != 0) {
                return i;
            }
            return (o1.getUp() == null ? 1 : 0) - (o2.getUp() == null ? 1 : 0);
        }
    };
    private Thread liftThread;
    private TreeSet<LiftTarget> liftTargetsOrderedUp = new TreeSet<>(MOVING_UP_COMPARATOR);

    private TreeSet<LiftTarget> liftTargetsOrderdDown = new TreeSet<>(MOVING_DOWN_COMPARATOR);

    @Nullable
    private LiftTarget markedTarget;

    public Lift(LiftConfig liftConfig) {
        this.liftConfig = liftConfig;
    }

    public void start() {
        if (liftThread == null) {
            liftThread = new Thread(new LiftWorker(this, liftConfig.getFloorHeight(), liftConfig.getSpeed(), liftConfig.getDoorsTime()));
            liftThread.start();
        }
    }

    public synchronized void submitTarget(LiftTarget target) {
        if (markedTarget == null || !markedTarget.isMatchedByDirection(target)) {
            liftTargetsOrderedUp.add(target);
            liftTargetsOrderdDown.add(target);
        }
    }

    public synchronized void completeAndMarkMatchingTargets(LiftTarget completed) {
        markedTarget = completed;
        liftTargetsOrderedUp.removeIf(target -> target.isMatchedByDirection(completed));
        liftTargetsOrderdDown.removeIf(target -> target.isMatchedByDirection(completed));
    }

    public synchronized void unmarkMatchingTargets() {
        markedTarget = null;
    }

    public LiftTarget findTarget(int level, boolean movingUp) {
        final LiftTarget liftTarget = new LiftTarget(level, movingUp);
        if (movingUp) {
            return liftTargetsOrderedUp.ceiling(liftTarget);
        } else {
            return liftTargetsOrderdDown.ceiling(liftTarget);
        }
    }

    public void shutdown() {
        if (liftThread != null) {
            liftThread.interrupt();
            liftThread = null;
        }
    }

}
