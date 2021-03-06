package br.com.tbsa.entity;
// Generated 13/05/2018 19:27:05 by Hibernate Tools 4.3.1

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Usuario generated by hbm2java
 */
@Entity
@Table(name = "USUARIO",
        schema = "dbo",
        catalog = "BIGEYE"
)
public class Usuario implements java.io.Serializable {

    private int idUsuario;
    private Perfil perfil;
    private String codusuario;
    private String nome;
    private String senha;

    public Usuario() {
    }

    public Usuario(int idUsuario, String codusuario, String nome, String senha) {
        this.idUsuario = idUsuario;
        this.codusuario = codusuario;
        this.nome = nome;
        this.senha = senha;
    }

    public Usuario(int idUsuario, Perfil perfil, String codusuario, String nome, String senha) {
        this.idUsuario = idUsuario;
        this.perfil = perfil;
        this.codusuario = codusuario;
        this.nome = nome;
        this.senha = senha;
    }

    public Usuario(Perfil perfil, String codusuario, String nome, String senha) {
        this.perfil = perfil;
        this.codusuario = codusuario;
        this.nome = nome;
        this.senha = senha;
    }

    @Id

    @Column(name = "ID_USUARIO", unique = true, nullable = false)
    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERFIL")
    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Column(name = "CODUSUARIO", nullable = false, length = 50)
    public String getCodusuario() {
        return this.codusuario;
    }

    public void setCodusuario(String codusuario) {
        this.codusuario = codusuario;
    }

    @Column(name = "NOME", nullable = false, length = 100)
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "SENHA", nullable = false, length = 4000)
    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.codusuario);
        hash = 31 * hash + Objects.hashCode(this.nome);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.codusuario, other.codusuario)) {
            return false;
        }
        return Objects.equals(this.nome, other.nome);
    }

}
