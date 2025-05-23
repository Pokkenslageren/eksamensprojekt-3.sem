package org.example.eksamensprojekt3sem.SessionExercise;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.eksamensprojekt3sem.Session.Session;
import org.example.eksamensprojekt3sem.Exercise.Exercise;

@Entity
@Table(name = "session_exercise")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class SessionExercise {

    @EmbeddedId
    private SessionExerciseId id = new SessionExerciseId();

    @NotNull(message = "Session skal udfyldes")
    @ManyToOne
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private Session session;

    @NotNull(message = "Øvelse skal udfyldes")
    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @NotNull(message = "Rækkefølge skal udfyldes")
    @Min(value = 1, message = "Order number must be at least 1")
    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "notes")
    private String notes;

    public SessionExercise() {}

    public SessionExercise(Session session, Exercise exercise, Integer orderNum, String notes) {
        this.session = session;
        this.exercise = exercise;
        this.orderNum = orderNum;
        this.notes = notes;

        //SETUP FOR Composite key!
        this.id = new SessionExerciseId();
        if (session != null && session.getSessionId() != null) {
            this.id.setSessionId(session.getSessionId());
        }
        if (exercise != null && exercise.getExerciseId() != null) {
            this.id.setExerciseId(exercise.getExerciseId());
        }
    }

    @Override
    public String toString() {
        return "SessionExercise{" +
                "id=" + id +
                ", session=" + session +
                ", exercise=" + exercise +
                ", orderNum=" + orderNum +
                ", notes='" + notes + '\'' +
                '}';
    }

    public SessionExerciseId getId() {
        return id;
    }

    public void setId(SessionExerciseId id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}