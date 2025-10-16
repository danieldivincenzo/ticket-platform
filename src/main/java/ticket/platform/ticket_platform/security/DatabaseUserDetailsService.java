package ticket.platform.ticket_platform.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ticket.platform.ticket_platform.model.Utente;
import ticket.platform.ticket_platform.repository.UtenteRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService{

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Utente> utenteOpt = utenteRepository.findByEmail(username);

        if(utenteOpt.isPresent()){

            return new DatabaseUserDetails(utenteOpt.get());

        } else {

            throw new UsernameNotFoundException("Utente con email " + username + " non trovato.");

        }
    }
}
