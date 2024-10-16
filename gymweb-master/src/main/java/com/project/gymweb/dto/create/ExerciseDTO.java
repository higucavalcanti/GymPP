package com.project.gymweb.dto.create;

import java.util.UUID;

public record ExerciseDTO(String name, Long sets, Long reps, UUID routineId, 
                          boolean withWarmUp, boolean withStretching, 
                          double intensityMultiplier) {
    public ExerciseDTO {
        if (intensityMultiplier == 0) {
            intensityMultiplier = 1.0; 
        }
    }
}
