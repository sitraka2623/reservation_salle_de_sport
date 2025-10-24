package com.gestion.sallesport.repository;

import com.gestion.sallesport.entity.Reservation;
import com.gestion.sallesport.entity.Client;
import com.gestion.sallesport.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // ================================
    // Recherches par client ou salle
    // ================================
    List<Reservation> findByClient(Client client);

    List<Reservation> findBySalle(Salle salle);

    List<Reservation> findByStatut(Reservation.StatutReservation statut);

    // ================================
    // Recherches par période
    // ================================
    @Query("SELECT r FROM Reservation r WHERE r.dateDebut >= :debut AND r.dateFin <= :fin")
    List<Reservation> findReservationsBetween(@Param("debut") LocalDateTime debut,
                                              @Param("fin") LocalDateTime fin);

    // ================================
    // Vérifier conflits de réservation
    // ================================
    @Query("SELECT r FROM Reservation r WHERE r.salle.id = :salleId " +
            "AND r.statut = 'CONFIRMEE' " +
            "AND ((r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut))")
    List<Reservation> findConflictingReservations(@Param("salleId") Long salleId,
                                                  @Param("dateDebut") LocalDateTime dateDebut,
                                                  @Param("dateFin") LocalDateTime dateFin);

    // ================================
    // Trier les réservations par date
    // ================================
    List<Reservation> findByClientIdOrderByDateDebutDesc(Long clientId);

    List<Reservation> findBySalleIdOrderByDateDebutDesc(Long salleId);

    // ================================
    // Somme des montants pour le mois courant (Dashboard)
    // ================================
    @Query("SELECT SUM(r.prixTotal) FROM Reservation r " +
            "WHERE MONTH(r.dateDebut) = MONTH(CURRENT_DATE) " +
            "AND YEAR(r.dateDebut) = YEAR(CURRENT_DATE)")
    Double sumMontantCurrentMonth();
}
