package org.example.eksamensprojekt3sem.Session;

import jakarta.validation.Valid;
import org.example.eksamensprojekt3sem.Session.SessionService.SessionExerciseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fodboldklub/sessions")
@CrossOrigin(origins = "*") // Enable CORS for testing
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Session> createSession(@Valid @RequestBody SessionWithExercisesRequest request) {
        Session session = sessionService.createSession(request.getSession(), request.getExercises());
        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody SessionWithExercisesRequest request) {
        Session session = sessionService.updateSession(id, request.getSession(), request.getExercises());
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    // Request wrapper for session and exercises
    public static class SessionWithExercisesRequest {
        private Session session;
        private List<SessionExerciseDTO> exercises;

        public Session getSession() {
            return session;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        public List<SessionExerciseDTO> getExercises() {
            return exercises;
        }

        public void setExercises(List<SessionExerciseDTO> exercises) {
            this.exercises = exercises;
        }
    }
}