package org.example.eksamensprojekt3sem.SessionExercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
import org.example.eksamensprojekt3sem.Session.Session;
import org.example.eksamensprojekt3sem.Session.SessionRepository;
import org.example.eksamensprojekt3sem.Team.Team;
import org.example.eksamensprojekt3sem.Team.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SessionExerciseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SessionExerciseRepository sessionExerciseRepository;

    private Session session;
    private Exercise exercise;
    private Team team;

    @BeforeEach
    void setUp() {
        // Create a team
        team = new Team();
        team.setName("Integration Test Team");
        team.setDescription("Team for session exercise tests");
        team.setActive(true);
        team = teamRepository.save(team);

        // Create a session
        session = new Session();
        session.setTeam(team);
        session.setDateTime(LocalDateTime.now().plusDays(1));
        session.setLocation("Integration Test Location");
        session = sessionRepository.save(session);

        // Create an exercise
        exercise = new Exercise();
        exercise.setName("Integration Test Exercise");
        exercise.setDescription("Exercise for session exercise tests");
        exercise.setDuration(25);
        exercise = exerciseRepository.save(exercise);
    }

    @Test
    void getAllSessionExercises_ShouldReturnAllSessionExercises() throws Exception {
        // Create a session exercise
        SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSession(session);
        sessionExercise.setExercise(exercise);
        sessionExercise.setOrderNum(1);
        sessionExercise.setNotes("Integration test note");

        // Create the ID
        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(session.getSessionId());
        id.setExerciseId(exercise.getExerciseId());
        sessionExercise.setId(id);

        sessionExerciseRepository.save(sessionExercise);

        // Perform GET request and validate response
        mockMvc.perform(get("/fodboldklub/session-exercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void getSessionExerciseById_ExistingId_ShouldReturnSessionExercise() throws Exception {
        // Create a session exercise
        SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSession(session);
        sessionExercise.setExercise(exercise);
        sessionExercise.setOrderNum(2);
        sessionExercise.setNotes("Get by ID test note");

        // Create the ID
        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(session.getSessionId());
        id.setExerciseId(exercise.getExerciseId());
        sessionExercise.setId(id);

        sessionExerciseRepository.save(sessionExercise);

        // Perform GET request and validate response
        mockMvc.perform(get("/fodboldklub/session-exercises/session/{sessionId}/exercise/{exerciseId}",
                        session.getSessionId(), exercise.getExerciseId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderNum", is(2)))
                .andExpect(jsonPath("$.notes", is("Get by ID test note")));
    }

    @Test
    void createSessionExercise_WithValidData_ShouldCreateSessionExercise() throws Exception {
        // Create session exercise request data
        SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSession(session);
        sessionExercise.setExercise(exercise);
        sessionExercise.setOrderNum(3);
        sessionExercise.setNotes("Create test note");

        // Create the ID
        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(session.getSessionId());
        id.setExerciseId(exercise.getExerciseId());
        sessionExercise.setId(id);

        // Perform POST request and validate response
        mockMvc.perform(post("/fodboldklub/session-exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionExercise)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNum", is(3)))
                .andExpect(jsonPath("$.notes", is("Create test note")));
    }

    @Test
    void updateSessionExercise_WithValidData_ShouldUpdateSessionExercise() throws Exception {
        // Create a session exercise
        SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSession(session);
        sessionExercise.setExercise(exercise);
        sessionExercise.setOrderNum(4);
        sessionExercise.setNotes("Original note");

        // Create the ID
        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(session.getSessionId());
        id.setExerciseId(exercise.getExerciseId());
        sessionExercise.setId(id);

        sessionExerciseRepository.save(sessionExercise);

        // Update data
        sessionExercise.setOrderNum(5);
        sessionExercise.setNotes("Updated note");

        // Perform PUT request and validate response
        mockMvc.perform(put("/fodboldklub/session-exercises/session/{sessionId}/exercise/{exerciseId}",
                        session.getSessionId(), exercise.getExerciseId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionExercise)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNum", is(5)))
                .andExpect(jsonPath("$.notes", is("Updated note")));
    }

    @Test
    void deleteSessionExercise_ExistingId_ShouldDeleteSessionExercise() throws Exception {
        // Create a session exercise
        SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSession(session);
        sessionExercise.setExercise(exercise);
        sessionExercise.setOrderNum(6);
        sessionExercise.setNotes("Delete test note");

        // Create the ID
        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(session.getSessionId());
        id.setExerciseId(exercise.getExerciseId());
        sessionExercise.setId(id);

        sessionExerciseRepository.save(sessionExercise);

        // Perform DELETE request and validate response
        mockMvc.perform(delete("/fodboldklub/session-exercises/session/{sessionId}/exercise/{exerciseId}",
                        session.getSessionId(), exercise.getExerciseId()))
                .andExpect(status().isNoContent());

        // Verify the session exercise no longer exists
        mockMvc.perform(get("/fodboldklub/session-exercises/session/{sessionId}/exercise/{exerciseId}",
                        session.getSessionId(), exercise.getExerciseId()))
                .andExpect(status().isNotFound());
    }
}