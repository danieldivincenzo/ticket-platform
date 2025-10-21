package ticket.platform.ticket_platform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()){
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                return "redirect:/admin/tickets";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("OPERATORE"))){
                return "redirect:/operatore/tickets";
            }
        }
        
        return "redirect:/login";
    }
    
}
