/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author PcWare
 */
@Entity
@NamedQueries(
        @NamedQuery( name = "Monitor.consultarTodos",
                query = "SELECT m FROM Monitor m")
)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = "nomeUsuario")
})
public class Monitor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 50, nullable = true)
    private String nome;
    
    @Column(length = 40, nullable = false)
    private String nomeUsuario;
    
    @Column(length = 100/*, nullable = false*/)
    private String senha;
    
    
    private Boolean supervisor;
    
    
//    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL)
//    @JoinColumn(name = "monitor_id")
//    private List<Visita> visitas;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = supervisor;
    }

//    public List<Visita> getVisitas() {
//        return visitas;
//    }
//
//    public void setVisitas(List<Visita> visitas) {
//        this.visitas = visitas;
//    }
    
    @Override
    public String toString(){
        return nome;
    }
    
}
