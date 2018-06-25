/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import entidades.Frase;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Significado;
import entidades.Respostas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lucas Sens Faht
 */
public class FraseJpaController implements Serializable {

    public FraseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Frase frase) {
        if (frase.getRespostasCollection() == null) {
            frase.setRespostasCollection(new ArrayList<Respostas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Significado idSignificado = frase.getIdSignificado();
            if (idSignificado != null) {
                idSignificado = em.getReference(idSignificado.getClass(), idSignificado.getId());
                frase.setIdSignificado(idSignificado);
            }
            Collection<Respostas> attachedRespostasCollection = new ArrayList<Respostas>();
            for (Respostas respostasCollectionRespostasToAttach : frase.getRespostasCollection()) {
                respostasCollectionRespostasToAttach = em.getReference(respostasCollectionRespostasToAttach.getClass(), respostasCollectionRespostasToAttach.getId());
                attachedRespostasCollection.add(respostasCollectionRespostasToAttach);
            }
            frase.setRespostasCollection(attachedRespostasCollection);
            em.persist(frase);
            if (idSignificado != null) {
                idSignificado.getFraseCollection().add(frase);
                idSignificado = em.merge(idSignificado);
            }
            for (Respostas respostasCollectionRespostas : frase.getRespostasCollection()) {
                Frase oldIdFraseOfRespostasCollectionRespostas = respostasCollectionRespostas.getIdFrase();
                respostasCollectionRespostas.setIdFrase(frase);
                respostasCollectionRespostas = em.merge(respostasCollectionRespostas);
                if (oldIdFraseOfRespostasCollectionRespostas != null) {
                    oldIdFraseOfRespostasCollectionRespostas.getRespostasCollection().remove(respostasCollectionRespostas);
                    oldIdFraseOfRespostasCollectionRespostas = em.merge(oldIdFraseOfRespostasCollectionRespostas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Frase frase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Frase persistentFrase = em.find(Frase.class, frase.getId());
            Significado idSignificadoOld = persistentFrase.getIdSignificado();
            Significado idSignificadoNew = frase.getIdSignificado();
            Collection<Respostas> respostasCollectionOld = persistentFrase.getRespostasCollection();
            Collection<Respostas> respostasCollectionNew = frase.getRespostasCollection();
            List<String> illegalOrphanMessages = null;
            for (Respostas respostasCollectionOldRespostas : respostasCollectionOld) {
                if (!respostasCollectionNew.contains(respostasCollectionOldRespostas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Respostas " + respostasCollectionOldRespostas + " since its idFrase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idSignificadoNew != null) {
                idSignificadoNew = em.getReference(idSignificadoNew.getClass(), idSignificadoNew.getId());
                frase.setIdSignificado(idSignificadoNew);
            }
            Collection<Respostas> attachedRespostasCollectionNew = new ArrayList<Respostas>();
            for (Respostas respostasCollectionNewRespostasToAttach : respostasCollectionNew) {
                respostasCollectionNewRespostasToAttach = em.getReference(respostasCollectionNewRespostasToAttach.getClass(), respostasCollectionNewRespostasToAttach.getId());
                attachedRespostasCollectionNew.add(respostasCollectionNewRespostasToAttach);
            }
            respostasCollectionNew = attachedRespostasCollectionNew;
            frase.setRespostasCollection(respostasCollectionNew);
            frase = em.merge(frase);
            if (idSignificadoOld != null && !idSignificadoOld.equals(idSignificadoNew)) {
                idSignificadoOld.getFraseCollection().remove(frase);
                idSignificadoOld = em.merge(idSignificadoOld);
            }
            if (idSignificadoNew != null && !idSignificadoNew.equals(idSignificadoOld)) {
                idSignificadoNew.getFraseCollection().add(frase);
                idSignificadoNew = em.merge(idSignificadoNew);
            }
            for (Respostas respostasCollectionNewRespostas : respostasCollectionNew) {
                if (!respostasCollectionOld.contains(respostasCollectionNewRespostas)) {
                    Frase oldIdFraseOfRespostasCollectionNewRespostas = respostasCollectionNewRespostas.getIdFrase();
                    respostasCollectionNewRespostas.setIdFrase(frase);
                    respostasCollectionNewRespostas = em.merge(respostasCollectionNewRespostas);
                    if (oldIdFraseOfRespostasCollectionNewRespostas != null && !oldIdFraseOfRespostasCollectionNewRespostas.equals(frase)) {
                        oldIdFraseOfRespostasCollectionNewRespostas.getRespostasCollection().remove(respostasCollectionNewRespostas);
                        oldIdFraseOfRespostasCollectionNewRespostas = em.merge(oldIdFraseOfRespostasCollectionNewRespostas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = frase.getId();
                if (findFrase(id) == null) {
                    throw new NonexistentEntityException("The frase with id " + id + " no longer exists.");
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
            Frase frase;
            try {
                frase = em.getReference(Frase.class, id);
                frase.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The frase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Respostas> respostasCollectionOrphanCheck = frase.getRespostasCollection();
            for (Respostas respostasCollectionOrphanCheckRespostas : respostasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Frase (" + frase + ") cannot be destroyed since the Respostas " + respostasCollectionOrphanCheckRespostas + " in its respostasCollection field has a non-nullable idFrase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Significado idSignificado = frase.getIdSignificado();
            if (idSignificado != null) {
                idSignificado.getFraseCollection().remove(frase);
                idSignificado = em.merge(idSignificado);
            }
            em.remove(frase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Frase> findFraseEntities() {
        return findFraseEntities(true, -1, -1);
    }

    public List<Frase> findFraseEntities(int maxResults, int firstResult) {
        return findFraseEntities(false, maxResults, firstResult);
    }

    private List<Frase> findFraseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Frase.class));
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

    public Frase findFrase(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Frase.class, id);
        } finally {
            em.close();
        }
    }

    public int getFraseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Frase> rt = cq.from(Frase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
