package com.gestion.sallesport.repository;

import com.gestion.sallesport.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByEmail(String email);
    
    List<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    
    List<Client> findByTypeAbonnement(Client.TypeAbonnement typeAbonnement);
    
    boolean existsByEmail(String email);
}