package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Lucas Sens Faht
 */
public class conexao {
    EntityManagerFactory fac = Persistence.createEntityManagerFactory("chatbotPU");
    EntityManager manager = fac.createEntityManager();
    
    public EntityManagerFactory getConexao(){
        return this.fac;
    }
}
