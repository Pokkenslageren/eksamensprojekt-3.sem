package org.example.eksamensprojekt3sem.Session;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.*;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.Team.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @JsonBackReference
    @NotNull(message = "Team skal udfyldes")
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull(message = "Sessionsdato skal udfyldes")
    @Future(message = "Sessionsdato skal være i fremtiden")
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @NotBlank(message = "Lokation skal udfyldes")
    @Column(name = "location")
    private String location;

    @JsonManagedReference
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<SessionExercise> sessionExercises;

    protected Session() {}

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<SessionExercise> getSessionExercises() {
        return sessionExercises;
    }

    public void setSessionExercises(List<SessionExercise> sessionExercises) {
        this.sessionExercises = sessionExercises;
    }
}
