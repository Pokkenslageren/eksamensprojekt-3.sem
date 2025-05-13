package org.example.eksamensprojekt3sem.SessionExercise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionExerciseRepository extends JpaRepository<SessionExercise, SessionExerciseId> {
    List<SessionExercise> findByExerciseExerciseId(Long id);
}
