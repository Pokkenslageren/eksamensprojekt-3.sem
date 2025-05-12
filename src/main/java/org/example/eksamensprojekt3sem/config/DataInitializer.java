package org.example.eksamensprojekt3sem.config;

import jakarta.annotation.PostConstruct;
import org.example.eksamensprojekt3sem.entity.*;
import org.example.eksamensprojekt3sem.entity.Membership.MembershipType;
import org.example.eksamensprojekt3sem.entity.Payment.PaymentStatus;
import org.example.eksamensprojekt3sem.entity.User.UserRole;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@Profile("dev")
public class DataInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void initData() {
        // Create users (admin, chairman, trainers)
        User admin = new User("admin", passwordEncoder.encode("admin123"), UserRole.ADMIN);
        User chairman = new User("chairman", passwordEncoder.encode("chairman123"), UserRole.CHAIRMAN);
        User trainer1 = new User("trainer1", passwordEncoder.encode("trainer123"), UserRole.TRAINER);
        User trainer2 = new User("trainer2", passwordEncoder.encode("trainer123"), UserRole.TRAINER);

        entityManager.persist(admin);
        entityManager.persist(chairman);
        entityManager.persist(trainer1);
        entityManager.persist(trainer2);

        // Create teams
        Team seniorTeam = new Team("Senior hold", "Seniorhold for spillere over 18 år", true);
        Team youthTeam = new Team("Ungdomshold", "Ungdomshold for spillere under 18 år", true);

        entityManager.persist(seniorTeam);
        entityManager.persist(youthTeam);

        // Create members and assign to teams
        Member member1 = new Member("Anders Jensen", "anders@example.com", "Farum Hovedgade 10", LocalDate.of(1990, 5, 15), true);
        member1.setTeam(seniorTeam);
        member1.setTeamJoinDate(LocalDate.of(2023, 3, 1));

        Member member2 = new Member("Marie Nielsen", "marie@example.com", "Stavnsholtvej 25", LocalDate.of(2008, 8, 22), true);
        member2.setTeam(youthTeam);
        member2.setTeamJoinDate(LocalDate.of(2022, 9, 1));

        Member member3 = new Member("Peter Sørensen", "peter@example.com", "Ganløsevej 45", LocalDate.of(1985, 12, 3), true);
        member3.setTeam(seniorTeam);
        member3.setTeamJoinDate(LocalDate.of(2021, 8, 15));

        Member member4 = new Member("Sofie Andersen", "sofie@example.com", "Paltholmvej 12", LocalDate.of(2010, 4, 17), true);
        member4.setTeam(youthTeam);
        member4.setTeamJoinDate(LocalDate.of(2023, 1, 10));

        Member member5 = new Member("Jens Hansen", "jens@example.com", "Farumvej 78", LocalDate.of(1988, 2, 28), true);
        member5.setTeam(seniorTeam);
        member5.setTeamJoinDate(LocalDate.of(2022, 5, 5));

        Member member6 = new Member("Mette Pedersen", "mette@example.com", "Frederiksborgvej 102", LocalDate.of(1975, 7, 9), false);
        // Passive member, no team

        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
        entityManager.persist(member5);
        entityManager.persist(member6);

        // Create memberships
        Membership membership1 = new Membership(member1, MembershipType.SENIOR, LocalDate.of(2023, 1, 1));
        membership1.setEndDate(LocalDate.of(2023, 12, 31));

        Membership membership2 = new Membership(member2, MembershipType.YOUTH, LocalDate.of(2022, 8, 1));
        membership2.setEndDate(LocalDate.of(2023, 7, 31));

        Membership membership3 = new Membership(member3, MembershipType.SENIOR, LocalDate.of(2021, 1, 1));
        membership3.setEndDate(LocalDate.of(2023, 12, 31));

        Membership membership4 = new Membership(member4, MembershipType.YOUTH, LocalDate.of(2023, 1, 1));
        membership4.setEndDate(LocalDate.of(2023, 12, 31));

        Membership membership5 = new Membership(member5, MembershipType.SENIOR, LocalDate.of(2022, 1, 1));
        membership5.setEndDate(LocalDate.of(2022, 12, 31));

        Membership membership6 = new Membership(member6, MembershipType.PASSIVE, LocalDate.of(2023, 1, 1));
        membership6.setEndDate(LocalDate.of(2023, 12, 31));

        entityManager.persist(membership1);
        entityManager.persist(membership2);
        entityManager.persist(membership3);
        entityManager.persist(membership4);
        entityManager.persist(membership5);
        entityManager.persist(membership6);

        // Create payments
        Payment payment1 = new Payment(membership1, new BigDecimal("1200.00"), LocalDate.of(2023, 1, 15), PaymentStatus.PAID);
        Payment payment2 = new Payment(membership2, new BigDecimal("800.00"), LocalDate.of(2022, 8, 5), PaymentStatus.UNPAID);
        Payment payment3 = new Payment(membership3, new BigDecimal("1200.00"), LocalDate.of(2023, 1, 20), PaymentStatus.PARTIAL);
        Payment payment4 = new Payment(membership4, new BigDecimal("800.00"), LocalDate.of(2023, 1, 10), PaymentStatus.PAID);
        Payment payment5 = new Payment(membership5, new BigDecimal("1200.00"), LocalDate.of(2022, 1, 5), PaymentStatus.PAID);
        Payment payment6 = new Payment(membership6, new BigDecimal("400.00"), LocalDate.of(2023, 1, 20), PaymentStatus.PAID);

        entityManager.persist(payment1);
        entityManager.persist(payment2);
        entityManager.persist(payment3);
        entityManager.persist(payment4);
        entityManager.persist(payment5);
        entityManager.persist(payment6);

        // Create exercises
        Exercise exercise1 = new Exercise("Warmup", "Grundig opvarmning med fokus på bevægelighed", 10);
        Exercise exercise2 = new Exercise("5 Star Handball", "Australsk fodbold-inspireret håndboldøvelse", 15);
        Exercise exercise3 = new Exercise("Kicking Practice", "Øvelser til at forbedre spark-teknik", 20);
        Exercise exercise4 = new Exercise("Marking Drills", "Øvelser til at forbedre gribeteknik", 15);
        Exercise exercise5 = new Exercise("Tackling Technique", "Sikker tackling-teknik øvelser", 15);
        Exercise exercise6 = new Exercise("Match Simulation", "Spil med fokus på specifikke spilsituationer", 30);
        Exercise exercise7 = new Exercise("Cooldown", "Nedkøling og stræk", 10);

        entityManager.persist(exercise1);
        entityManager.persist(exercise2);
        entityManager.persist(exercise3);
        entityManager.persist(exercise4);
        entityManager.persist(exercise5);
        entityManager.persist(exercise6);
        entityManager.persist(exercise7);

        // Create training sessions
        LocalDate nextTuesday = LocalDate.now().plusDays((9 - LocalDate.now().getDayOfWeek().getValue()) % 7 + 1);
        LocalDate nextThursday = LocalDate.now().plusDays((11 - LocalDate.now().getDayOfWeek().getValue()) % 7 + 1);

        Session session1 = new Session(
                seniorTeam,
                LocalDateTime.of(nextTuesday, LocalTime.of(18, 0)),
                "Farum Park",
                "Tekniktræning for seniorhold",
                "Fokus på grundlæggende teknikker og holdspil",
                trainer1
        );

        Session session2 = new Session(
                youthTeam,
                LocalDateTime.of(nextThursday, LocalTime.of(17, 0)),
                "Farum Park",
                "Ungdomstræning",
                "Fokus på sjov og grundlæggende færdigheder",
                trainer2
        );

        entityManager.persist(session1);
        entityManager.persist(session2);

        // Create session exercises
        SessionExercise se1 = new SessionExercise(session1, exercise1, 1, "Start med lette løbeøvelser");
        SessionExercise se2 = new SessionExercise(session1, exercise2, 2, "Del holdet i to grupper");
        SessionExercise se3 = new SessionExercise(session1, exercise3, 3, "Fokus på drop punt teknik");
        SessionExercise se4 = new SessionExercise(session1, exercise6, 4, "Småspil 6v6");
        SessionExercise se5 = new SessionExercise(session1, exercise7, 5, "Grundig udstrækning");

        SessionExercise se6 = new SessionExercise(session2, exercise1, 1, "Sjove opvarmningslege");
        SessionExercise se7 = new SessionExercise(session2, exercise4, 2, "Parvis øvelse");
        SessionExercise se8 = new SessionExercise(session2, exercise3, 3, "Grundlæggende spark-teknik");
        SessionExercise se9 = new SessionExercise(session2, exercise7, 4, "Udstrækning i cirkel");

        entityManager.persist(se1);
        entityManager.persist(se2);
        entityManager.persist(se3);
        entityManager.persist(se4);
        entityManager.persist(se5);
        entityManager.persist(se6);
        entityManager.persist(se7);
        entityManager.persist(se8);
        entityManager.persist(se9);
    }
}