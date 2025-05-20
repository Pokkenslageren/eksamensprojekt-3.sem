package org.example.eksamensprojekt3sem.config;

import jakarta.annotation.PostConstruct;
import org.example.eksamensprojekt3sem.Enums.MembershipType;
import org.example.eksamensprojekt3sem.Enums.PaymentStatus;
import org.example.eksamensprojekt3sem.Enums.UserRole;
import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
import org.example.eksamensprojekt3sem.Member.Member;
import org.example.eksamensprojekt3sem.Member.MemberRepository;
import org.example.eksamensprojekt3sem.Membership.Membership;
import org.example.eksamensprojekt3sem.Membership.MembershipRepository;
import org.example.eksamensprojekt3sem.Session.Session;
import org.example.eksamensprojekt3sem.Session.SessionRepository;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExercise;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseId;
import org.example.eksamensprojekt3sem.SessionExercise.SessionExerciseRepository;
import org.example.eksamensprojekt3sem.Team.Team;
import org.example.eksamensprojekt3sem.Team.TeamRepository;
import org.example.eksamensprojekt3sem.User.User;
import org.example.eksamensprojekt3sem.User.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Component
@Profile("dev")
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final TeamRepository teamRepository;
    private final ExerciseRepository exerciseRepository;
    private final SessionRepository sessionRepository;
    private final SessionExerciseRepository sessionExerciseRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(TeamRepository teamRepository,
                           ExerciseRepository exerciseRepository,
                           SessionRepository sessionRepository,
                           SessionExerciseRepository sessionExerciseRepository,
                           MemberRepository memberRepository,
                           MembershipRepository membershipRepository,
                           UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.exerciseRepository = exerciseRepository;
        this.sessionRepository = sessionRepository;
        this.sessionExerciseRepository = sessionExerciseRepository;
        this.memberRepository = memberRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    @Transactional
    public void initData() {
        logger.info("Starting data initialization...");

        try {
            // Check if data already exists
            if (teamRepository.count() > 0) {
                logger.info("Data already initialized, skipping...");
                return;
            }

            // Create team (just one as per requirement)
            Team team = new Team();
            team.setName("Farum Cats");
            team.setDescription("Australsk fodboldklub i Farum");
            team.setActive(true);

            team = teamRepository.save(team);
            logger.info("Team created: {}", team.getName());

            // Create users
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin"); // admin123
            admin.setUserRole(UserRole.ADMIN);

            User chairman = new User();
            chairman.setUsername("chairman");
            chairman.setPassword("chairman"); // admin123
            chairman.setUserRole(UserRole.CHAIRMAN);

            User trainer = new User();
            trainer.setUsername("trainer");
            trainer.setPassword("trainer"); // admin123
            trainer.setUserRole(UserRole.TRAINER);

            userRepository.save(admin);
            userRepository.save(chairman);
            userRepository.save(trainer);
            logger.info("Users created");

            // Create some members
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Member member1 = createMember("Anders Jensen", "anders@example.com", "12345678", "Farum Hovedgade 10", sdf.parse("1990-05-15"), PaymentStatus.PAID);
            Member member2 = createMember("Marie Nielsen", "marie@example.com", "23456789", "Stavnsholtvej 25", sdf.parse("1995-08-22"), PaymentStatus.PAID);
            Member member3 = createMember("Peter Sørensen", "peter@example.com", "34567890", "Ganløsevej 45", sdf.parse("1985-12-03"), PaymentStatus.UNPAID);
            Member member4 = createMember("Sofie Andersen", "sofie@example.com", "45678901", "Paltholmvej 12", sdf.parse("1992-04-17"), PaymentStatus.PAID);
            Member member5 = createMember("Jens Hansen", "jens@example.com", "56789012", "Farumvej 78", sdf.parse("1988-02-28"), PaymentStatus.PARTIALLY_PAID);

            logger.info("Members created");

            // Create memberships
            createMembership(member1.getMemberId(), MembershipType.SENIOR);
            createMembership(member2.getMemberId(), MembershipType.SENIOR);
            createMembership(member3.getMemberId(), MembershipType.SENIOR);
            createMembership(member4.getMemberId(), MembershipType.JUNIOR);
            createMembership(member5.getMemberId(), MembershipType.PASSIVE);

            logger.info("Memberships created");

            // Create exercises
            Exercise exercise1 = createExercise("Opvarmning", "Grundig opvarmning med fokus på bevægelighed", 10);
            Exercise exercise2 = createExercise("5 Star Handball", "Australsk fodbold-inspireret håndboldøvelse", 15);
            Exercise exercise3 = createExercise("Kicking Practice", "Øvelser til at forbedre spark-teknik", 20);
            Exercise exercise4 = createExercise("Marking Drills", "Øvelser til at forbedre gribeteknik", 15);
            Exercise exercise5 = createExercise("Tackling Technique", "Sikker tackling-teknik øvelser", 15);
            Exercise exercise6 = createExercise("Match Simulation", "Spil med fokus på specifikke spilsituationer", 30);
            Exercise exercise7 = createExercise("Cooldown", "Nedkøling og stræk", 10);

            logger.info("Exercises created");

            // Create one past session and one future session
            LocalDateTime pastDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(18, 0));
            Session pastSession = createSession(team, pastDate, "Farum Park");

            LocalDateTime futureDate = LocalDateTime.now().plusDays(3).with(LocalTime.of(18, 0));
            Session futureSession = createSession(team, futureDate, "Farum Park");

            logger.info("Sessions created");

            // Add exercises to past session
            addExerciseToSession(pastSession, exercise1, 1, "Standard opvarmning");
            addExerciseToSession(pastSession, exercise2, 2, "Del holdet i to lige store grupper");
            addExerciseToSession(pastSession, exercise3, 3, "Fokus på teknik frem for kraft");
            addExerciseToSession(pastSession, exercise7, 4, "Grundig udstrækning");

            // Add exercises to future session
            addExerciseToSession(futureSession, exercise1, 1, "Hurtig opvarmning");
            addExerciseToSession(futureSession, exercise4, 2, "Fokus på timing");
            addExerciseToSession(futureSession, exercise5, 3, "Sikkerhed er vigtig");
            addExerciseToSession(futureSession, exercise6, 4, "Fuld bane");
            addExerciseToSession(futureSession, exercise7, 5, "Kort nedkøling");

            logger.info("Session exercises added");

            logger.info("Data initialization completed successfully");

        } catch (Exception e) {
            logger.error("Error during data initialization", e);
        }
    }

    private Member createMember(String name, String email, String phone, String address, Date dateOfBirth, PaymentStatus paymentStatus) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setPhone(phone);
        member.setAddress(address);
        member.setDateOfBirth(dateOfBirth);
        member.setPaymentStatus(paymentStatus);
        return memberRepository.save(member);
    }

    private Membership createMembership(Long memberId, MembershipType type) {
        Membership membership = new Membership();
        membership.setMemberId(memberId);
        membership.setMembershipType(type);
        membership.setStartDate(LocalDate.now().minusMonths(2));
        membership.setEndDate(LocalDate.now().plusMonths(10));
        return membershipRepository.save(membership);
    }

    private Exercise createExercise(String name, String description, int duration) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setDescription(description);
        exercise.setDuration(duration);
        return exerciseRepository.save(exercise);
    }

    private Session createSession(Team team, LocalDateTime dateTime, String location) {
        Session session = new Session();
        session.setTeam(team);
        session.setDateTime(dateTime);
        session.setLocation(location);
        return sessionRepository.save(session);
    }

    private void addExerciseToSession(Session session, Exercise exercise, int orderNum, String notes) {
        SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSession(session);
        sessionExercise.setExercise(exercise);
        sessionExercise.setOrderNum(orderNum);
        sessionExercise.setNotes(notes);

        SessionExerciseId id = new SessionExerciseId();
        id.setSessionId(session.getSessionId());
        id.setExerciseId(exercise.getExerciseId());
        sessionExercise.setId(id);

        sessionExerciseRepository.save(sessionExercise);
    }

}

