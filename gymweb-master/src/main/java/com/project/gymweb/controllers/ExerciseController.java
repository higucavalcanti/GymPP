package com.project.gymweb.controllers;

import com.project.gymweb.dto.create.ExerciseDTO;
import com.project.gymweb.dto.view.ExerciseRO;
import com.project.gymweb.entities.Exercise;
import com.project.gymweb.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("Exercise")
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ExerciseRO>> getAllExercises() {
        List<ExerciseRO> exercises = exerciseService.findAll();
        if (exercises.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(exercises);
        }
        return ResponseEntity.status(HttpStatus.OK).body(exercises);
    }

    @GetMapping("/routine/{routineId}")
    public ResponseEntity<?> getExercisesByRoutine(@PathVariable UUID routineId) {
        List<ExerciseRO> exercises = exerciseService.findByRoutineId(routineId);
        if (exercises.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No exercises found for routine with ID: " + routineId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(exercises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExerciseById(@PathVariable UUID id) {
        try {
            Exercise exercise = exerciseService.getExercise(id);
            return ResponseEntity.status(HttpStatus.OK).body(exercise);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Exercise with ID: " + id + " not found.");
        }
    }

    @PostMapping("/")
    public ResponseEntity<ExerciseRO> createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        var exercise = exerciseService.createExercise(exerciseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(exercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable UUID id, @RequestBody ExerciseDTO exerciseDTO) {
        try {
            ExerciseRO updatedExercise = exerciseService.updateExercise(id, exerciseDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Exercise updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Exercise with ID: " + id + " not found.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable UUID id) {
        try {
            exerciseService.deleteExercise(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Exercise with ID: " + id + " was successfully deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Exercise with ID: " + id + " not found.");
        }
    }
}
