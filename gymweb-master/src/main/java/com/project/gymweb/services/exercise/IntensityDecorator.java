package com.project.gymweb.services.exercise;

import com.project.gymweb.utils.ExerciseComponent;

public class IntensityDecorator extends ExerciseDecorator {
    private final double intensityMultiplier;

    public IntensityDecorator(ExerciseComponent decoratedExercise, double intensityMultiplier) {
        super(decoratedExercise);
        this.intensityMultiplier = intensityMultiplier;
    }

    @Override
    public void execute() {
        System.out.println("Increasing intensity...");
        System.out.println("Performing " + getSets() + " sets of " + Math.round(getReps() * intensityMultiplier) + " reps.");
    }
}
