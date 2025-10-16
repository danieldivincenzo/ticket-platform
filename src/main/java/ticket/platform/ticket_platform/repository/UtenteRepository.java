package ticket.platform.ticket_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    public Optional<Utente> findByEmail(String email);
}
