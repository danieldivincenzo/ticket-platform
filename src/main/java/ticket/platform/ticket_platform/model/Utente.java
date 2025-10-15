package ticket.platform.ticket_platform.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique=true, nullable=false)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @NotBlank
    private String nomeCompleto;

    private boolean disponibile = true;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Ruolo> ruoli;

    @OneToMany(mappedBy = "operatore")
    private List<Ticket> ticketsAssegnati;

    @OneToMany(mappedBy = "autore")
    private List<Nota> noteCreate;

    public List<Ticket> getTicketsAssegnati() {
        return ticketsAssegnati;
    }

    public void setTicketsAssegnati(List<Ticket> ticketsAssegnati) {
        this.ticketsAssegnati = ticketsAssegnati;
    }

    public List<Nota> getNoteCreate() {
        return noteCreate;
    }

    public void setNoteCreate(List<Nota> noteCreate) {
        this.noteCreate = noteCreate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ruolo> getRuoli() {
        return ruoli;
    }

    public void setRuoli(List<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }
}
