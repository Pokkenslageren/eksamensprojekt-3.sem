package org.example.eksamensprojekt3sem.Exercise;

 // import org.example.eksamensprojekt3sem.entity.SessionExercise;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @GetMapping("/{id}")
    public Exercise getExerciseById(@PathVariable Long id) {
        return exerciseService.getExerciseById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<Exercise> createExercise(@Valid @RequestBody Exercise exercise) {
        Exercise savedExercise = exerciseService.createExercise(exercise);
        return new ResponseEntity<>(savedExercise, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable Long id, @Valid @RequestBody Exercise exerciseDetails) {
        try {
            Exercise updatedExercise = exerciseService.updateExercise(id, exerciseDetails);
            return ResponseEntity.ok(updatedExercise);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteExercise(@PathVariable Long id) {
        try {
            boolean deleted = exerciseService.deleteExercise(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("deleted", true));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with id: " + id);
            }
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<Exercise> searchExercises(@RequestParam String name) {
        return exerciseService.searchExercisesByName(name);
    }

    @GetMapping("/duration/{maxDuration}")
    public List<Exercise> getExercisesByMaxDuration(@PathVariable int maxDuration) {
        return exerciseService.getExercisesByMaxDuration(maxDuration);
    }

    @GetMapping("/most-used")
    public List<Exercise> getMostUsedExercises() {
        return exerciseService.getMostUsedExercises();
    }

    @GetMapping("/{id}/sessions")
    public List<SessionExercise> getSessionsUsingExercise(@PathVariable Long id) {
        // Validate exercise exists
        if (!exerciseService.getExerciseById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with id: " + id);
        }

        return exerciseService.getSessionsUsingExercise(id);
    }
}
