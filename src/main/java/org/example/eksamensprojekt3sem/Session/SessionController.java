package org.example.eksamensprojekt3sem.Session;

import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseId;
import org.example.eksamensprojekt3sem.Team.Team;
import org.example.eksamensprojekt3sem.Team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "http://localhost:8081")
public class SessionController {

    private final SessionService sessionService;
    private final TeamRepository teamRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public SessionController(SessionService sessionService, TeamRepository teamRepository, ExerciseRepository exerciseRepository) {
        this.sessionService = sessionService;
        this.teamRepository = teamRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        List<SessionDTO> sessionDTOs = sessions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sessionDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        return sessionService.getSessionById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionCreationRequest request) {
        try {
            // Convert the request DTO to entities
            Session session = new Session();
            session.setDateTime(request.getSession().getDateTime());
            session.setLocation(request.getSession().getLocation());

            Team team = teamRepository.findById(request.getSession().getTeam().getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));

            session.setTeam(team);

            List<SessionService.SessionExerciseDTO> exerciseDTOs = new ArrayList<>();

            for (SessionExerciseDTO dto : request.getExercises()) {
                exerciseRepository.findById(dto.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + dto.getExerciseId()));

                SessionService.SessionExerciseDTO serviceDto = new SessionService.SessionExerciseDTO();
                serviceDto.setExerciseId(dto.getExerciseId());
                serviceDto.setOrderNum(dto.getOrderNum());
                serviceDto.setNotes(dto.getNotes());

                exerciseDTOs.add(serviceDto);
            }

            Session createdSession = sessionService.createSession(session, exerciseDTOs);

            // Convert back to DTO for response
            return ResponseEntity.ok(convertToDTO(createdSession));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession(@PathVariable Long id, @RequestBody SessionCreationRequest request) {
        try {
            Session existingSession = sessionService.getSessionById(id)
                    .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));

            existingSession.setDateTime(request.getSession().getDateTime());
            existingSession.setLocation(request.getSession().getLocation());

            if (request.getSession().getTeam() != null) {
                Team team = teamRepository.findById(request.getSession().getTeam().getTeamId())
                        .orElseThrow(() -> new RuntimeException("Team not found"));

                existingSession.setTeam(team);
            }

            List<SessionService.SessionExerciseDTO> exerciseDTOs = new ArrayList<>();

            for (SessionExerciseDTO dto : request.getExercises()) {

                exerciseRepository.findById(dto.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + dto.getExerciseId()));

                SessionService.SessionExerciseDTO serviceDto = new SessionService.SessionExerciseDTO();
                serviceDto.setExerciseId(dto.getExerciseId());
                serviceDto.setOrderNum(dto.getOrderNum());
                serviceDto.setNotes(dto.getNotes());

                exerciseDTOs.add(serviceDto);
            }

            Session updatedSession = sessionService.updateSession(id, existingSession, exerciseDTOs);

            return ResponseEntity.ok(convertToDTO(updatedSession));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Long id) {
        try {
            sessionService.deleteSession(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private SessionDTO convertToDTO(Session session) {
        SessionDTO dto = new SessionDTO();
        dto.setSessionId(session.getSessionId());
        dto.setDateTime(session.getDateTime());
        dto.setLocation(session.getLocation());

        if (session.getTeam() != null) {
            TeamDTO teamDTO = new TeamDTO();
            teamDTO.setTeamId(session.getTeam().getTeamId());
            teamDTO.setName(session.getTeam().getName());
            dto.setTeam(teamDTO);
        }

        if (session.getSessionExercises() != null) {
            List<SessionExerciseDTO> exerciseDTOs = session.getSessionExercises().stream()
                    .map(se -> {
                        SessionExerciseDTO seDTO = new SessionExerciseDTO();
                        seDTO.setExerciseId(se.getExercise().getExerciseId());
                        seDTO.setOrderNum(se.getOrderNum());
                        seDTO.setNotes(se.getNotes());
                        return seDTO;
                    })
                    .collect(Collectors.toList());

            dto.setExercises(exerciseDTOs);
        }

        return dto;
    }

    // DTO classes
    public static class TeamDTO {
        private Long teamId;
        private String name;

        // Getters and Setters
        public Long getTeamId() {
            return teamId;
        }

        public void setTeamId(Long teamId) {
            this.teamId = teamId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TeamRefDTO {
        private Long teamId;

        // Getters and Setters
        public Long getTeamId() {
            return teamId;
        }

        public void setTeamId(Long teamId) {
            this.teamId = teamId;
        }
    }

    public static class SessionExerciseDTO {
        private Long exerciseId;
        private Integer orderNum;
        private String notes;

        // Getters and Setters
        public Long getExerciseId() {
            return exerciseId;
        }

        public void setExerciseId(Long exerciseId) {
            this.exerciseId = exerciseId;
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

    public static class SessionCreateDTO {
        private LocalDateTime dateTime;
        private String location;
        private TeamRefDTO team;

        // Getters and Setters
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

        public TeamRefDTO getTeam() {
            return team;
        }

        public void setTeam(TeamRefDTO team) {
            this.team = team;
        }
    }

    public static class SessionDTO {
        private Long sessionId;
        private LocalDateTime dateTime;
        private String location;
        private TeamDTO team;
        private List<SessionExerciseDTO> exercises;

        // Getters and Setters
        public Long getSessionId() {
            return sessionId;
        }

        public void setSessionId(Long sessionId) {
            this.sessionId = sessionId;
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

        public TeamDTO getTeam() {
            return team;
        }

        public void setTeam(TeamDTO team) {
            this.team = team;
        }

        public List<SessionExerciseDTO> getExercises() {
            return exercises;
        }

        public void setExercises(List<SessionExerciseDTO> exercises) {
            this.exercises = exercises;
        }
    }

    public static class SessionCreationRequest {
        private SessionCreateDTO session;
        private List<SessionExerciseDTO> exercises;

        // Getters and Setters
        public SessionCreateDTO getSession() {
            return session;
        }

        public void setSession(SessionCreateDTO session) {
            this.session = session;
        }

        public List<SessionExerciseDTO> getExercises() {
            return exercises;
        }

        public void setExercises(List<SessionExerciseDTO> exercises) {
            this.exercises = exercises;
        }
    }
}