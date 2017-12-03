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
        checkArgument(height >= MIN_HEIGHT && height <= MAX_HEIGHT, "height is out of range, actual: " + height);
        System.out.println("height = " + height);
        floorHeight = args.length >= 2 ? Double.parseDouble(args[1]) : 3.0;
        checkArgument(floorHeight > 0.0, "floor height must be positive, actual: " + floorHeight);
        System.out.println("floorHeight = " + floorHeight);
        speed = args.length >= 3 ? Double.parseDouble(args[2]) : 1.0;
        checkArgument(speed > 0.0, "floor height must be positive, actual: " + speed);
        System.out.println("speed = " + speed);
        doorsTime = args.length >= 4 ? Double.parseDouble(args[2]) : 5.0;
        checkArgument(doorsTime > 0.0, "doors openinig time must be positive, actual: " + doorsTime);
        System.out.println("doorsTime = " + doorsTime);
    }

    private void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

    public int parseTarget(String input) {
        if (input.length() == 0) {
            throw new IllegalArgumentException("command expected");
        }
        switch (input.charAt(0)) {
            case 'i':
            case 'o':
                int targetLevel;
                try {
                    targetLevel = Integer.parseInt(input.substring(1));
                } catch (NumberFormatException ne) {
                    throw new IllegalArgumentException("wrong level: " + input.substring(1));
                }
                if (targetLevel < 1 || targetLevel > height) {
                    throw new IllegalArgumentException("wrong level: " + targetLevel);
                }
                return targetLevel;

            default:
                throw new IllegalArgumentException("wrong command: " + input.charAt(0));
        }
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
