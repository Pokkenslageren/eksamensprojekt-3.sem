package org.example.eksamensprojekt3sem.Exercise;

import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing exercises
 */
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final SessionExerciseRepository sessionExerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, SessionExerciseRepository sessionExerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.sessionExerciseRepository = sessionExerciseRepository;
    }

    /**
     * Get all exercises
     * @return List of all exercises
     */
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    /**
     * Get exercise by ID
     * @param id Exercise ID
     * @return Optional containing the exercise if found
     */
    public Optional<Exercise> getExerciseById(Long id) {
        return exerciseRepository.findById(id);
    }

    /**
     * Create a new exercise
     * @param exercise Exercise to create
     * @return Created exercise
     */
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    /**
     * Update an existing exercise
     * @param id Exercise ID
     * @param exerciseDetails Updated exercise details
     * @return Updated exercise
     * @throws RuntimeException if exercise not found
     */
    public Exercise updateExercise(Long id, Exercise exerciseDetails) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id " + id));

        exercise.setName(exerciseDetails.getName());
        exercise.setDescription(exerciseDetails.getDescription());
        exercise.setDuration(exerciseDetails.getDuration());

        return exerciseRepository.save(exercise);
    }

    /**
     * Delete an exercise
     * @param id Exercise ID
     * @return true if deleted, false if not found
     * @throws RuntimeException if exercise is used in sessions
     */
    @Transactional
    public boolean deleteExercise(Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        if (exercise.isPresent()) {
            // Check if exercise is used in any sessions
            List<SessionExercise> sessionExercises = sessionExerciseRepository.findByExerciseExerciseId(id);
            if (!sessionExercises.isEmpty()) {
                throw new RuntimeException("Cannot delete exercise used in sessions");
            }

            exerciseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Search exercises by name
     * @param name Name to search for
     * @return List of matching exercises
     */
    public List<Exercise> searchExercisesByName(String name) {
        return exerciseRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Get exercises with duration less than or equal to maxDuration
     * @param maxDuration Maximum duration in minutes
     * @return List of matching exercises
     */
    public List<Exercise> getExercisesByMaxDuration(int maxDuration) {
        return exerciseRepository.findByDurationLessThanEqual(maxDuration);
    }

    /**
     * Get most used exercises
     * @return List of exercises ordered by usage frequency
     */
    public List<Exercise> getMostUsedExercises() {
        return exerciseRepository.findMostUsedExercises();
    }

    /**
     * Get sessions using a specific exercise
     * @param exerciseId Exercise ID
     * @return List of session exercises
     */
    public List<SessionExercise> getSessionsUsingExercise(Long exerciseId) {
        return sessionExerciseRepository.findByExerciseExerciseId(exerciseId);
    }
}