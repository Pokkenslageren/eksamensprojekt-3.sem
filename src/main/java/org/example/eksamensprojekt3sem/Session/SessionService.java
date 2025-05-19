package org.example.eksamensprojekt3sem.Session;

import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseId;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseRepository;
import org.example.eksamensprojekt3sem.Team.Team;
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
        // Verify team exists if provided
        if (session.getTeam() != null && session.getTeam().getTeamId() != 0) {
            Team team = teamRepository.findById(session.getTeam().getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + session.getTeam().getTeamId()));
            session.setTeam(team);
        }

        // Save the session first to get an ID
        Session savedSession = sessionRepository.save(session);

        // Now create and save session exercises
        if (exerciseDTOs != null && !exerciseDTOs.isEmpty()) {
            List<SessionExercise> sessionExercises = new ArrayList<>();

            for (SessionExerciseDTO dto : exerciseDTOs) {
                Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + dto.getExerciseId()));

                SessionExercise sessionExercise = new SessionExercise(
                        savedSession,
                        exercise,
                        dto.getOrderNum(),
                        dto.getNotes()
                );

                // Set the composite key
                SessionExerciseId id = new SessionExerciseId();
                id.setSessionId(savedSession.getSessionId());
                id.setExerciseId(exercise.getExerciseId());
                sessionExercise.setId(id);

                sessionExerciseRepository.save(sessionExercise);
                sessionExercises.add(sessionExercise);
            }

            savedSession.setSessionExercises(sessionExercises);
        }

        return savedSession;
    }

    @Transactional
    public Session updateSession(Long id, Session sessionDetails, List<SessionExerciseDTO> exerciseDTOs) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));

        // Update session details
        session.setDateTime(sessionDetails.getDateTime());
        session.setLocation(sessionDetails.getLocation());


        Session updatedSession = sessionRepository.save(session);

        if (updatedSession.getSessionExercises() != null) {
            sessionExerciseRepository.deleteAll(updatedSession.getSessionExercises());
        }

        if (exerciseDTOs != null && !exerciseDTOs.isEmpty()) {
            List<SessionExercise> sessionExercises = new ArrayList<>();

            for (SessionExerciseDTO dto : exerciseDTOs) {
                Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + dto.getExerciseId()));

                SessionExercise sessionExercise = new SessionExercise(
                        updatedSession,
                        exercise,
                        dto.getOrderNum(),
                        dto.getNotes()
                );

                SessionExerciseId id2 = new SessionExerciseId();
                id2.setSessionId(updatedSession.getSessionId());
                id2.setExerciseId(exercise.getExerciseId());
                sessionExercise.setId(id2);

                sessionExerciseRepository.save(sessionExercise);
                sessionExercises.add(sessionExercise);
            }

            updatedSession.setSessionExercises(sessionExercises);
        }

        return updatedSession;
    }

    @Transactional
    public void deleteSession(Long id) {
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isPresent()) {
            Session session = sessionOptional.get();

            // Delete all associated exercises first
            if (session.getSessionExercises() != null) {
                sessionExerciseRepository.deleteAll(session.getSessionExercises());
            }

            sessionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Session not found with id: " + id);
        }
    }

    // DTO for session exercises
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