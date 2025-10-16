package ticket.platform.ticket_platform.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ticket.platform.ticket_platform.model.Ruolo;
import ticket.platform.ticket_platform.model.Utente;

public class DatabaseUserDetails implements UserDetails{

    private String username;

    private String password;

    private Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(Utente user) {
        this.username = user.getEmail();

        this.password = user.getPassword();

        this.authorities = new HashSet<>();

        for (Ruolo ruolo : user.getRuoli()) {
            authorities.add(new SimpleGrantedAuthority(ruolo.getNome()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
