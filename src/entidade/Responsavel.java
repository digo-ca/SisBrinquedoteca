package entidade;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@NamedQueries(
        @NamedQuery( name = "Responsavel.consultarTodos",
                query = "SELECT r FROM Responsavel r")
)
public class Responsavel implements Serializable {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 50/*, nullable = false*/)
    private String nome;
    
    @Column(length = 30/*, nullable = false*/)
    private String telefone;
    
    @Column(length = 100/*, nullable = false*/)
    private String endereco;
    
    @Column(name = "numero_vinculo")
    private Integer numeroVinculo;
    
    private String vinculo;

    @ManyToMany(mappedBy = "responsaveis", fetch = FetchType.LAZY)
    private List<Crianca> criancas;
    
    //Getters e Setters==================================
    public List<Crianca> getCriancas() {
        return criancas;
    }

    public void setCriancas(List<Crianca> criancas) {
        this.criancas = criancas;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getNumeroVinculo() {
        return numeroVinculo;
    }

    public void setNumeroVinculo(Integer numeroVinculo) {
        this.numeroVinculo = numeroVinculo;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
