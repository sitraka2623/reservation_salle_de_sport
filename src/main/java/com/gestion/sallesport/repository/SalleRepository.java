package com.gestion.sallesport.repository;

import com.gestion.sallesport.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    
    List<Salle> findByDisponibleTrue();
    
    List<Salle> findByType(String type);
    
    List<Salle> findByCapaciteGreaterThanEqual(Integer capacite);
    
    @Query("SELECT s FROM Salle s WHERE s.prixHeure <= :prixMax")
    List<Salle> findByPrixHeureMax(@Param("prixMax") Double prixMax);
    
    @Query("SELECT s FROM Salle s WHERE s.id NOT IN " +
           "(SELECT r.salle.id FROM Reservation r " +
           "WHERE r.statut = 'CONFIRMEE' " +
           "AND ((r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut)))")
    List<Salle> findSallesDisponibles(@Param("dateDebut") LocalDateTime dateDebut, 
                                      @Param("dateFin") LocalDateTime dateFin);
}