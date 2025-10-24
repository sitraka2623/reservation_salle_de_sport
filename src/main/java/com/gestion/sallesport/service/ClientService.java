package com.gestion.sallesport.service;

import com.gestion.sallesport.entity.Client;
import com.gestion.sallesport.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Client saveClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà : " + client.getEmail());
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id : " + id));

        if (!client.getEmail().equals(clientDetails.getEmail()) &&
                clientRepository.existsByEmail(clientDetails.getEmail())) {
            throw new RuntimeException("Un autre client utilise déjà cet email : " + clientDetails.getEmail());
        }

        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setEmail(clientDetails.getEmail());
        client.setTelephone(clientDetails.getTelephone());
        client.setAdresse(clientDetails.getAdresse());
        client.setTypeAbonnement(clientDetails.getTypeAbonnement());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id : " + id));
        clientRepository.delete(client);
    }

    public List<Client> searchClients(String searchTerm) {
        return clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(searchTerm, searchTerm);
    }

    public List<Client> getClientsByAbonnement(Client.TypeAbonnement typeAbonnement) {
        return clientRepository.findByTypeAbonnement(typeAbonnement);
    }

    // ✅ Nouvelle méthode pour le dashboard
    public long countClients() {
        return clientRepository.count();
    }
}
