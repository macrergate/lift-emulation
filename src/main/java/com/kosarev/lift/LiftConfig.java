package com.kosarev.lift;

public class LiftConfig {

    private static final int MAX_HEIGHT = 20;
    private static final int MIN_HEIGHT = 5;
    private final int height;
    private final double floorHeight;
    private final double speed;
    private final double doorsTime;

    public LiftConfig(String[] args) {
        height = args.length >= 1 ? Integer.parseInt(args[0]) : 20;
        Utils.check(height >= MIN_HEIGHT && height <= MAX_HEIGHT, "height is out of range, actual: " + height);
        System.out.println("height = " + height);
        floorHeight = args.length >= 2 ? Double.parseDouble(args[1]) : 3.0;
        Utils.check(floorHeight > 0.0, "floor height must be positive, actual: " + floorHeight);
        System.out.println("floorHeight = " + floorHeight);
        speed = args.length >= 3 ? Double.parseDouble(args[2]) : 1.0;
        Utils.check(speed > 0.0, "floor height must be positive, actual: " + speed);
        System.out.println("speed = " + speed);
        doorsTime = args.length >= 4 ? Double.parseDouble(args[2]) : 5.0;
        Utils.check(doorsTime > 0.0, "doors openinig time must be positive, actual: " + doorsTime);
        System.out.println("doorsTime = " + doorsTime);
    }

    public LiftTarget parseTarget(String input) {
        if (input.length() == 0) {
            throw new IllegalArgumentException("command expected");
        }
        Boolean movingUp;
        switch (input.charAt(0)) {
            case 'i':
                movingUp = null;
                break;
            case 'd':
                movingUp = false;
                break;
            case 'u':
                movingUp = true;
                break;
            default:
                throw new IllegalArgumentException("wrong command: " + input.charAt(0) + ". any of i, d, u, q expected.");
        }
        int targetLevel;
        try {
            targetLevel = Integer.parseInt(input.substring(1));
        } catch (NumberFormatException ne) {
            throw new IllegalArgumentException("wrong level: " + input.substring(1));
        }
        if (targetLevel < 1 || targetLevel > height) {
            throw new IllegalArgumentException("wrong level: " + targetLevel);
        }
        return new LiftTarget(targetLevel, movingUp);

    }

    public int getHeight() {
        return height;
    }

    public double getFloorHeight() {
        return floorHeight;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDoorsTime() {
        return doorsTime;
    }
}
