package com.project.gymweb.dto.view;

import java.util.UUID;

public record ExerciseRO(UUID id, String name, Long sets, Long reps, UUID routineId) {
}
