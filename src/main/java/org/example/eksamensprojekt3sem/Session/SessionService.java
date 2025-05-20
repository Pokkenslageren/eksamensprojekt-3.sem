package org.example.eksamensprojekt3sem.Session;

import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseId;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseRepository;
import org.example.eksamensprojekt3sem.Team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionExerciseRepository sessionExerciseRepository;
    private final ExerciseRepository exerciseRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository,
                          SessionExerciseRepository sessionExerciseRepository,
                          ExerciseRepository exerciseRepository,
                          TeamRepository teamRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionExerciseRepository = sessionExerciseRepository;
        this.exerciseRepository = exerciseRepository;
        this.teamRepository = teamRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    @Transactional
    public Session createSession(Session session, List<SessionExerciseDTO> exerciseDTOs) {
        // First persist the session to get an ID
        Session savedSession = sessionRepository.save(session);

        if (exerciseDTOs != null && !exerciseDTOs.isEmpty()) {
            for (SessionExerciseDTO dto : exerciseDTOs) {
                Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + dto.getExerciseId()));

                SessionExercise sessionExercise = new SessionExercise();

                sessionExercise.setSession(savedSession);
                sessionExercise.setExercise(exercise);
                sessionExercise.setOrderNum(dto.getOrderNum());
                sessionExercise.setNotes(dto.getNotes());

                SessionExerciseId compositeId = new SessionExerciseId();
                compositeId.setSessionId(savedSession.getSessionId());
                compositeId.setExerciseId(exercise.getExerciseId());
                sessionExercise.setId(compositeId);

                sessionExerciseRepository.save(sessionExercise);
            }
        }

        return sessionRepository.findById(savedSession.getSessionId())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created session"));
    }

    @Transactional
    public Session updateSession(Long sessionId, Session updatedSession, List<SessionExerciseDTO> exerciseDTOs) {

        Session existingSession = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));

        existingSession.setDateTime(updatedSession.getDateTime());
        existingSession.setLocation(updatedSession.getLocation());
        existingSession.setTeam(updatedSession.getTeam());

        Session savedSession = sessionRepository.save(existingSession);

        if (savedSession.getSessionExercises() != null && !savedSession.getSessionExercises().isEmpty()) {
            sessionExerciseRepository.deleteAll(savedSession.getSessionExercises());
            savedSession.getSessionExercises().clear();
        }

        if (exerciseDTOs != null && !exerciseDTOs.isEmpty()) {
            for (SessionExerciseDTO dto : exerciseDTOs) {
                Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + dto.getExerciseId()));


                SessionExercise sessionExercise = new SessionExercise();

                sessionExercise.setSession(savedSession);
                sessionExercise.setExercise(exercise);
                sessionExercise.setOrderNum(dto.getOrderNum());
                sessionExercise.setNotes(dto.getNotes());

                // Create the composite ID
                SessionExerciseId compositeId = new SessionExerciseId();
                compositeId.setSessionId(savedSession.getSessionId());
                compositeId.setExerciseId(exercise.getExerciseId());
                sessionExercise.setId(compositeId);
                sessionExerciseRepository.save(sessionExercise);
            }
        }
        return sessionRepository.findById(savedSession.getSessionId())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated session"));
    }

    @Transactional
    public void deleteSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));

        if (session.getSessionExercises() != null && !session.getSessionExercises().isEmpty()) {
            sessionExerciseRepository.deleteAll(session.getSessionExercises());
        }

        sessionRepository.delete(session);
    }

    // DTO class for session exercises
    public static class SessionExerciseDTO {
        private Long exerciseId;
        private Integer orderNum;
        private String notes;

        // Getters and setters
        public Long getExerciseId() {
            return exerciseId;
        }

        public void setExerciseId(Long exerciseId) {
            this.exerciseId = exerciseId;
        }

        public Integer getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(Integer orderNum) {
            this.orderNum = orderNum;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}