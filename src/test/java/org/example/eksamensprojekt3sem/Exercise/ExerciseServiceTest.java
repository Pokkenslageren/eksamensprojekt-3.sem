package org.example.eksamensprojekt3sem.Exercise;

import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private SessionExerciseRepository sessionExerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    void setUp() {
        // Setup test data
        exercise1 = new Exercise();
        exercise1.setExerciseId(1L);
        exercise1.setName("Test Exercise 1");
        exercise1.setDescription("Test Description 1");
        exercise1.setDuration(10);

        exercise2 = new Exercise();
        exercise2.setExerciseId(2L);
        exercise2.setName("Test Exercise 2");
        exercise2.setDescription("Test Description 2");
        exercise2.setDuration(15);
    }

    @Test
    void getAllExercises_ShouldReturnAllExercises() {
        // Arrange
        when(exerciseRepository.findAll()).thenReturn(Arrays.asList(exercise1, exercise2));

        // Act
        List<Exercise> result = exerciseService.getAllExercises();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Exercise 1", result.get(0).getName());
        assertEquals("Test Exercise 2", result.get(1).getName());
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    void getExerciseById_ExistingId_ShouldReturnExercise() {
        // Arrange
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));

        // Act
        Optional<Exercise> result = exerciseService.getExerciseById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Exercise 1", result.get().getName());
        verify(exerciseRepository, times(1)).findById(1L);
    }

    @Test
    void getExerciseById_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(exerciseRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Exercise> result = exerciseService.getExerciseById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(exerciseRepository, times(1)).findById(3L);
    }

    @Test
    void createExercise_ShouldSaveAndReturnExercise() {
        // Arrange
        Exercise newExercise = new Exercise();
        newExercise.setName("New Exercise");
        newExercise.setDescription("New Description");
        newExercise.setDuration(20);

        when(exerciseRepository.save(any(Exercise.class))).thenReturn(newExercise);

        // Act
        Exercise result = exerciseService.createExercise(newExercise);

        // Assert
        assertEquals("New Exercise", result.getName());
        verify(exerciseRepository, times(1)).save(newExercise);
    }

    @Test
    void updateExercise_ExistingId_ShouldUpdateAndReturnExercise() {
        // Arrange
        Exercise updatedExercise = new Exercise();
        updatedExercise.setName("Updated Exercise");
        updatedExercise.setDescription("Updated Description");
        updatedExercise.setDuration(25);

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));
        when(exerciseRepository.save(any(Exercise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Exercise result = exerciseService.updateExercise(1L, updatedExercise);

        // Assert
        assertEquals("Updated Exercise", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(25, result.getDuration());
        verify(exerciseRepository, times(1)).findById(1L);
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    void updateExercise_NonExistingId_ShouldThrowException() {
        // Arrange
        Exercise updatedExercise = new Exercise();
        when(exerciseRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> exerciseService.updateExercise(3L, updatedExercise));
        verify(exerciseRepository, times(1)).findById(3L);
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    void deleteExercise_ExistingIdWithNoSessions_ShouldDeleteAndReturnTrue() {
        // Arrange
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));
        when(sessionExerciseRepository.findByExerciseExerciseId(1L)).thenReturn(Collections.emptyList());

        // Act
        boolean result = exerciseService.deleteExercise(1L);

        // Assert
        assertTrue(result);
        verify(exerciseRepository, times(1)).findById(1L);
        verify(sessionExerciseRepository, times(1)).findByExerciseExerciseId(1L);
        verify(exerciseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExercise_ExistingIdWithSessions_ShouldThrowException() {
        // Arrange
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));
        when(sessionExerciseRepository.findByExerciseExerciseId(1L)).thenReturn(
                Collections.singletonList(mock(SessionExercise.class)));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> exerciseService.deleteExercise(1L));
        verify(exerciseRepository, times(1)).findById(1L);
        verify(sessionExerciseRepository, times(1)).findByExerciseExerciseId(1L);
        verify(exerciseRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteExercise_NonExistingId_ShouldReturnFalse() {
        // Arrange
        when(exerciseRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        boolean result = exerciseService.deleteExercise(3L);

        // Assert
        assertFalse(result);
        verify(exerciseRepository, times(1)).findById(3L);
        verify(exerciseRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchExercisesByName_ShouldReturnMatchingExercises() {
        // Arrange
        when(exerciseRepository.findByNameContainingIgnoreCase("Test")).thenReturn(
                Arrays.asList(exercise1, exercise2));

        // Act
        List<Exercise> result = exerciseService.searchExercisesByName("Test");

        // Assert
        assertEquals(2, result.size());
        verify(exerciseRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }

    @Test
    void getExercisesByMaxDuration_ShouldReturnExercisesWithinDuration() {
        // Arrange
        when(exerciseRepository.findByDurationLessThanEqual(15)).thenReturn(
                Arrays.asList(exercise1, exercise2));

        // Act
        List<Exercise> result = exerciseService.getExercisesByMaxDuration(15);

        // Assert
        assertEquals(2, result.size());
        verify(exerciseRepository, times(1)).findByDurationLessThanEqual(15);
    }

    @Test
    void getMostUsedExercises_ShouldReturnMostUsedExercises() {
        // Arrange
        when(exerciseRepository.findMostUsedExercises()).thenReturn(
                Collections.singletonList(exercise1));

        // Act
        List<Exercise> result = exerciseService.getMostUsedExercises();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Exercise 1", result.get(0).getName());
        verify(exerciseRepository, times(1)).findMostUsedExercises();
    }

    @Test
    void getSessionsUsingExercise_ShouldReturnSessionsUsingExercise() {
        // Arrange
        SessionExercise sessionExercise = mock(SessionExercise.class);
        when(sessionExerciseRepository.findByExerciseExerciseId(1L)).thenReturn(
                Collections.singletonList(sessionExercise));

        // Act
        List<SessionExercise> result = exerciseService.getSessionsUsingExercise(1L);

        // Assert
        assertEquals(1, result.size());
        verify(sessionExerciseRepository, times(1)).findByExerciseExerciseId(1L);
    }
}