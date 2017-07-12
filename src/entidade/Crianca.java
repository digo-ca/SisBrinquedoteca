package entidade;

import java.io.Serializable;
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

    @Column(nullable = false)
    private Integer idade;

    @Column(length = 100)
    private String escola;

    //@Column(nullable = false)
    private String foto;
    
    @ManyToMany
    @JoinTable(name = "Crianca_Responsavel")
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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    @Override
    public String toString(){
        return nome;
    }
}
