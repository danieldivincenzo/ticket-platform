package ticket.platform.ticket_platform.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.Utente;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UtenteRepository;




@Controller
@RequestMapping("/admin/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @GetMapping
    public String index(Model model) {
        List<Ticket> listaTickets = ticketRepository.findAll();
        model.addAttribute("listaTickets", listaTickets);
        return "tickets/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("ticket", new Ticket());

        List<Utente> utenti = utenteRepository.findAll();

        List<Utente> operatoriDisponibili = utenti.stream().filter(utente -> utente.getRuoli().stream().anyMatch(ruolo -> ruolo.getNome().equals("OPERATORE")) && utente.isDisponibile()).collect(Collectors.toList());
        model.addAttribute("listaOperatori", operatoriDisponibili);
        return "tickets/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            List<Utente> utenti = utenteRepository.findAll();
            List<Utente> operatoriDisponibili = utenti.stream().filter(utente -> utente.getRuoli().stream().anyMatch(ruolo -> ruolo.getNome().equals("OPERATORE")) && utente.isDisponibile()).collect(Collectors.toList());
            model.addAttribute("listaOperatori", operatoriDisponibili);
            return "tickets/create";
        } else{
            ticket.setDataCreazione(LocalDateTime.now());
            ticket.setStato("Da fare");
        }

        ticketRepository.save(ticket);
        return "redirect:/admin/tickets";
    }
    
    
    
}
