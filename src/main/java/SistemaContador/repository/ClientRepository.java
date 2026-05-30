package SistemaContador.repository;

import SistemaContador.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository
        extends JpaRepository<Client,Integer> {

    List<Client> findByUserIdAndStatusTrue(
            Integer userId
    );
}