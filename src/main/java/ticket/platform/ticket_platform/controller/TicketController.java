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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.Nota;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.Utente;
import ticket.platform.ticket_platform.repository.CategoriaRepository;
import ticket.platform.ticket_platform.repository.NotaRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UtenteRepository;

@Controller
@RequestMapping("/admin/tickets")
public class TicketController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Ticket> listaTickets = null;
        if(keyword != null && !keyword.isBlank()){
            listaTickets = ticketRepository.findByTitoloContainingIgnoreCase(keyword);
        } else {
            listaTickets = ticketRepository.findAll();
        }
        model.addAttribute("listaTickets", listaTickets);
        model.addAttribute("keyword", keyword);
        return "tickets/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("ticket", new Ticket());

        List<Utente> utenti = utenteRepository.findAll();

        List<Utente> operatoriDisponibili = utenti.stream().filter(utente -> utente.getRuoli().stream().anyMatch(ruolo -> ruolo.getNome().equals("OPERATORE")) && utente.isDisponibile()).collect(Collectors.toList());
        model.addAttribute("listaOperatori", operatoriDisponibili);

        model.addAttribute("listaCategorie", categoriaRepository.findAll());

        return "tickets/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if(bindingResult.hasErrors()) {
            List<Utente> utenti = utenteRepository.findAll();
            List<Utente> operatoriDisponibili = utenti.stream().filter(utente -> utente.getRuoli().stream().anyMatch(ruolo -> ruolo.getNome().equals("OPERATORE")) && utente.isDisponibile()).collect(Collectors.toList());
            model.addAttribute("listaOperatori", operatoriDisponibili);

            model.addAttribute("listaCategorie", categoriaRepository.findAll());

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

            model.addAttribute("listaCategorie", categoriaRepository.findAll());
            
            return "tickets/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket con id " + id + " non trovato.");
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);

            List<Utente> utenti = utenteRepository.findAll();
            List<Utente> operatoriDisponibili = utenti.stream().filter(utente -> utente.getRuoli().stream().anyMatch(ruolo -> ruolo.getNome().equals("OPERATORE")) && utente.isDisponibile()).collect(Collectors.toList());
            model.addAttribute("listaOperatori", operatoriDisponibili);

            model.addAttribute("listaCategorie", categoriaRepository.findAll());

            return "tickets/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket con id " + id + " non trovato.");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if(bindingResult.hasErrors()){
            List<Utente> utenti = utenteRepository.findAll();
            List<Utente> operatoriDisponibili = utenti.stream().filter(utente -> utente.getRuoli().stream().anyMatch(ruolo -> ruolo.getNome().equals("OPERATORE")) && utente.isDisponibile()).collect(Collectors.toList());
            model.addAttribute("listaOperatori", operatoriDisponibili);

            model.addAttribute("listaCategorie", categoriaRepository.findAll());

            return "tickets/edit";
            
        } else {
            Ticket ticketOriginale = ticketRepository.findById(id).get();

            formTicket.setId(ticketOriginale.getId());
            formTicket.setDataCreazione(ticketOriginale.getDataCreazione());

            ticketRepository.save(formTicket);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket aggiornato con successo!");
            return "redirect:/admin/tickets";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        for (Nota nota : ticketRepository.findById(id).get().getNote()) {
            notaRepository.delete(nota);
        }

        ticketRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket eliminato con successo!");
        
        return "redirect:/admin/tickets";
    }
    
    
    
    
    
    
}
