package com.gestion.sallesport.repository;

import com.gestion.sallesport.entity.Salle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SalleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SalleRepository salleRepository;

    @Test
    void findByDisponibleTrue_shouldReturnAvailableRooms() {
        // Given
        Salle salle1 = new Salle();
        salle1.setNom("Salle 1");
        salle1.setType("Musculation");
        salle1.setCapacite(20);
        salle1.setPrixHeure(25000.0);
        salle1.setDisponible(true);

        Salle salle2 = new Salle();
        salle2.setNom("Salle 2");
        salle2.setType("Yoga");
        salle2.setCapacite(15);
        salle2.setPrixHeure(20000.0);
        salle2.setDisponible(false);

        entityManager.persistAndFlush(salle1);
        entityManager.persistAndFlush(salle2);

        // When
        List<Salle> availableRooms = salleRepository.findByDisponibleTrue();

        // Then
        assertThat(availableRooms).hasSize(1);
        assertThat(availableRooms.get(0).getNom()).isEqualTo("Salle 1");
    }

    @Test
    void findByType_shouldReturnRoomsOfSpecificType() {
        // Given
        Salle salle1 = new Salle();
        salle1.setNom("Salle Musculation 1");
        salle1.setType("Musculation");
        salle1.setCapacite(20);
        salle1.setPrixHeure(25000.0);
        salle1.setDisponible(true);

        Salle salle2 = new Salle();
        salle2.setNom("Salle Musculation 2");
        salle2.setType("Musculation");
        salle2.setCapacite(25);
        salle2.setPrixHeure(30000.0);
        salle2.setDisponible(true);

        Salle salle3 = new Salle();
        salle3.setNom("Studio Yoga");
        salle3.setType("Yoga");
        salle3.setCapacite(15);
        salle3.setPrixHeure(20000.0);
        salle3.setDisponible(true);

        entityManager.persistAndFlush(salle1);
        entityManager.persistAndFlush(salle2);
        entityManager.persistAndFlush(salle3);

        // When
        List<Salle> musculationRooms = salleRepository.findByType("Musculation");

        // Then
        assertThat(musculationRooms).hasSize(2);
        assertThat(musculationRooms).allMatch(salle -> salle.getType().equals("Musculation"));
    }

    @Test
    void findByCapaciteGreaterThanEqual_shouldReturnRoomsWithSufficientCapacity() {
        // Given
        Salle salle1 = new Salle();
        salle1.setNom("Petite Salle");
        salle1.setType("Yoga");
        salle1.setCapacite(10);
        salle1.setPrixHeure(20.0);
        salle1.setDisponible(true);

        Salle salle2 = new Salle();
        salle2.setNom("Grande Salle");
        salle2.setType("Musculation");
        salle2.setCapacite(30);
        salle2.setPrixHeure(35000.0);
        salle2.setDisponible(true);

        entityManager.persistAndFlush(salle1);
        entityManager.persistAndFlush(salle2);

        // When
        List<Salle> largeRooms = salleRepository.findByCapaciteGreaterThanEqual(20);

        // Then
        assertThat(largeRooms).hasSize(1);
        assertThat(largeRooms.get(0).getNom()).isEqualTo("Grande Salle");
    }
}