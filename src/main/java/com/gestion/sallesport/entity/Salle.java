package com.gestion.sallesport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la salle est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le type de salle est obligatoire")
    private String type;

    @NotNull(message = "La capacité est obligatoire")
    @Positive(message = "La capacité doit être positive")
    private Integer capacite;

    @NotNull(message = "Le prix par heure est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @Column(name = "prix_heure")
    private Double prixHeure;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String equipements;

    private boolean disponible = true;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    // Champ Transient pour le dashboard (pas stocké en DB)
    @Transient
    private double occupancyRate = 0.0;
}
