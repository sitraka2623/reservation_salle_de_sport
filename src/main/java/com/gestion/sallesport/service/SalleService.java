package com.gestion.sallesport.service;

import com.gestion.sallesport.entity.Reservation;
import com.gestion.sallesport.entity.Salle;
import com.gestion.sallesport.repository.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;

    public List<Salle> getAllSalles() {
        List<Salle> salles = salleRepository.findAll();
        salles.forEach(this::calculerTauxOccupation); // Remplir occupancyRate
        return salles;
    }

    public Salle getSalleById(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'id : " + id));
        calculerTauxOccupation(salle);
        return salle;
    }

    public Salle saveSalle(Salle salle) {
        return salleRepository.save(salle);
    }

    public Salle updateSalle(Long id, Salle salleDetails) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'id : " + id));

        salle.setNom(salleDetails.getNom());
        salle.setType(salleDetails.getType());
        salle.setCapacite(salleDetails.getCapacite());
        salle.setPrixHeure(salleDetails.getPrixHeure());
        salle.setDescription(salleDetails.getDescription());
        salle.setEquipements(salleDetails.getEquipements());
        salle.setDisponible(salleDetails.isDisponible());

        return salleRepository.save(salle);
    }

    public void deleteSalle(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'id : " + id));
        salleRepository.delete(salle);
    }

    public List<Salle> getSallesDisponibles() {
        return salleRepository.findByDisponibleTrue();
    }

    public List<Salle> getSallesByType(String type) {
        return salleRepository.findByType(type);
    }

    public List<Salle> getSallesDisponiblesPourPeriode(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return salleRepository.findSallesDisponibles(dateDebut, dateFin);
    }

    public List<Salle> getSallesByCapaciteMin(Integer capacite) {
        return salleRepository.findByCapaciteGreaterThanEqual(capacite);
    }

    public List<Salle> getSallesByPrixMax(Double prixMax) {
        return salleRepository.findByPrixHeureMax(prixMax);
    }

    // Dashboard
    public long countSalles() {
        return salleRepository.count();
    }

    // ✅ Calcul du taux d'occupation
    private void calculerTauxOccupation(Salle salle) {
        long reservationsConfirmees = salle.getReservations().stream()
                .filter(r -> r.getStatut() != null && r.getStatut().name().equals("CONFIRMEE"))
                .count();

        if (salle.getCapacite() > 0) {
            salle.setOccupancyRate((double) reservationsConfirmees / salle.getCapacite() * 100);
        } else {
            salle.setOccupancyRate(0.0);
        }
    }
}
