package br.com.tbsa.entity;
// Generated 09/05/2018 20:17:25 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Servico generated by hbm2java
 */
@Entity
@Table(name = "SERVICO",
        schema = "dbo",
        catalog = "BIGEYE"
)
public class Servico implements java.io.Serializable {

    private int idServico;
    private String nome;
    private boolean status;
    private String descricao;
    private String executavel;
    private Set<ServicosVm> servicosVms = new HashSet<ServicosVm>(0);

    public Servico() {
    }

    public Servico(String nome, boolean status, String descricao, String executavel) {
        this.nome = nome;
        this.status = status;
        this.descricao = descricao;
        this.executavel = executavel;
    }

    public Servico(int idServico, String nome, boolean status) {
        this.idServico = idServico;
        this.nome = nome;
        this.status = status;
    }

    public Servico(int idServico, String nome, boolean status, String descricao, String executavel, Set<ServicosVm> servicosVms) {
        this.idServico = idServico;
        this.nome = nome;
        this.status = status;
        this.descricao = descricao;
        this.executavel = executavel;
        this.servicosVms = servicosVms;
    }

    public Servico(String nome) {
        this.nome = nome;
    }

    @Id

    @Column(name = "ID_SERVICO", unique = true, nullable = false)
    public int getIdServico() {
        return this.idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    @Column(name = "NOME", nullable = false, length = 100)
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "STATUS", nullable = false)
    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Column(name = "DESCRICAO", length = 150)
    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "EXECUTAVEL", length = 150)
    public String getExecutavel() {
        return this.executavel;
    }

    public void setExecutavel(String executavel) {
        this.executavel = executavel;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servico")
    public Set<ServicosVm> getServicosVms() {
        return this.servicosVms;
    }

    public void setServicosVms(Set<ServicosVm> servicosVms) {
        this.servicosVms = servicosVms;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Servico other = (Servico) obj;
        return Objects.equals(this.nome.toLowerCase(), other.nome.toLowerCase());
    }

}
