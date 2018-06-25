/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Lucas Sens Faht
 */
@Entity
@Table(name = "frase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Frase.findAll", query = "SELECT f FROM Frase f"),
    @NamedQuery(name = "Frase.findById", query = "SELECT f FROM Frase f WHERE f.id = :id"),
    @NamedQuery(name = "Frase.findByFrase", query = "SELECT f FROM Frase f WHERE f.frase = :frase")})
public class Frase implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "frase")
    private String frase;
    @JoinColumn(name = "id_significado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Significado idSignificado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFrase")
    private Collection<Respostas> respostasCollection;

    public Frase() {
    }

    public Frase(Integer id) {
        this.id = id;
    }

    public Frase(Integer id, String frase) {
        this.id = id;
        this.frase = frase;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        String oldFrase = this.frase;
        this.frase = frase;
        changeSupport.firePropertyChange("frase", oldFrase, frase);
    }

    public Significado getIdSignificado() {
        return idSignificado;
    }

    public void setIdSignificado(Significado idSignificado) {
        Significado oldIdSignificado = this.idSignificado;
        this.idSignificado = idSignificado;
        changeSupport.firePropertyChange("idSignificado", oldIdSignificado, idSignificado);
    }

    @XmlTransient
    public Collection<Respostas> getRespostasCollection() {
        return respostasCollection;
    }

    public void setRespostasCollection(Collection<Respostas> respostasCollection) {
        this.respostasCollection = respostasCollection;
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
        if (!(object instanceof Frase)) {
            return false;
        }
        Frase other = (Frase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Frase[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
