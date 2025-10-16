package ticket.platform.ticket_platform.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max=150)
    private String titolo;

    @NotNull
    @NotBlank
    private String stato;

    @NotNull
    private LocalDateTime dataCreazione;

    @ManyToOne
    @JoinColumn(name="operatore_id", nullable=false)
    private Utente operatore;

    @ManyToMany
    private List<Categoria> categorie;

    @OneToMany(mappedBy = "ticket")
    private List<Nota> note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Utente getOperatore() {
        return operatore;
    }

    public void setOperatore(Utente operatore) {
        this.operatore = operatore;
    }

    public List<Categoria> getCategorie() {
        return categorie;
    }

    public void setCategorie(List<Categoria> categorie) {
        this.categorie = categorie;
    }

    public List<Nota> getNota() {
        return note;
    }

    public void setNota(List<Nota> note) {
        this.note = note;
    }

    
}
