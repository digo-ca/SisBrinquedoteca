package entidade;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Ivanildo
 */
@Entity
@NamedQueries(
        @NamedQuery(name = "Crianca.consultarTodos",
                query = "SELECT c FROM Crianca c")
)
public class Crianca implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String nome;

//    @Temporal(TemporalType.DATE)
    @Column(nullable = false/*, columnDefinition = "DATE"*/)
    private LocalDate nascimento;

    @Column(length = 100)
    @Basic(fetch = FetchType.LAZY)
    private String escola;

    //@Column(nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private byte[] foto;
    
    @ManyToMany
    @JoinTable(name = "Crianca_Responsavel",joinColumns = @JoinColumn(name = "crianca_id"),
            inverseJoinColumns = @JoinColumn(name = "responsavel_id"))
    private List<Responsavel> responsaveis;

    //Getters e Setters===============================
    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    @Override
    public String toString(){
        return nome;
    }
}
