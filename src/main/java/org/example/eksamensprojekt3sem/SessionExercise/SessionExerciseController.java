package org.example.eksamensprojekt3sem.SessionExercise;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fodboldklub/session-exercises")
public class SessionExerciseController {

    @Autowired
    private SessionExerciseService sessionExerciseService;

    @GetMapping
    public ResponseEntity<List<SessionExercise>> getAllSessionExercises() {
        return ResponseEntity.ok(sessionExerciseService.getAllSessionExercises());
    }

    @GetMapping("/session/{sessionId}/exercise/{exerciseId}")
    public ResponseEntity<SessionExercise> getSessionExerciseById(
            @PathVariable Long sessionId,
            @PathVariable Long exerciseId) {

        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(sessionId);
        id.setExerciseId(exerciseId);

        return sessionExerciseService.getSessionExerciseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SessionExercise> createSessionExercise(@Valid @RequestBody SessionExercise sessionExercise) {
        return ResponseEntity.ok(sessionExerciseService.createSessionExercise(sessionExercise));
    }

    @PutMapping("/session/{sessionId}/exercise/{exerciseId}")
    public ResponseEntity<SessionExercise> updateSessionExercise(
            @Valid
            @PathVariable Long sessionId,
            @PathVariable Long exerciseId,
            @RequestBody SessionExercise sessionExercise) {

        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(sessionId);
        id.setExerciseId(exerciseId);

        return ResponseEntity.ok(sessionExerciseService.updateSessionExercise(id, sessionExercise));
    }

    @DeleteMapping("/session/{sessionId}/exercise/{exerciseId}")
    public ResponseEntity<Void> deleteSessionExercise(
            @PathVariable Long sessionId,
            @PathVariable Long exerciseId) {

        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(sessionId);
        id.setExerciseId(exerciseId);

        sessionExerciseService.deleteSessionExercise(id);
        return ResponseEntity.noContent().build();
    }
}