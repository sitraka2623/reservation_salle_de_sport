package com.gestion.sallesport.config;

import com.gestion.sallesport.entity.Client;
import com.gestion.sallesport.entity.Salle;
import com.gestion.sallesport.entity.Reservation;
import com.gestion.sallesport.entity.User;
import com.gestion.sallesport.repository.ClientRepository;
import com.gestion.sallesport.repository.SalleRepository;
import com.gestion.sallesport.repository.ReservationRepository;
import com.gestion.sallesport.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final SalleRepository salleRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Initialiser d'abord l'utilisateur admin
        initializeAdminUser();
        
        if (salleRepository.count() == 0) {
            initializeSalles();
        }
        
        if (clientRepository.count() == 0) {
            initializeClients();
        }
        
        if (reservationRepository.count() == 0) {
            initializeReservations();
        }
    }

    private void initializeSalles() {
        Salle salle1 = new Salle();
        salle1.setNom("Salle Musculation 1");
        salle1.setType("Musculation");
        salle1.setCapacite(20);
        salle1.setPrixHeure(25000.0);
        salle1.setDescription("Salle équipée de machines de musculation modernes");
        salle1.setEquipements("Haltères, Barres, Machines guidées, Bancs");
        salle1.setDisponible(true);

        Salle salle2 = new Salle();
        salle2.setNom("Studio Yoga");
        salle2.setType("Yoga");
        salle2.setCapacite(15);
        salle2.setPrixHeure(20000.0);
        salle2.setDescription("Studio calme et lumineux pour la pratique du yoga");
        salle2.setEquipements("Tapis, Blocs, Sangles, Miroirs");
        salle2.setDisponible(true);

        Salle salle3 = new Salle();
        salle3.setNom("Salle Cardio");
        salle3.setType("Cardio");
        salle3.setCapacite(25);
        salle3.setPrixHeure(22000.0);
        salle3.setDescription("Espace cardio avec équipements dernière génération");
        salle3.setEquipements("Tapis de course, Vélos, Elliptiques, Rameurs");
        salle3.setDisponible(true);

        Salle salle4 = new Salle();
        salle4.setNom("Dojo Arts Martiaux");
        salle4.setType("Arts Martiaux");
        salle4.setCapacite(30);
        salle4.setPrixHeure(30000.0);
        salle4.setDescription("Dojo traditionnel pour la pratique des arts martiaux");
        salle4.setEquipements("Tatamis, Sacs de frappe, Miroirs");
        salle4.setDisponible(true);

        Salle salle5 = new Salle();
        salle5.setNom("Studio Danse");
        salle5.setType("Danse");
        salle5.setCapacite(18);
        salle5.setPrixHeure(28000.0);
        salle5.setDescription("Studio de danse avec parquet et sonorisation");
        salle5.setEquipements("Barres, Miroirs, Système audio, Parquet");
        salle5.setDisponible(true);

        salleRepository.saveAll(Arrays.asList(salle1, salle2, salle3, salle4, salle5));
    }

    private void initializeClients() {
        Client client1 = new Client();
        client1.setNom("Dupont");
        client1.setPrenom("Jean");
        client1.setEmail("jean.dupont@email.com");
        client1.setTelephone("0123456789");
        client1.setAdresse("123 Rue de la Paix, Paris");
        client1.setTypeAbonnement(Client.TypeAbonnement.ANNUEL);

        Client client2 = new Client();
        client2.setNom("Martin");
        client2.setPrenom("Marie");
        client2.setEmail("marie.martin@email.com");
        client2.setTelephone("0234567890");
        client2.setAdresse("456 Avenue des Sports, Lyon");
        client2.setTypeAbonnement(Client.TypeAbonnement.MENSUEL);

        Client client3 = new Client();
        client3.setNom("Bernard");
        client3.setPrenom("Pierre");
        client3.setEmail("pierre.bernard@email.com");
        client3.setTelephone("0345678901");
        client3.setAdresse("789 Boulevard du Fitness, Marseille");
        client3.setTypeAbonnement(Client.TypeAbonnement.TRIMESTRIEL);

        Client client4 = new Client();
        client4.setNom("Moreau");
        client4.setPrenom("Sophie");
        client4.setEmail("sophie.moreau@email.com");
        client4.setTelephone("0456789012");
        client4.setAdresse("321 Rue du Bien-être, Toulouse");
        client4.setTypeAbonnement(Client.TypeAbonnement.OCCASIONNEL);

        clientRepository.saveAll(Arrays.asList(client1, client2, client3, client4));
    }

    private void initializeReservations() {
        Client client1 = clientRepository.findByEmail("jean.dupont@email.com").orElse(null);
        Client client2 = clientRepository.findByEmail("marie.martin@email.com").orElse(null);
        Salle salle1 = salleRepository.findById(1L).orElse(null);
        Salle salle2 = salleRepository.findById(2L).orElse(null);

        if (client1 != null && salle1 != null) {
            Reservation reservation1 = new Reservation();
            reservation1.setClient(client1);
            reservation1.setSalle(salle1);
            reservation1.setDateDebut(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            reservation1.setDateFin(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0));
            reservation1.setStatut(Reservation.StatutReservation.CONFIRMEE);
            reservation1.setRemarques("Séance de musculation matinale");
            reservation1.calculerPrixTotal();

            reservationRepository.save(reservation1);
        }

        if (client2 != null && salle2 != null) {
            Reservation reservation2 = new Reservation();
            reservation2.setClient(client2);
            reservation2.setSalle(salle2);
            reservation2.setDateDebut(LocalDateTime.now().plusDays(2).withHour(18).withMinute(0));
            reservation2.setDateFin(LocalDateTime.now().plusDays(2).withHour(19).withMinute(30));
            reservation2.setStatut(Reservation.StatutReservation.EN_ATTENTE);
            reservation2.setRemarques("Cours de yoga du soir");
            reservation2.calculerPrixTotal();

            reservationRepository.save(reservation2);
        }
    }

    private void initializeAdminUser() {
        String adminUsername = "admin";
        
        if (!userService.existsByUsername(adminUsername)) {
            try {
                User admin = userService.createUser(
                    adminUsername,
                    "admin123",
                    "Administrateur Système",
                    User.Role.ADMIN
                );
                log.info("Utilisateur admin créé avec succès: {}", admin.getUsername());
                log.info("Mot de passe par défaut: admin123");
            } catch (Exception e) {
                log.error("Erreur lors de la création de l'utilisateur admin", e);
            }
        } else {
            log.info("L'utilisateur admin existe déjà");
        }
    }
}