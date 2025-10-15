package ticket.platform.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.Utente;

public interface NotaRepository extends JpaRepository<Utente, Integer> {

}
