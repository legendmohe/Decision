package com.legendmohe;

import javafx.scene.input.TouchEvent;

public class Main {

    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_DEFAULT = 2;
    public static final int PRIORITY_LOW = 1;

    public static void main(String[] args) {
        Decision.forEvent("touch").add(new Decision.MakeDecision<TouchEvent, Integer>() {
            @Override
            public Integer onEvent(TouchEvent event) {
                return null;
            }

            @Override
            public int getPriority() {
                return PRIORITY_HIGH;
            }
        });

        Decision.forEvent("touch").add(new Decision.MakeDecision<TouchEvent, Integer>() {
            @Override
            public Integer onEvent(TouchEvent event) {
                return 2;
            }

            @Override
            public int getPriority() {
                return PRIORITY_DEFAULT;
            }
        });

        Decision.forEvent("touch").add(new Decision.MakeDecision<TouchEvent, Integer>() {
            @Override
            public Integer onEvent(TouchEvent event) {
                return 1;
            }

            @Override
            public int getPriority() {
                return PRIORITY_LOW;
            }
        });

        Integer result = Decision.forEvent("touch")
                .getResult(new TouchEvent(null, null, null, -1, false, false, false, false));
        System.out.println("result=" + result);

        Decision.clear();
    }

}
