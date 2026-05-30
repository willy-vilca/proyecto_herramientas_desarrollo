package SistemaContador.service;

import SistemaContador.model.Client;

import java.util.List;

public interface ClientService {

    List<Client> getClientsByUser(Integer userId);

    void save(Client client);

    void update(Client client);

    void delete(Integer clientId);
}