package org.example.eksamensprojekt3sem.Exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ExerciseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void getAllExercises_ShouldReturnAllExercises() throws Exception {
        // Create test exercise data
        Exercise exercise = new Exercise();
        exercise.setName("Integration Test Exercise");
        exercise.setDescription("Exercise for integration tests");
        exercise.setDuration(20);
        exerciseRepository.save(exercise);

        // Perform GET request and validate response
        mockMvc.perform(get("/fodboldklub/exercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Integration Test Exercise")));
    }

    @Test
    void getExerciseById_ExistingId_ShouldReturnExercise() throws Exception {
        // Create test exercise data
        Exercise exercise = new Exercise();
        exercise.setName("Exercise by ID");
        exercise.setDescription("Get by ID test");
        exercise.setDuration(15);
        Exercise savedExercise = exerciseRepository.save(exercise);

        // Perform GET request and validate response
        mockMvc.perform(get("/fodboldklub/exercises/{id}", savedExercise.getExerciseId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Exercise by ID")))
                .andExpect(jsonPath("$.description", is("Get by ID test")))
                .andExpect(jsonPath("$.duration", is(15)));
    }

    @Test
    void getExerciseById_NonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/fodboldklub/exercises/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createExercise_WithValidData_ShouldCreateExercise() throws Exception {
        // Create test exercise data
        Exercise exercise = new Exercise();
        exercise.setName("New Exercise");
        exercise.setDescription("Created through API");
        exercise.setDuration(25);

        // Perform POST request and validate response
        mockMvc.perform(post("/fodboldklub/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exercise)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Exercise")))
                .andExpect(jsonPath("$.description", is("Created through API")))
                .andExpect(jsonPath("$.duration", is(25)));
    }

    @Test
    void createExercise_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Create invalid exercise data (missing required fields)
        Exercise exercise = new Exercise();
        // Missing name and description

        // Perform POST request and validate response
        mockMvc.perform(post("/fodboldklub/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exercise)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateExercise_WithValidData_ShouldUpdateExercise() throws Exception {
        // Create test exercise data
        Exercise exercise = new Exercise();
        exercise.setName("Exercise to Update");
        exercise.setDescription("Original description");
        exercise.setDuration(30);
        Exercise savedExercise = exerciseRepository.save(exercise);

        // Update data
        savedExercise.setName("Updated Exercise");
        savedExercise.setDescription("Updated description");
        savedExercise.setDuration(35);

        // Perform PUT request and validate response
        mockMvc.perform(put("/fodboldklub/exercises/{id}", savedExercise.getExerciseId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedExercise)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Exercise")))
                .andExpect(jsonPath("$.description", is("Updated description")))
                .andExpect(jsonPath("$.duration", is(35)));
    }

    @Test
    void deleteExercise_ExistingId_ShouldDeleteExercise() throws Exception {
        // Create test exercise data
        Exercise exercise = new Exercise();
        exercise.setName("Exercise to Delete");
        exercise.setDescription("Will be deleted");
        exercise.setDuration(10);
        Exercise savedExercise = exerciseRepository.save(exercise);

        // Perform DELETE request and validate response
        mockMvc.perform(delete("/fodboldklub/exercises/{id}", savedExercise.getExerciseId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted", is(true)));
    }
}