package com.gestion.sallesport.controller;

import com.gestion.sallesport.entity.Salle;
import com.gestion.sallesport.service.SalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/salles")
@RequiredArgsConstructor
public class SalleController {

    private final SalleService salleService;

    // Liste de toutes les salles
    @GetMapping
    public String listSalles(Model model) {
        List<Salle> salles = salleService.getAllSalles();
        model.addAttribute("salles", salles);
        return "salles/list"; // ton template Thymeleaf pour afficher toutes les salles
    }

    // Formulaire création salle
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("salle", new Salle());
        return "salles/form"; // template form
    }

    // Sauvegarder nouvelle salle
    @PostMapping
    public String saveSalle(@Valid @ModelAttribute("salle") Salle salle, BindingResult result) {
        if (result.hasErrors()) {
            return "salles/form";
        }
        salleService.saveSalle(salle);
        return "redirect:/salles";
    }

    // Formulaire édition salle
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Salle salle = salleService.getSalleById(id); // retourne Salle directement
        model.addAttribute("salle", salle);
        return "salles/form";
    }

    // Mettre à jour salle
    @PostMapping("/{id}/update")
    public String updateSalle(@PathVariable Long id, @Valid @ModelAttribute("salle") Salle salleDetails, BindingResult result) {
        if (result.hasErrors()) {
            return "salles/form";
        }
        salleService.updateSalle(id, salleDetails);
        return "redirect:/salles";
    }

    // Supprimer salle
    @GetMapping("/{id}/delete")
    public String deleteSalle(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return "redirect:/salles";
    }

    // Salles disponibles
    @GetMapping("/disponibles")
    public String listSallesDisponibles(Model model) {
        List<Salle> sallesDisponibles = salleService.getSallesDisponibles();
        model.addAttribute("salles", sallesDisponibles);
        return "salles/list"; // tu peux utiliser le même template list
    }
}
