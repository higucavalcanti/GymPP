package com.project.gymweb.services.exercise;

import com.project.gymweb.utils.ExerciseComponent;

public abstract class ExerciseDecorator implements ExerciseComponent {
    protected ExerciseComponent decoratedExercise;

    public ExerciseDecorator(ExerciseComponent decoratedExercise) {
        this.decoratedExercise = decoratedExercise;
    }

    @Override
    public void execute() {
        decoratedExercise.execute();
    }

    @Override
    public Long getSets() {
        return decoratedExercise.getSets();
    }

    @Override
    public Long getReps() {
        return decoratedExercise.getReps();
    }
}

