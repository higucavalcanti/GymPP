package com.project.gymweb.services.exercise;

import com.project.gymweb.utils.ExerciseComponent;

public class WarmUpDecorator extends ExerciseDecorator {
    public WarmUpDecorator(ExerciseComponent decoratedExercise) {
        super(decoratedExercise);
    }

    @Override
    public void execute() {
        System.out.println("Starting warm-up...");
        super.execute();
        System.out.println("Finished warm-up.");
    }
}
