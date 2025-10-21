package ticket.platform.ticket_platform.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.repository.TicketRepository;


@RestController
@CrossOrigin
@RequestMapping("/api/tickets")
public class TicketRestController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> index(@RequestParam(name="stato", required=false) String stato, @RequestParam(name="categoria", required=false) String categoria) {
        List<Ticket> listaTickets = null;
        if (stato != null && !stato.isBlank()){
            listaTickets = ticketRepository.findByStato(stato);
        } else if (categoria != null && !categoria.isBlank()){
            listaTickets = ticketRepository.findByCategorie_Nome(categoria);
        } else {
            listaTickets = ticketRepository.findAll();
        }

        return listaTickets;
    }
    
}
