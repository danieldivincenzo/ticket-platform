package ticket.platform.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Integer> {

}
