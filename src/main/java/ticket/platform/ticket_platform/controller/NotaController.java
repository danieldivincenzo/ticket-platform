package ticket.platform.ticket_platform.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import ticket.platform.ticket_platform.model.Nota;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.Utente;
import ticket.platform.ticket_platform.repository.NotaRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UtenteRepository;



@Controller
@RequestMapping("/note")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private TicketRepository ticketRepository; 

    @Autowired
    private UtenteRepository utenteRepository;

    @PostMapping("/admin/store/{ticketId}")
    public String store(@PathVariable("ticketId") Integer ticketId, @ModelAttribute("nota") Nota formNota, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(ticketId).get();

        String userEmail = authentication.getName();
        Utente autore = utenteRepository.findByEmail(userEmail).get();

        formNota.setTicket(ticket);
        formNota.setAutore(autore);
        formNota.setDataCreazione(LocalDateTime.now());

        notaRepository.save(formNota);
        
        return "redirect:/admin/tickets/show/" + ticketId;
    }

    @PostMapping("/operatore/store/{ticketId}")
    public String storeFromOperatore(@PathVariable("ticketId") Integer ticketId, @ModelAttribute("nota") Nota formNota, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(ticketId).get();

        String userEmail = authentication.getName();
        if (!ticket.getOperatore().getEmail().equals(userEmail)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Utente autore = utenteRepository.findByEmail(userEmail).get();

        formNota.setTicket(ticket);
        formNota.setAutore(autore);
        formNota.setDataCreazione(LocalDateTime.now());

        notaRepository.save(formNota);
        
        return "redirect:/operatore/tickets/show/" + ticketId;
    }
    
    
}
