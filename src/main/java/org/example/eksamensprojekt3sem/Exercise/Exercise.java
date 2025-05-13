package org.example.eksamensprojekt3sem.Exercise;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;

import java.util.*;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long exerciseId;

    @NotBlank(message = "Navn skal udfyldes")
    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 1000)
    @NotBlank(message = "Beskrivelse skal udfyldes")
    private String description;

    @Column(name = "duration")
    @PositiveOrZero
    @NotBlank(message = "Varighed skal udfyldes")
    private int duration;

    @OneToMany(mappedBy = "exercise")
    private List<SessionExercise> sessionExercises = new ArrayList<>();

    // Constructors
    public Exercise() {
    }

    public Exercise(String name, String description, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    // Getters and Setters
    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<SessionExercise> getSessionExercises() {
        return sessionExercises;
    }

    public void setSessionExercises(List<SessionExercise> sessionExercises) {
        this.sessionExercises = sessionExercises;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseId=" + exerciseId +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}