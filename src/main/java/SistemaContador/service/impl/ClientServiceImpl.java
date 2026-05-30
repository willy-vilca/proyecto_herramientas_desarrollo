package SistemaContador.service.impl;

import SistemaContador.model.Client;
import SistemaContador.repository.ClientRepository;
import SistemaContador.service.ClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl
        implements ClientService {

    private final ClientRepository repository;

    @Override
    public List<Client> getClientsByUser(Integer userId) {

        return repository.findByUserIdAndStatusTrue(
                userId
        );
    }

    @Override
    public void save(Client client) {

        repository.save(client);
    }

    @Override
    public void update(Client client) {

        repository.save(client);
    }

    @Override
    public void delete(Integer clientId) {

        Client client =
                repository.findById(clientId)
                        .orElseThrow();

        client.setStatus(false);

        repository.save(client);
    }
}
