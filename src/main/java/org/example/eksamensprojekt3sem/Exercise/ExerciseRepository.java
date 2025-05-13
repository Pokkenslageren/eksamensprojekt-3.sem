
package org.example.eksamensprojekt3sem.Exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByNameContainingIgnoreCase(String name);

    List<Exercise> findByDurationLessThanEqual(int maxDuration);

    @Query("SELECT e FROM Exercise e ORDER BY SIZE(e.sessionExercises) DESC")
    List<Exercise> findMostUsedExercises();
}