package org.example.eksamensprojekt3sem.Team;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "teams")
public class TeamModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private long teamId;

    @Column(name = "name")
    @NotBlank(message = "Navn mangler")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Beskrivelse mangler")
    private String description;

    @Column(name = "active")
    @NotBlank(message = "Aktivitet mangler")
    private boolean active;

    public TeamModel() {
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
