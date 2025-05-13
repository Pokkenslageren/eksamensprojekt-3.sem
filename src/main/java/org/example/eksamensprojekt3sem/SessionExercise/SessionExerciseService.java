package org.example.eksamensprojekt3sem.SessionExercise;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionExerciseService {

    @Autowired
    private SessionExerciseRepository sessionExerciseRepository;


    public SessionExerciseService(SessionExerciseRepository sessionExerciseRepository) {
        this.sessionExerciseRepository = sessionExerciseRepository;
    }

    public List<SessionExercise> getAllSessionExercises() {
        return sessionExerciseRepository.findAll();
    }

    public Optional<SessionExercise> getSessionExerciseById(SessionExerciseId id) {
        return sessionExerciseRepository.findById(id);
    }

    public SessionExercise createSessionExercise(SessionExercise sessionExercise) {
        return sessionExerciseRepository.save(sessionExercise);
    }

    public SessionExercise updateSessionExercise(SessionExerciseId id, SessionExercise sessionExerciseDetails) {
        SessionExercise sessionExercise = sessionExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SessionExercise not found"));

        sessionExercise.setOrderNum(sessionExerciseDetails.getOrderNum());
        sessionExercise.setNotes(sessionExerciseDetails.getNotes());

        return sessionExerciseRepository.save(sessionExercise);
    }

    public void deleteSessionExercise(SessionExerciseId id) {
        sessionExerciseRepository.deleteById(id);
    }
}