package org.example.eksamensprojekt3sem.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private Team team;
    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    void setUp() {
        // Create a team for testing
        team = new Team();
        team.setName("Test Team");
        team.setDescription("Team for integration tests");
        team.setActive(true);
        team = teamRepository.save(team);

        // Create exercises for testing
        exercise1 = new Exercise();
        exercise1.setName("Test Exercise 1");
        exercise1.setDescription("Exercise for integration tests");
        exercise1.setDuration(15);
        exercise1 = exerciseRepository.save(exercise1);

        exercise2 = new Exercise();
        exercise2.setName("Test Exercise 2");
        exercise2.setDescription("Another exercise for integration tests");
        exercise2.setDuration(20);
        exercise2 = exerciseRepository.save(exercise2);
    }

    @Test
    void getAllSessions_ShouldReturnAllSessions() throws Exception {
        // Create test session
        Session session = new Session();
        session.setTeam(team);
        session.setDateTime(LocalDateTime.now().plusDays(1));
        session.setLocation("Test Location");
        sessionRepository.save(session);

        // Perform GET request and validate response
        mockMvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].location", hasItem("Test Location")));
    }

    @Test
    void getSessionById_ExistingId_ShouldReturnSession() throws Exception {
        // Create test session
        Session session = new Session();
        session.setTeam(team);
        session.setDateTime(LocalDateTime.now().plusDays(2));
        session.setLocation("Session By ID");
        Session savedSession = sessionRepository.save(session);

        // Perform GET request and validate response
        mockMvc.perform(get("/sessions/{id}", savedSession.getSessionId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location", is("Session By ID")));
    }

    @Test
    void createSession_WithValidData_ShouldCreateSession() throws Exception {
        // Create session request data
        LocalDateTime dateTime = LocalDateTime.now().plusDays(3);

        String requestBody = "{\n" +
                "  \"session\": {\n" +
                "    \"dateTime\": \"" + dateTime + "\",\n" +
                "    \"location\": \"New Session Location\",\n" +
                "    \"team\": {\n" +
                "      \"teamId\": " + team.getTeamId() + "\n" +
                "    }\n" +
                "  },\n" +
                "  \"exercises\": [\n" +
                "    {\n" +
                "      \"exerciseId\": " + exercise1.getExerciseId() + ",\n" +
                "      \"orderNum\": 1,\n" +
                "      \"notes\": \"First exercise note\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"exerciseId\": " + exercise2.getExerciseId() + ",\n" +
                "      \"orderNum\": 2,\n" +
                "      \"notes\": \"Second exercise note\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Perform POST request and validate response
        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location", is("New Session Location")))
                .andExpect(jsonPath("$.team.teamId", is(team.getTeamId())))
                .andExpect(jsonPath("$.exercises", hasSize(2)));
    }

    @Test
    void deleteSession_ExistingId_ShouldDeleteSession() throws Exception {
        // Create test session
        Session session = new Session();
        session.setTeam(team);
        session.setDateTime(LocalDateTime.now().plusDays(4));
        session.setLocation("Session To Delete");
        Session savedSession = sessionRepository.save(session);

        // Perform DELETE request and validate response
        mockMvc.perform(delete("/sessions/{id}", savedSession.getSessionId()))
                .andExpect(status().isNoContent());

        // Verify the session no longer exists
        mockMvc.perform(get("/sessions/{id}", savedSession.getSessionId()))
                .andExpect(status().isNotFound());
    }
}