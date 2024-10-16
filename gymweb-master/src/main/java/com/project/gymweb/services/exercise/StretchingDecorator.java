package com.project.gymweb.services.exercise;

import com.project.gymweb.utils.ExerciseComponent;

public class StretchingDecorator extends ExerciseDecorator {
    public StretchingDecorator(ExerciseComponent decoratedExercise) {
        super(decoratedExercise);
    }

    @Override
    public void execute() {
        System.out.println("Starting stretching exercises...");
        super.execute();
        System.out.println("Finishing with stretching exercises.");
    }
}
