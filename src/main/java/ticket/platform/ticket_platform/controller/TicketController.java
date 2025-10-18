package ticket.platform.ticket_platform.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String store(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if(bindingResult.hasErrors()) {
            List<Utente> utenti = utenteRepository.findAll();
            List<Utente> operatoriDisponibili = utenti.stream().filter(utente -> utente.getRuoli().stream().anyMatch(ruolo -> ruolo.getNome().equals("OPERATORE")) && utente.isDisponibile()).collect(Collectors.toList());
            model.addAttribute("listaOperatori", operatoriDisponibili);
            return "tickets/create";

        } else{
            ticket.setDataCreazione(LocalDateTime.now());
            ticket.setStato("Da fare");
            ticketRepository.save(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket creato con successo!");
            return "redirect:/admin/tickets";
        }
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();

            model.addAttribute("ticket", ticket);
            return "tickets/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket con id " + id + " non trovato.");
        }
    }
    
    
    
}
