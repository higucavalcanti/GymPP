package com.project.gymweb.services.exercise;

import com.project.gymweb.entities.Exercise;
import com.project.gymweb.utils.ExerciseComponent;

public class BasicExercise implements ExerciseComponent {
    private final Exercise exercise;

    public BasicExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    @Override
    public void execute() {
        System.out.println("Performing " + exercise.getSets() + " sets of " + exercise.getReps() + " reps of " + exercise.getName());
    }

    @Override
    public Long getSets() {
        return exercise.getSets();
    }

    @Override
    public Long getReps() {
        return exercise.getReps();
    }
}