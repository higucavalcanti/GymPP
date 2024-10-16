package com.project.gymweb.utils;

import com.project.gymweb.entities.Exercise;
import com.project.gymweb.entities.Routine;

import java.util.UUID;

public class ExerciseBuilder {
    private UUID id;
    private String name;
    private Long sets;
    private Long reps;
    private Routine routine;

    public ExerciseBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ExerciseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ExerciseBuilder withSets(Long sets) {
        this.sets = sets;
        return this;
    }

    public ExerciseBuilder withReps(Long reps) {
        this.reps = reps;
        return this;
    }

    public ExerciseBuilder withRoutine(Routine routine) {
        this.routine = routine;
        return this;
    }

    public Exercise build() {
        Exercise exercise = new Exercise();
        exercise.setId(this.id != null ? this.id : UUID.randomUUID());
        exercise.setName(this.name);
        exercise.setSets(this.sets);
        exercise.setReps(this.reps);
        exercise.setRoutine(this.routine);
        return exercise;
    }
}
