package org.example.eksamensprojekt3sem.Session;

import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseId;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseRepository;
import org.example.eksamensprojekt3sem.Team.Team;
import org.example.eksamensprojekt3sem.Team.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionExerciseRepository sessionExerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session1;
    private Session session2;
    private Team team;
    private Exercise exercise;
    private SessionService.SessionExerciseDTO exerciseDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        team = new Team();
        team.setTeamId(1L);
        team.setName("Test Team");

        session1 = new Session();
        session1.setSessionId(1L);
        session1.setTeam(team);
        session1.setDateTime(LocalDateTime.now());
        session1.setLocation("Test Location 1");

        session2 = new Session();
        session2.setSessionId(2L);
        session2.setTeam(team);
        session2.setDateTime(LocalDateTime.now().plusDays(1));
        session2.setLocation("Test Location 2");

        exercise = new Exercise();
        exercise.setExerciseId(1L);
        exercise.setName("Test Exercise");

        exerciseDTO = new SessionService.SessionExerciseDTO();
        exerciseDTO.setExerciseId(1L);
        exerciseDTO.setOrderNum(1);
        exerciseDTO.setNotes("Test notes");
    }

    @Test
    void getAllSessions_ShouldReturnAllSessions() {
        // Arrange
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session1, session2));

        // Act
        List<Session> result = sessionService.getAllSessions();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Location 1", result.get(0).getLocation());
        assertEquals("Test Location 2", result.get(1).getLocation());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void getSessionById_ExistingId_ShouldReturnSession() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));

        // Act
        Optional<Session> result = sessionService.getSessionById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Location 1", result.get().getLocation());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void getSessionById_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(sessionRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Session> result = sessionService.getSessionById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(sessionRepository, times(1)).findById(3L);
    }

    @Test
    void createSession_WithExercises_ShouldCreateSessionAndExercises() {
        // Arrange
        List<SessionService.SessionExerciseDTO> exerciseDTOs = new ArrayList<>();
        exerciseDTOs.add(exerciseDTO);

        when(sessionRepository.save(any(Session.class))).thenReturn(session1);
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));

        // Act
        Session result = sessionService.createSession(session1, exerciseDTOs);

        // Assert
        assertEquals("Test Location 1", result.getLocation());
        verify(sessionRepository, times(1)).save(session1);
        verify(exerciseRepository, times(1)).findById(1L);
        verify(sessionExerciseRepository, times(1)).save(any(SessionExercise.class));
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void updateSession_ExistingIdWithExercises_ShouldUpdateSessionAndExercises() {
        // Arrange
        List<SessionService.SessionExerciseDTO> exerciseDTOs = new ArrayList<>();
        exerciseDTOs.add(exerciseDTO);

        Session updatedSession = new Session();
        updatedSession.setLocation("Updated Location");
        updatedSession.setDateTime(LocalDateTime.now().plusDays(2));
        updatedSession.setTeam(team);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        when(sessionRepository.save(any(Session.class))).thenReturn(session1);
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        // Act
        Session result = sessionService.updateSession(1L, updatedSession, exerciseDTOs);

        // Assert
        assertNotNull(result);
        verify(sessionRepository, times(2)).findById(1L); // Once for the update, once for retrieval
        verify(sessionRepository, times(1)).save(any(Session.class));
        verify(exerciseRepository, times(1)).findById(1L);
        verify(sessionExerciseRepository, times(1)).save(any(SessionExercise.class));
    }

    @Test
    void deleteSession_ExistingId_ShouldDeleteSessionAndExercises() {
        // Arrange
        Session sessionWithExercises = new Session();
        sessionWithExercises.setSessionId(1L);
        sessionWithExercises.setSessionExercises(new ArrayList<>());
        sessionWithExercises.getSessionExercises().add(mock(SessionExercise.class));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(sessionWithExercises));

        // Act
        sessionService.deleteSession(1L);

        // Assert
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionExerciseRepository, times(1)).deleteAll(anyList());
        verify(sessionRepository, times(1)).delete(sessionWithExercises);
    }

    @Test
    void deleteSession_NonExistingId_ShouldThrowException() {
        // Arrange
        when(sessionRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sessionService.deleteSession(3L));
        verify(sessionRepository, times(1)).findById(3L);
        verify(sessionRepository, never()).delete(any(Session.class));
    }
}