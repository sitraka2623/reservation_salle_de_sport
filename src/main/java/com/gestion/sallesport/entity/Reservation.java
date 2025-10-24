package com.gestion.sallesport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"client"})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Le client est obligatoire")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    @NotNull(message = "La salle est obligatoire")
    private Salle salle;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(name = "prix_total")
    private Double prixTotal;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    private String remarques;

    public enum StatutReservation {
        EN_ATTENTE,
        CONFIRMEE,
        ANNULEE,
        TERMINEE
    }

    @PrePersist
    @PreUpdate
    public void calculerPrixTotal() {
        if (salle != null && dateDebut != null && dateFin != null) {
            long heures = java.time.Duration.between(dateDebut, dateFin).toHours();
            this.prixTotal = heures * salle.getPrixHeure();
        }
    }
}