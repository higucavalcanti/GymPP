package com.project.gymweb.controllers;

import com.project.gymweb.dto.create.RoutineDTO;
import com.project.gymweb.dto.view.RoutineRO;
import com.project.gymweb.services.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController("Routines")
@RequestMapping("/api/routines")
public class RoutineController {
    private final RoutineService routineService;

    @Autowired
    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping("/")
    public ResponseEntity<List<RoutineRO>> routines() {
        var routines = routineService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(routines);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoutineRO>> routinesByUserId(@PathVariable("userId") UUID userId) {
        var routines = routineService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(routines);
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<List<RoutineRO>> routinesByUserUsername(@PathVariable String username) {
        var routines = routineService.findAllByUserUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(routines);
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<List<RoutineRO>> routinesByUserEmail(@PathVariable String email) {
        var routines = routineService.findAllByUserEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(routines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineRO> routine(@PathVariable UUID id) {
        var routine = routineService.getRoutine(id);
        return ResponseEntity.status(HttpStatus.OK).body(routine);
    }

    @GetMapping("/{routineId}/export/pdf")
    public ResponseEntity<InputStreamResource> exportRoutineToPDF(@PathVariable UUID routineId) throws IOException {
        return routineService.exportRoutineToPDF(routineId);
    }

    @PostMapping("/")
    public ResponseEntity<RoutineRO> addRoutine(@RequestBody RoutineDTO routineDTO) {
        var routine = routineService.createRoutine(routineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(routine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutineRO> updateRoutine(@PathVariable UUID id, @RequestBody RoutineDTO routineDTO) {
        var routine = routineService.updateRoutine(id, routineDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(routine);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable UUID id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
