package com.gestion.sallesport.controller;

import com.gestion.sallesport.entity.Salle;
import com.gestion.sallesport.service.SalleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalleController.class)
class SalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalleService salleService;

    @Autowired
    private ObjectMapper objectMapper;

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
        salle.setDescription("Salle de test");
        salle.setEquipements("Haltères, Bancs");
    }

    @Test
    void listSalles_shouldReturnSallesListView() throws Exception {
        // Given
        given(salleService.getAllSalles()).willReturn(Arrays.asList(salle));

        // When & Then
        mockMvc.perform(get("/salles"))
                .andExpect(status().isOk())
                .andExpect(view().name("salles/list"))
                .andExpect(model().attributeExists("salles"));
    }

    @Test
    void showCreateForm_shouldReturnCreateFormView() throws Exception {
        // When & Then
        mockMvc.perform(get("/salles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("salles/form"))
                .andExpect(model().attributeExists("salle"));
    }

    @Test
    void createSalle_withValidData_shouldRedirectToList() throws Exception {
        // Given
        given(salleService.saveSalle(any(Salle.class))).willReturn(salle);

        // When & Then
        mockMvc.perform(post("/salles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nom", "Salle Test")
                .param("type", "Musculation")
                .param("capacite", "20")
                .param("prixHeure", "25000.0")
                .param("disponible", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/salles"));
    }

    @Test
    void createSalle_withInvalidData_shouldReturnFormWithErrors() throws Exception {
        // When & Then
        mockMvc.perform(post("/salles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nom", "")  // Invalid: empty name
                .param("type", "Musculation")
                .param("capacite", "20")
                .param("prixHeure", "25000.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("salles/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    void showSalle_withValidId_shouldReturnDetailView() throws Exception {
        // Given
        given(salleService.getSalleById(1L)).willReturn(Optional.of(salle));

        // When & Then
        mockMvc.perform(get("/salles/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("salles/detail"))
                .andExpect(model().attributeExists("salle"));
    }

    @Test
    void showEditForm_withValidId_shouldReturnEditFormView() throws Exception {
        // Given
        given(salleService.getSalleById(1L)).willReturn(Optional.of(salle));

        // When & Then
        mockMvc.perform(get("/salles/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("salles/form"))
                .andExpect(model().attributeExists("salle"));
    }

    @Test
    void updateSalle_withValidData_shouldRedirectToList() throws Exception {
        // Given
        given(salleService.updateSalle(anyLong(), any(Salle.class))).willReturn(salle);

        // When & Then
        mockMvc.perform(post("/salles/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nom", "Salle Modifiée")
                .param("type", "Cardio")
                .param("capacite", "25")
                .param("prixHeure", "30000.0")
                .param("disponible", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/salles"));
    }

    @Test
    void deleteSalle_shouldRedirectToList() throws Exception {
        // When & Then
        mockMvc.perform(get("/salles/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/salles"));
    }

    @Test
    void listSallesDisponibles_shouldReturnAvailableSallesView() throws Exception {
        // Given
        given(salleService.getSallesDisponibles()).willReturn(Arrays.asList(salle));

        // When & Then
        mockMvc.perform(get("/salles/disponibles"))
                .andExpect(status().isOk())
                .andExpect(view().name("salles/list"))
                .andExpect(model().attributeExists("salles"));
    }
}