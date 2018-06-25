/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lucas Sens Faht
 */
@Entity
@Table(name = "respostas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Respostas.findAll", query = "SELECT r FROM Respostas r"),
    @NamedQuery(name = "Respostas.findById", query = "SELECT r FROM Respostas r WHERE r.id = :id")})
public class Respostas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_frase", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Frase idFrase;

    public Respostas() {
    }

    public Respostas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Frase getIdFrase() {
        return idFrase;
    }

    public void setIdFrase(Frase idFrase) {
        this.idFrase = idFrase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Respostas)) {
            return false;
        }
        Respostas other = (Respostas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Respostas[ id=" + id + " ]";
    }
    
}
