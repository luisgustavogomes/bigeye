package br.com.tbsa.entity;
// Generated 09/05/2018 20:17:25 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Vm generated by hbm2java
 */
@Entity
@Table(name = "VM",
        schema = "dbo",
        catalog = "BIGEYE"
)
public class Vm implements java.io.Serializable {

    private int idVm;
    private Ambiente ambiente;
    private Cluster cluster;
    private So so;
    private String nomeVm;
    private String ipVm;
    private String descricao;
    private boolean servidorBd;
    private Set<ProgramasVm> programasVms = new HashSet<ProgramasVm>(0);

    public Vm() {
    }

    public Vm(Ambiente ambiente, Cluster cluster, So so, String nomeVm, String ipVm, String descricao, boolean servidorBd) {
        this.ambiente = ambiente;
        this.cluster = cluster;
        this.so = so;
        this.nomeVm = nomeVm;
        this.ipVm = ipVm;
        this.descricao = descricao;
        this.servidorBd = servidorBd;
    }

    public Vm(int idVm, Ambiente ambiente, Cluster cluster, So so, String nomeVm, boolean servidorBd) {
        this.idVm = idVm;
        this.ambiente = ambiente;
        this.cluster = cluster;
        this.so = so;
        this.nomeVm = nomeVm;
        this.servidorBd = servidorBd;
    }

    public Vm(int idVm, Ambiente ambiente, Cluster cluster, So so, String nomeVm, String ipVm, String descricao, boolean servidorBd, Set<ProgramasVm> programasVms) {
        this.idVm = idVm;
        this.ambiente = ambiente;
        this.cluster = cluster;
        this.so = so;
        this.nomeVm = nomeVm;
        this.ipVm = ipVm;
        this.descricao = descricao;
        this.servidorBd = servidorBd;
        this.programasVms = programasVms;
    }

    @Id

    @Column(name = "ID_VM", unique = true, nullable = false)
    public int getIdVm() {
        return this.idVm;
    }

    public void setIdVm(int idVm) {
        this.idVm = idVm;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AMBIENTE", nullable = false)
    public Ambiente getAmbiente() {
        return this.ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLUSTER", nullable = false)
    public Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SO", nullable = false)
    public So getSo() {
        return this.so;
    }

    public void setSo(So so) {
        this.so = so;
    }

    @Column(name = "NOME_VM", nullable = false, length = 100)
    public String getNomeVm() {
        return this.nomeVm;
    }

    public void setNomeVm(String nomeVm) {
        this.nomeVm = nomeVm;
    }

    @Column(name = "IP_VM", length = 50)
    public String getIpVm() {
        return this.ipVm;
    }

    public void setIpVm(String ipVm) {
        this.ipVm = ipVm;
    }

    @Column(name = "DESCRICAO", length = 150)
    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "SERVIDOR_BD", nullable = false)
    public boolean isServidorBd() {
        return this.servidorBd;
    }

    public void setServidorBd(boolean servidorBd) {
        this.servidorBd = servidorBd;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vm")
    public Set<ProgramasVm> getProgramasVms() {
        return this.programasVms;
    }

    public void setProgramasVms(Set<ProgramasVm> programasVms) {
        this.programasVms = programasVms;
    }

    @Override
    public String toString() {
        return nomeVm;
    }

}
