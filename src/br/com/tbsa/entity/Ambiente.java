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
 * Ambiente generated by hbm2java
 */
@Entity
@Table(name = "AMBIENTE",
        schema = "dbo",
        catalog = "BIGEYE"
)
public class Ambiente implements java.io.Serializable {

    private int idAmbiente;
    private String descricao;
    private Set<Vm> vms = new HashSet<Vm>(0);

    public Ambiente() {
    }

    public Ambiente(String descricao) {
        this.descricao = descricao;
    }

    public Ambiente(int idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public Ambiente(int idAmbiente, String descricao, Set<Vm> vms) {
        this.idAmbiente = idAmbiente;
        this.descricao = descricao;
        this.vms = vms;
    }

    @Id

    @Column(name = "ID_AMBIENTE", unique = true, nullable = false)
    public int getIdAmbiente() {
        return this.idAmbiente;
    }

    public void setIdAmbiente(int idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    @Column(name = "DESCRICAO", length = 150)
    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ambiente")
    public Set<Vm> getVms() {
        return this.vms;
    }

    public void setVms(Set<Vm> vms) {
        this.vms = vms;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.descricao);
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
        final Ambiente other = (Ambiente) obj;
        return Objects.equals(this.descricao.toLowerCase(), other.descricao.toLowerCase());
    }

    @Override
    public String toString() {
        return descricao;
    }

}