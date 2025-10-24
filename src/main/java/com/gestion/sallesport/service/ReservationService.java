package com.gestion.sallesport.service;

import com.gestion.sallesport.entity.Reservation;
import com.gestion.sallesport.entity.Client;
import com.gestion.sallesport.entity.Salle;
import com.gestion.sallesport.repository.ReservationRepository;
import com.gestion.sallesport.repository.ClientRepository;
import com.gestion.sallesport.repository.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final SalleRepository salleRepository;

    // Récupérer toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Récupérer une réservation par ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Créer une réservation
    public Reservation createReservation(Reservation reservation) {
        validateReservation(reservation);

        // Vérifier les conflits
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getSalle().getId(),
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("La salle est déjà réservée pour cette période");
        }

        // Calculer le prix total avant sauvegarde
        reservation.calculerPrixTotal();

        return reservationRepository.save(reservation);
    }

    // Mettre à jour une réservation
    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));

        validateReservation(reservationDetails);

        // Vérifier les conflits en excluant la réservation actuelle
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservationDetails.getSalle().getId(),
                reservationDetails.getDateDebut(),
                reservationDetails.getDateFin()
        );
        conflicts.removeIf(r -> r.getId().equals(id));

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("La salle est déjà réservée pour cette période");
        }

        reservation.setClient(reservationDetails.getClient());
        reservation.setSalle(reservationDetails.getSalle());
        reservation.setDateDebut(reservationDetails.getDateDebut());
        reservation.setDateFin(reservationDetails.getDateFin());
        reservation.setStatut(reservationDetails.getStatut());
        reservation.setRemarques(reservationDetails.getRemarques());
        reservation.calculerPrixTotal();

        return reservationRepository.save(reservation);
    }

    // Supprimer une réservation
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));
        reservationRepository.delete(reservation);
    }

    // Confirmer une réservation
    public Reservation confirmerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));
        reservation.setStatut(Reservation.StatutReservation.CONFIRMEE);
        return reservationRepository.save(reservation);
    }

    // Annuler une réservation
    public Reservation annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));
        reservation.setStatut(Reservation.StatutReservation.ANNULEE);
        return reservationRepository.save(reservation);
    }

    // Récupérer les réservations d’un client
    public List<Reservation> getReservationsByClient(Long clientId) {
        return reservationRepository.findByClientIdOrderByDateDebutDesc(clientId);
    }

    // Récupérer les réservations d’une salle
    public List<Reservation> getReservationsBySalle(Long salleId) {
        return reservationRepository.findBySalleIdOrderByDateDebutDesc(salleId);
    }

    // Récupérer les réservations par statut
    public List<Reservation> getReservationsByStatut(Reservation.StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    // Récupérer les réservations entre deux dates
    public List<Reservation> getReservationsBetween(LocalDateTime debut, LocalDateTime fin) {
        return reservationRepository.findReservationsBetween(debut, fin);
    }

    // Validation simple des dates
    private void validateReservation(Reservation reservation) {
        if (reservation.getDateDebut().isAfter(reservation.getDateFin())) {
            throw new RuntimeException("La date de début doit être avant la date de fin");
        }

        if (reservation.getDateDebut().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La réservation ne peut pas être dans le passé");
        }
    }
}
