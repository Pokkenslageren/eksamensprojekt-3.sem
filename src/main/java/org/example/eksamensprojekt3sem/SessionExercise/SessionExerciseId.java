package org.example.eksamensprojekt3sem.SessionExercise;


import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SessionExerciseId implements Serializable {

    private Long sessionId;
    private Long exerciseId;

    public SessionExerciseId() {}

    public SessionExerciseId(Long sessionId, Long exerciseId) {
        this.sessionId = sessionId;
        this.exerciseId = exerciseId;
    }

    //NB! læs op på
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionExerciseId that)) return false;
        return Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(exerciseId, that.exerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, exerciseId);
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
}