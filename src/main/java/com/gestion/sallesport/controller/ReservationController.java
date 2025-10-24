package com.gestion.sallesport.controller;

import com.gestion.sallesport.entity.Reservation;
import com.gestion.sallesport.service.ReservationService;
import com.gestion.sallesport.service.ClientService;
import com.gestion.sallesport.service.SalleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ClientService clientService;
    private final SalleService salleService;

    @GetMapping
    public String listReservations(Model model) {
        var reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        
        // Calculate statistics using Java Stream API
        long nombreConfirmees = reservations.stream()
            .filter(reservation -> reservation.getStatut() == Reservation.StatutReservation.CONFIRMEE)
            .count();
            
        long nombreEnAttente = reservations.stream()
            .filter(reservation -> reservation.getStatut() == Reservation.StatutReservation.EN_ATTENTE)
            .count();
            
        double revenusConfirmes = reservations.stream()
            .filter(reservation -> reservation.getStatut() == Reservation.StatutReservation.CONFIRMEE)
            .mapToDouble(reservation -> reservation.getPrixTotal() != null ? reservation.getPrixTotal() : 0.0)
            .sum();
        
        model.addAttribute("nombreConfirmees", nombreConfirmees);
        model.addAttribute("nombreEnAttente", nombreEnAttente);
        model.addAttribute("revenusConfirmes", revenusConfirmes);
        
        return "reservations/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("salles", salleService.getAllSalles());
        model.addAttribute("statuts", Reservation.StatutReservation.values());
        return "reservations/form";
    }

    @PostMapping
    public String createReservation(@Valid @ModelAttribute Reservation reservation,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("salles", salleService.getAllSalles());
            model.addAttribute("statuts", Reservation.StatutReservation.values());
            return "reservations/form";
        }
        
        try {
            reservationService.createReservation(reservation);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation créée avec succès!");
            return "redirect:/reservations";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("salles", salleService.getAllSalles());
            model.addAttribute("statuts", Reservation.StatutReservation.values());
            return "reservations/form";
        }
    }

    @GetMapping("/{id}")
    public String showReservation(@PathVariable Long id, Model model) {
        reservationService.getReservationById(id).ifPresent(reservation -> {
            model.addAttribute("reservation", reservation);
        });
        return "reservations/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        reservationService.getReservationById(id).ifPresent(reservation -> {
            model.addAttribute("reservation", reservation);
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("salles", salleService.getAllSalles());
            model.addAttribute("statuts", Reservation.StatutReservation.values());
        });
        return "reservations/form";
    }

    @PostMapping("/{id}")
    public String updateReservation(@PathVariable Long id,
                                    @Valid @ModelAttribute Reservation reservation,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("salles", salleService.getAllSalles());
            model.addAttribute("statuts", Reservation.StatutReservation.values());
            return "reservations/form";
        }
        
        try {
            reservationService.updateReservation(id, reservation);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation modifiée avec succès!");
            return "redirect:/reservations";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("salles", salleService.getAllSalles());
            model.addAttribute("statuts", Reservation.StatutReservation.values());
            return "reservations/form";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservationService.deleteReservation(id);
        redirectAttributes.addFlashAttribute("successMessage", "Réservation supprimée avec succès!");
        return "redirect:/reservations";
    }

    @GetMapping("/{id}/confirmer")
    public String confirmerReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservationService.confirmerReservation(id);
        redirectAttributes.addFlashAttribute("successMessage", "Réservation confirmée!");
        return "redirect:/reservations";
    }

    @GetMapping("/{id}/annuler")
    public String annulerReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservationService.annulerReservation(id);
        redirectAttributes.addFlashAttribute("successMessage", "Réservation annulée!");
        return "redirect:/reservations";
    }

    @GetMapping("/check-disponibilite")
    public String checkDisponibilite(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
                                     Model model) {
        model.addAttribute("sallesDisponibles", salleService.getSallesDisponiblesPourPeriode(dateDebut, dateFin));
        return "reservations/disponibilite";
    }
}