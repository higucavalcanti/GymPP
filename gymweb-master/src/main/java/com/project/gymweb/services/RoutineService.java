package com.project.gymweb.services;

import com.project.gymweb.dto.create.RoutineDTO;
import com.project.gymweb.dto.view.RoutineRO;
import com.project.gymweb.entities.Exercise;
import com.project.gymweb.entities.Routine;
import com.project.gymweb.exceptions.RoutineNotFoundException;
import com.project.gymweb.exceptions.UserNotFoundException;
import com.project.gymweb.repositories.ExerciseRepository;
import com.project.gymweb.repositories.RoutineRepository;
import com.project.gymweb.repositories.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class RoutineService {
    private final RoutineRepository repository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public RoutineService(RoutineRepository repository, UserRepository userRepository, ExerciseRepository exerciseRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<RoutineRO> findAll() {
        return repository.findAll().stream().map(this::entityToRO).toList();
    }

    public List<RoutineRO> findAllByUserId(UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found"));
        return repository.findAllByUserId(user.getId()).stream().map(this::entityToRO).toList();
    }

    public List<RoutineRO> findAllByUserUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " was not found"));
        return repository.findAllByUserId(user.getId()).stream().map(this::entityToRO).toList();
    }

    public List<RoutineRO> findAllByUserEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " was not found"));
        return repository.findAllByUserId(user.getId()).stream().map(this::entityToRO).toList();
    }

    public RoutineRO getRoutine(UUID routineId) {
        var routine = repository.findById(routineId).orElseThrow(() -> new RoutineNotFoundException("Routine with id " + routineId + " was not found"));
        return entityToRO(routine);
    }

    public RoutineRO createRoutine(RoutineDTO routineDTO) {
        Routine routine = dtoToEntity(routineDTO);
        var savedRoutine = repository.save(routine);
        return entityToRO(savedRoutine);
    }

    public RoutineRO updateRoutine(UUID id, RoutineDTO routineDTO) {
        var routine = repository.findById(id).orElseThrow(() -> new RoutineNotFoundException("Routine with id " + id + " was not found"));
        var user = userRepository.findById(routineDTO.userId()).orElseThrow(() -> new UserNotFoundException("User with id " + routineDTO.userId() + " was not found"));

        routine.setName(routineDTO.name());
        routine.setUser(user);

        var savedRoutine = repository.save(routine);

        return entityToRO(savedRoutine);
    }

    public void deleteRoutine(UUID id) {
        var routine = repository.findById(id).orElseThrow(() -> new RoutineNotFoundException("Routine with id " + id + " was not found"));
        repository.deleteById(routine.getId());
    }

    private Routine dtoToEntity(RoutineDTO routineDTO) {
        Routine routine = new Routine();

        routine.setId(UUID.randomUUID());
        routine.setName(routineDTO.name());

        var user = userRepository.findById(routineDTO.userId()).orElseThrow(() -> new UserNotFoundException("User with id " + routineDTO.userId() + " was not found"));

        routine.setUser(user);

        return routine;
    }

    private RoutineRO entityToRO(Routine routine) {
        return new RoutineRO(routine.getId(), routine.getName(), routine.getUser().getId());
    }

    public ResponseEntity<InputStreamResource> exportRoutineToPDF(UUID routineId) throws IOException {
        Routine routine = repository.findById(routineId).orElseThrow(() -> new RoutineNotFoundException("Routine with id " + routineId + " was not found"));
        List<Exercise> exercises = exerciseRepository.findAllByRoutineId(routineId);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Routine Plan: " + routine.getName());
                contentStream.newLine();
                contentStream.showText("User: " + routine.getUser().getUsername());
                contentStream.newLine();
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                for (Exercise exercise : exercises) {
                    contentStream.showText("Exercise: " + exercise.getName());
                    contentStream.newLine();
                    contentStream.showText("Sets: " + exercise.getSets() + " | Reps: " + exercise.getReps());
                    contentStream.newLine();
                }
                contentStream.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);

            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=routine_plan.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfInputStream));
        }
    }
}
