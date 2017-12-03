package com.kosarev.lift;

import java.io.Console;

public class Main {
    private static final Console console = System.console();
    private static Lift lift;
    private static LiftConfig liftConfig;

    public static void main(String[] args) {
        init(args);

        while (true) {
            try {
                final String input = console.readLine();
                System.out.println("input = " + input);
                if (input.equals("q")) {
                    break;
                }
                int targetLevel = liftConfig.parseTarget(input);
                lift.submitTarget(targetLevel);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        exit();
    }

    private static void exit() {
        lift.shutdown();
        System.out.println("bye");
    }

    private static void init(String[] args) {
        liftConfig = new LiftConfig(args);
        lift = new Lift(liftConfig);
    }
}
