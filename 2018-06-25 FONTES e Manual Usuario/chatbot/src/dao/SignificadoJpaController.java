/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Frase;
import entidades.Significado;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lucas Sens Faht
 */
public class SignificadoJpaController implements Serializable {

    public SignificadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Significado significado) {
        if (significado.getFraseCollection() == null) {
            significado.setFraseCollection(new ArrayList<Frase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Frase> attachedFraseCollection = new ArrayList<Frase>();
            for (Frase fraseCollectionFraseToAttach : significado.getFraseCollection()) {
                fraseCollectionFraseToAttach = em.getReference(fraseCollectionFraseToAttach.getClass(), fraseCollectionFraseToAttach.getId());
                attachedFraseCollection.add(fraseCollectionFraseToAttach);
            }
            significado.setFraseCollection(attachedFraseCollection);
            em.persist(significado);
            for (Frase fraseCollectionFrase : significado.getFraseCollection()) {
                Significado oldIdSignificadoOfFraseCollectionFrase = fraseCollectionFrase.getIdSignificado();
                fraseCollectionFrase.setIdSignificado(significado);
                fraseCollectionFrase = em.merge(fraseCollectionFrase);
                if (oldIdSignificadoOfFraseCollectionFrase != null) {
                    oldIdSignificadoOfFraseCollectionFrase.getFraseCollection().remove(fraseCollectionFrase);
                    oldIdSignificadoOfFraseCollectionFrase = em.merge(oldIdSignificadoOfFraseCollectionFrase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Significado significado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Significado persistentSignificado = em.find(Significado.class, significado.getId());
            Collection<Frase> fraseCollectionOld = persistentSignificado.getFraseCollection();
            Collection<Frase> fraseCollectionNew = significado.getFraseCollection();
            List<String> illegalOrphanMessages = null;
            for (Frase fraseCollectionOldFrase : fraseCollectionOld) {
                if (!fraseCollectionNew.contains(fraseCollectionOldFrase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Frase " + fraseCollectionOldFrase + " since its idSignificado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Frase> attachedFraseCollectionNew = new ArrayList<Frase>();
            for (Frase fraseCollectionNewFraseToAttach : fraseCollectionNew) {
                fraseCollectionNewFraseToAttach = em.getReference(fraseCollectionNewFraseToAttach.getClass(), fraseCollectionNewFraseToAttach.getId());
                attachedFraseCollectionNew.add(fraseCollectionNewFraseToAttach);
            }
            fraseCollectionNew = attachedFraseCollectionNew;
            significado.setFraseCollection(fraseCollectionNew);
            significado = em.merge(significado);
            for (Frase fraseCollectionNewFrase : fraseCollectionNew) {
                if (!fraseCollectionOld.contains(fraseCollectionNewFrase)) {
                    Significado oldIdSignificadoOfFraseCollectionNewFrase = fraseCollectionNewFrase.getIdSignificado();
                    fraseCollectionNewFrase.setIdSignificado(significado);
                    fraseCollectionNewFrase = em.merge(fraseCollectionNewFrase);
                    if (oldIdSignificadoOfFraseCollectionNewFrase != null && !oldIdSignificadoOfFraseCollectionNewFrase.equals(significado)) {
                        oldIdSignificadoOfFraseCollectionNewFrase.getFraseCollection().remove(fraseCollectionNewFrase);
                        oldIdSignificadoOfFraseCollectionNewFrase = em.merge(oldIdSignificadoOfFraseCollectionNewFrase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = significado.getId();
                if (findSignificado(id) == null) {
                    throw new NonexistentEntityException("The significado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Significado significado;
            try {
                significado = em.getReference(Significado.class, id);
                significado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The significado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Frase> fraseCollectionOrphanCheck = significado.getFraseCollection();
            for (Frase fraseCollectionOrphanCheckFrase : fraseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Significado (" + significado + ") cannot be destroyed since the Frase " + fraseCollectionOrphanCheckFrase + " in its fraseCollection field has a non-nullable idSignificado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(significado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Significado> findSignificadoEntities() {
        return findSignificadoEntities(true, -1, -1);
    }

    public List<Significado> findSignificadoEntities(int maxResults, int firstResult) {
        return findSignificadoEntities(false, maxResults, firstResult);
    }

    private List<Significado> findSignificadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Significado.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Significado findSignificado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Significado.class, id);
        } finally {
            em.close();
        }
    }

    public int getSignificadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Significado> rt = cq.from(Significado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
