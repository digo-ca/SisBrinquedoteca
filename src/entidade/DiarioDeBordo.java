/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author PcWare
 */
@Entity

@NamedQueries(
        @NamedQuery(name = "DiarioDeBordo.consultarHoje",
                query = "SELECT d FROM DiarioDeBordo d WHERE d.dia = CURRENT_DATE")
)
public class DiarioDeBordo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    
//    @Temporal(TemporalType.DATE)
    //@Column(columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private Date dia;
    
      //@Column(name = "brinquedos_mais_usados")
      @ManyToMany
      @JoinTable(name = "DiariodeBordo_Brinquedo", joinColumns = @JoinColumn(name = "diario_de_bordo_id"), 
          inverseJoinColumns = @JoinColumn(name = "brinquedo_id"))
      private List<Brinquedo> brinquedosMaisUsados;
    
    @ManyToOne
    //@Column(name = "monitor_abriu")
    private Monitor monitorAbriu;
    
    @ManyToOne
    //@Column(name = "monitor_fechou")
    private Monitor monitorFechou;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    private List <ItemDiarioDeBordo> ocorrencias;
    
    @Column(name = "visitas_no_dia")
    private int visitasNoDia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return dia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = Date.from(dia.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        //this.dia = dia;
    }

    public List<Brinquedo> getBrinquedosMaisUsados() {
        return brinquedosMaisUsados;
    }

    public void setBrinquedosMaisUsados(List<Brinquedo> brinquedosMaisUsados) {
        this.brinquedosMaisUsados = brinquedosMaisUsados;
    }

    public Monitor getMonitorAbriu() {
        return monitorAbriu;
    }

    public void setMonitorAbriu(Monitor monitorAbriu) {
        this.monitorAbriu = monitorAbriu;
    }

    public Monitor getMonitorFechou() {
        return monitorFechou;
    }

    public void setMonitorFechou(Monitor monitorFechou) {
        this.monitorFechou = monitorFechou;
    }

    public List<ItemDiarioDeBordo> getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(List<ItemDiarioDeBordo> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public int getVisitasNoDia() {
        return visitasNoDia;
    }

    public void setVisitasNoDia(int visitasNoDia) {
        this.visitasNoDia = visitasNoDia;
    }
    
    
}
