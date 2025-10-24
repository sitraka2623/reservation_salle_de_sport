package com.gestion.sallesport.service;

import com.gestion.sallesport.entity.Salle;
import com.gestion.sallesport.repository.SalleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SalleServiceTest {

    @Mock
    private SalleRepository salleRepository;

    @InjectMocks
    private SalleService salleService;

    private Salle salle;

    @BeforeEach
    void setUp() {
        salle = new Salle();
        salle.setId(1L);
        salle.setNom("Salle Test");
        salle.setType("Musculation");
        salle.setCapacite(20);
        salle.setPrixHeure(25000.0);
        salle.setDisponible(true);
    }

    @Test
    void getAllSalles_shouldReturnAllSalles() {
        // Given
        List<Salle> salles = Arrays.asList(salle);
        given(salleRepository.findAll()).willReturn(salles);

        // When
        List<Salle> result = salleService.getAllSalles();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(salle);
    }

    @Test
    void getSalleById_withValidId_shouldReturnSalle() {
        // Given
        given(salleRepository.findById(1L)).willReturn(Optional.of(salle));

        // When
        Optional<Salle> result = salleService.getSalleById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(salle);
    }

    @Test
    void saveSalle_shouldSaveAndReturnSalle() {
        // Given
        given(salleRepository.save(any(Salle.class))).willReturn(salle);

        // When
        Salle result = salleService.saveSalle(salle);

        // Then
        assertThat(result).isEqualTo(salle);
        verify(salleRepository).save(salle);
    }

    @Test
    void updateSalle_withValidId_shouldUpdateAndReturnSalle() {
        // Given
        Salle updatedSalle = new Salle();
        updatedSalle.setNom("Salle Modifiée");
        updatedSalle.setType("Cardio");
        updatedSalle.setCapacite(30);
        updatedSalle.setPrixHeure(30000.0);
        updatedSalle.setDisponible(false);

        given(salleRepository.findById(1L)).willReturn(Optional.of(salle));
        given(salleRepository.save(any(Salle.class))).willReturn(salle);

        // When
        Salle result = salleService.updateSalle(1L, updatedSalle);

        // Then
        assertThat(result.getNom()).isEqualTo("Salle Modifiée");
        assertThat(result.getType()).isEqualTo("Cardio");
        assertThat(result.getCapacite()).isEqualTo(30);
        assertThat(result.getPrixHeure()).isEqualTo(30000.0);
        assertThat(result.isDisponible()).isFalse();
    }

    @Test
    void updateSalle_withInvalidId_shouldThrowException() {
        // Given
        given(salleRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> salleService.updateSalle(999L, salle))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Salle non trouvée avec l'id : 999");
    }

    @Test
    void deleteSalle_withValidId_shouldDeleteSalle() {
        // Given
        given(salleRepository.findById(1L)).willReturn(Optional.of(salle));

        // When
        salleService.deleteSalle(1L);

        // Then
        verify(salleRepository).delete(salle);
    }

    @Test
    void deleteSalle_withInvalidId_shouldThrowException() {
        // Given
        given(salleRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> salleService.deleteSalle(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Salle non trouvée avec l'id : 999");
    }

    @Test
    void getSallesDisponibles_shouldReturnAvailableSalles() {
        // Given
        List<Salle> availableSalles = Arrays.asList(salle);
        given(salleRepository.findByDisponibleTrue()).willReturn(availableSalles);

        // When
        List<Salle> result = salleService.getSallesDisponibles();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isDisponible()).isTrue();
    }
}