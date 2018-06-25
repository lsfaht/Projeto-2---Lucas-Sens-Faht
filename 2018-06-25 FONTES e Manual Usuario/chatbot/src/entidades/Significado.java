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
@Table(name = "significado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Significado.findAll", query = "SELECT s FROM Significado s"),
    @NamedQuery(name = "Significado.findById", query = "SELECT s FROM Significado s WHERE s.id = :id"),
    @NamedQuery(name = "Significado.findBySignificado", query = "SELECT s FROM Significado s WHERE s.significado = :significado")})
public class Significado implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "significado")
    private String significado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSignificado")
    private Collection<Frase> fraseCollection;

    public Significado() {
    }

    public Significado(Integer id) {
        this.id = id;
    }

    public Significado(Integer id, String significado) {
        this.id = id;
        this.significado = significado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getSignificado() {
        return significado;
    }

    public void setSignificado(String significado) {
        String oldSignificado = this.significado;
        this.significado = significado;
        changeSupport.firePropertyChange("significado", oldSignificado, significado);
    }

    @XmlTransient
    public Collection<Frase> getFraseCollection() {
        return fraseCollection;
    }

    public void setFraseCollection(Collection<Frase> fraseCollection) {
        this.fraseCollection = fraseCollection;
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
        if (!(object instanceof Significado)) {
            return false;
        }
        Significado other = (Significado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Significado[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
