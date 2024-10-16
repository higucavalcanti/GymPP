package com.project.gymweb.services;

import com.project.gymweb.dto.create.ExerciseDTO;
import com.project.gymweb.dto.view.ExerciseRO;
import com.project.gymweb.entities.Exercise;
import com.project.gymweb.exceptions.ExerciseNotFoundException;
import com.project.gymweb.exceptions.RoutineNotFoundException;
import com.project.gymweb.repositories.ExerciseRepository;
import com.project.gymweb.repositories.RoutineRepository;
import com.project.gymweb.services.exercise.BasicExercise;
import com.project.gymweb.services.exercise.IntensityDecorator;
import com.project.gymweb.services.exercise.StretchingDecorator;
import com.project.gymweb.services.exercise.WarmUpDecorator;
import com.project.gymweb.utils.ExerciseBuilder;
import com.project.gymweb.utils.ExerciseComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExerciseService {
    
    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, RoutineRepository routineRepository) {
        this.exerciseRepository = exerciseRepository;
        this.routineRepository = routineRepository;
    }

    public List<ExerciseRO> findAll() {
        return exerciseRepository.findAll().stream().map(this::entityToRO).toList();
    }

    public List<ExerciseRO> findByRoutineId(UUID routineId) {
        var routine = routineRepository.findById(routineId).orElseThrow(() -> new RoutineNotFoundException("Routine with id " + routineId + " was not found"));
        return exerciseRepository.findAllByRoutineId(routine.getId()).stream().map(this::entityToRO).toList();
    }

    public Exercise getExercise(UUID id) {
        return exerciseRepository.findById(id)
               .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + id + " was not found"));
    }

    public ExerciseRO createExercise(ExerciseDTO exerciseDTO) {
        Exercise exercise = new Exercise();
        exercise.setName(exerciseDTO.name());
        exercise.setSets(exerciseDTO.sets());
        exercise.setReps(exerciseDTO.reps());

        var routine = routineRepository.findById(exerciseDTO.routineId())
                .orElseThrow(() -> new RoutineNotFoundException("Routine with id " + exerciseDTO.routineId() + " was not found"));
        exercise.setRoutine(routine);

        ExerciseComponent exerciseComponent = new BasicExercise(exercise);

        if (exerciseDTO.withWarmUp()) {
            exerciseComponent = new WarmUpDecorator(exerciseComponent);
        }

        if (exerciseDTO.withStretching()) {
            exerciseComponent = new StretchingDecorator(exerciseComponent);
        }

        if (exerciseDTO.intensityMultiplier() != 1.0) {
            exerciseComponent = new IntensityDecorator(exerciseComponent, exerciseDTO.intensityMultiplier());
        }

        exerciseComponent.execute();

        var savedExercise = exerciseRepository.save(exercise);

        return new ExerciseRO(savedExercise.getId(), savedExercise.getName(), savedExercise.getSets(), savedExercise.getReps(), savedExercise.getRoutine().getId());
    }

    public ExerciseRO updateExercise(UUID id, ExerciseDTO exerciseDTO) {
        var exercise = exerciseRepository.findById(id);

        if (exercise.isPresent()) {
            var routine = routineRepository.findById(exerciseDTO.routineId()).orElseThrow();

            var updatedExercise = dtoToEntity(exerciseDTO);

            updatedExercise.setId(exercise.get().getId());
            updatedExercise.setRoutine(routine);

            var savedExercise = exerciseRepository.save(updatedExercise);

            return entityToRO(savedExercise);
        }

        throw new ExerciseNotFoundException("Exercise with id " + id + " was not found");
    }

    public void deleteExercise(UUID id) {
        var exercise = exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + id + " was not found"));
        exerciseRepository.deleteById(exercise.getId());
    }

    private Exercise dtoToEntity(ExerciseDTO exerciseDTO) {
        var routine = routineRepository.findById(exerciseDTO.routineId()).orElseThrow(() -> new RoutineNotFoundException("Routine with id " + exerciseDTO.routineId() + " was not found"));

        return new ExerciseBuilder()
                .withId(UUID.randomUUID())
                .withName(exerciseDTO.name())
                .withSets(exerciseDTO.sets())
                .withReps(exerciseDTO.reps())
                .withRoutine(routine).build();
    }

    private ExerciseRO entityToRO(Exercise exercise) {
        return new ExerciseRO(exercise.getId(), exercise.getName(), exercise.getSets(), exercise.getReps(), exercise.getRoutine().getId());
    }
}
