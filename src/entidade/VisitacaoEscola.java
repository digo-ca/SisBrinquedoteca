/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author PcWare
 */
@Entity
public class VisitacaoEscola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    
    @Column(nullable = false)
    private Periodo periodo;
    
    //relação////////////////////////////
    @ManyToOne(cascade = CascadeType.ALL)
    private Escola escola;
    
    @Column(length = 50,nullable = false)
    private String professor;
    
    @Column(name = "atividades_ministradas")
    private String atividadesMinistradas;
    
    @Column(name = "faixa_etaria_criancas")
    private String faixaEtariaCriancas;
    
    
    private List<String> alunos;
    
    //relação///////////////////////////
    @ManyToOne(cascade = CascadeType.ALL)
    private Monitor monitor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getAtividadesMinistradas() {
        return atividadesMinistradas;
    }

    public void setAtividadesMinistradas(String atividadesMinistradas) {
        this.atividadesMinistradas = atividadesMinistradas;
    }

    public String getFaixaEtariaCriancas() {
        return faixaEtariaCriancas;
    }

    public void setFaixaEtariaCriancas(String faixaEtariaCriancas) {
        this.faixaEtariaCriancas = faixaEtariaCriancas;
    }

    public List<String> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<String> alunos) {
        this.alunos = alunos;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    } 
}
