package com.gestion.sallesport.controller;

import com.gestion.sallesport.entity.Client;
import com.gestion.sallesport.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        var clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        
        // Calculate statistics using Java Stream API
        long nombreAbonnesAnnuels = clients.stream()
            .filter(client -> client.getTypeAbonnement() == Client.TypeAbonnement.ANNUEL)
            .count();
            
        long nombreAbonnesMensuels = clients.stream()
            .filter(client -> client.getTypeAbonnement() == Client.TypeAbonnement.MENSUEL)
            .count();
            
        long nouveauxCeMois = clients.stream()
            .filter(client -> client.getDateInscription() != null && 
                            client.getDateInscription().getMonth() == java.time.LocalDate.now().getMonth() &&
                            client.getDateInscription().getYear() == java.time.LocalDate.now().getYear())
            .count();
        
        model.addAttribute("nombreAbonnesAnnuels", nombreAbonnesAnnuels);
        model.addAttribute("nombreAbonnesMensuels", nombreAbonnesMensuels);
        model.addAttribute("nouveauxCeMois", nouveauxCeMois);
        
        return "clients/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("typesAbonnement", Client.TypeAbonnement.values());
        return "clients/form";
    }

    @PostMapping
    public String createClient(@Valid @ModelAttribute Client client,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("typesAbonnement", Client.TypeAbonnement.values());
            return "clients/form";
        }
        
        try {
            clientService.saveClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Client créé avec succès!");
            return "redirect:/clients";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("typesAbonnement", Client.TypeAbonnement.values());
            return "clients/form";
        }
    }

    @GetMapping("/{id}")
    public String showClient(@PathVariable Long id, Model model) {
        clientService.getClientById(id).ifPresent(client -> {
            model.addAttribute("client", client);
        });
        return "clients/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        clientService.getClientById(id).ifPresent(client -> {
            model.addAttribute("client", client);
            model.addAttribute("typesAbonnement", Client.TypeAbonnement.values());
        });
        return "clients/form";
    }

    @PostMapping("/{id}")
    public String updateClient(@PathVariable Long id,
                               @Valid @ModelAttribute Client client,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        System.out.println("=== MISE A JOUR CLIENT ===");
        System.out.println("ID du client à modifier: " + id);
        System.out.println("Données reçues: " + client);
        System.out.println("Erreurs de validation: " + result.hasErrors());
        
        if (result.hasErrors()) {
            System.out.println("Erreurs: " + result.getAllErrors());
            model.addAttribute("typesAbonnement", Client.TypeAbonnement.values());
            return "clients/form";
        }
        
        try {
            Client updatedClient = clientService.updateClient(id, client);
            System.out.println("Client modifié avec succès: " + updatedClient);
            redirectAttributes.addFlashAttribute("successMessage", "Client modifié avec succès!");
            return "redirect:/clients";
        } catch (RuntimeException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("typesAbonnement", Client.TypeAbonnement.values());
            return "clients/form";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(id);
        redirectAttributes.addFlashAttribute("successMessage", "Client supprimé avec succès!");
        return "redirect:/clients";
    }

    @GetMapping("/search")
    public String searchClients(@RequestParam(required = false) String term, Model model) {
        var clients = (term != null && !term.isEmpty()) 
            ? clientService.searchClients(term) 
            : clientService.getAllClients();
            
        model.addAttribute("clients", clients);
        model.addAttribute("searchTerm", term);
        
        // Calculate statistics using Java Stream API
        long nombreAbonnesAnnuels = clients.stream()
            .filter(client -> client.getTypeAbonnement() == Client.TypeAbonnement.ANNUEL)
            .count();
            
        long nombreAbonnesMensuels = clients.stream()
            .filter(client -> client.getTypeAbonnement() == Client.TypeAbonnement.MENSUEL)
            .count();
            
        long nouveauxCeMois = clients.stream()
            .filter(client -> client.getDateInscription() != null && 
                            client.getDateInscription().getMonth() == java.time.LocalDate.now().getMonth() &&
                            client.getDateInscription().getYear() == java.time.LocalDate.now().getYear())
            .count();
        
        model.addAttribute("nombreAbonnesAnnuels", nombreAbonnesAnnuels);
        model.addAttribute("nombreAbonnesMensuels", nombreAbonnesMensuels);
        model.addAttribute("nouveauxCeMois", nouveauxCeMois);
        
        return "clients/list";
    }
}