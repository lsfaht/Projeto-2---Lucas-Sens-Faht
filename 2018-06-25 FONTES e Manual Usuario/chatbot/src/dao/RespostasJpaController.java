/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Frase;
import entidades.Respostas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lucas Sens Faht
 */
public class RespostasJpaController implements Serializable {

    public RespostasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Respostas respostas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Frase idFrase = respostas.getIdFrase();
            if (idFrase != null) {
                idFrase = em.getReference(idFrase.getClass(), idFrase.getId());
                respostas.setIdFrase(idFrase);
            }
            em.persist(respostas);
            if (idFrase != null) {
                idFrase.getRespostasCollection().add(respostas);
                idFrase = em.merge(idFrase);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Respostas respostas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Respostas persistentRespostas = em.find(Respostas.class, respostas.getId());
            Frase idFraseOld = persistentRespostas.getIdFrase();
            Frase idFraseNew = respostas.getIdFrase();
            if (idFraseNew != null) {
                idFraseNew = em.getReference(idFraseNew.getClass(), idFraseNew.getId());
                respostas.setIdFrase(idFraseNew);
            }
            respostas = em.merge(respostas);
            if (idFraseOld != null && !idFraseOld.equals(idFraseNew)) {
                idFraseOld.getRespostasCollection().remove(respostas);
                idFraseOld = em.merge(idFraseOld);
            }
            if (idFraseNew != null && !idFraseNew.equals(idFraseOld)) {
                idFraseNew.getRespostasCollection().add(respostas);
                idFraseNew = em.merge(idFraseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = respostas.getId();
                if (findRespostas(id) == null) {
                    throw new NonexistentEntityException("The respostas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Respostas respostas;
            try {
                respostas = em.getReference(Respostas.class, id);
                respostas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The respostas with id " + id + " no longer exists.", enfe);
            }
            Frase idFrase = respostas.getIdFrase();
            if (idFrase != null) {
                idFrase.getRespostasCollection().remove(respostas);
                idFrase = em.merge(idFrase);
            }
            em.remove(respostas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Respostas> findRespostasEntities() {
        return findRespostasEntities(true, -1, -1);
    }

    public List<Respostas> findRespostasEntities(int maxResults, int firstResult) {
        return findRespostasEntities(false, maxResults, firstResult);
    }

    private List<Respostas> findRespostasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Respostas.class));
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

    public Respostas findRespostas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Respostas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRespostasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Respostas> rt = cq.from(Respostas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
