package com.gestion.sallesport.service;

import com.gestion.sallesport.repository.ClientRepository;
import com.gestion.sallesport.repository.ReservationRepository;
import com.gestion.sallesport.repository.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClientRepository clientRepository;
    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;

    public long countClients() {
        return clientRepository.count();
    }

    public long countSalles() {
        return salleRepository.count();
    }

    public long countReservations() {
        return reservationRepository.count();
    }

    public double revenueMonth() {
        // Exemple simple : somme du prixTotal des réservations confirmées du mois courant
        return reservationRepository.findAll().stream()
                .filter(r -> r.getStatut().name().equals("CONFIRMEE"))
                .mapToDouble(r -> r.getPrixTotal())
                .sum();
    }
}
