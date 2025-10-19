package ticket.platform.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ticket.platform.ticket_platform.model.Nota;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.Utente;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UtenteRepository;






@Controller
@RequestMapping("/operatore/tickets")
public class OperatoreController {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public String index(Authentication authentication, Model model) {
        String userEmail = authentication.getName();

        Utente operatore = utenteRepository.findByEmail(userEmail).get();
        
        List<Ticket> listaTickets = operatore.getTicketsAssegnati();
        model.addAttribute("listaTickets", listaTickets);

        return "operatore/index";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(id).get();

        String userEmail = authentication.getName();
        if (!ticket.getOperatore().getEmail().equals(userEmail)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso non consentito.");
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("nota", new Nota());
        return "operatore/show";
    }

    @PostMapping("/update/{id}")
    public String updateStato(@PathVariable("id") Integer id, @ModelAttribute("ticket") Ticket formTicket, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(id).get();

        String userEmail = authentication.getName();
        if (!ticket.getOperatore().getEmail().equals(userEmail)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        
        ticket.setStato(formTicket.getStato());
        ticketRepository.save(ticket);
        return "redirect:/operatore/tickets/show/" + id;
    }

    @GetMapping("/profilo")
    public String showProfilo(Authentication authentication, Model model) {
        String userEmail = authentication.getName();
        Utente operatore = utenteRepository.findByEmail(userEmail).get();
        model.addAttribute("operatore", operatore);

        boolean haTicketAttivi = operatore.getTicketsAssegnati().stream().anyMatch(ticket -> ticket.getStato().equals("Da fare") || ticket.getStato().equals("In corso"));
        model.addAttribute("haTicketAttivi", haTicketAttivi);

        return "operatore/profilo";
    }

    @PostMapping("/profilo")
    public String updateProfilo(@ModelAttribute("operatore") Utente formOperatore, Authentication authentication, RedirectAttributes redirectAttributes) {
        String userEmail = authentication.getName();
        Utente operatoreOriginale = utenteRepository.findByEmail(userEmail).get();

        if(!formOperatore.isDisponibile()){
            boolean haTicketAttivi = operatoreOriginale.getTicketsAssegnati().stream().anyMatch(ticket -> ticket.getStato().equals("Da fare") || ticket.getStato().equals("In corso"));

            if (haTicketAttivi){
                redirectAttributes.addFlashAttribute("errorMessage", "Non puoi impostare lo stato 'Non disponibile' mentre hai ticket aperti.");
                return "redirect:/operatore/tickets/profilo";
            }
        }
        
        operatoreOriginale.setDisponibile(formOperatore.isDisponibile());
        utenteRepository.save(operatoreOriginale);

        redirectAttributes.addFlashAttribute("successMessage", "Profilo aggiornato con successo.");
        return "redirect:/operatore/tickets/profilo";
    }
    
    
    
    
    
}
