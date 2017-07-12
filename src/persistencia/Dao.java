package persistencia;

import entidade.Brinquedo;
import entidade.Classificacao;
import entidade.Crianca;
import entidade.DiarioDeBordo;
import entidade.Escola;
import entidade.Livro;
import entidade.Monitor;
import entidade.Responsavel;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.persistence.*;
import javax.persistence.spi.PersistenceProvider;
//import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author Ivanildo
 */
public class Dao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("BrinquedotecaPU");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction trx = em.getTransaction();

    private Dao() {

    }

    public static void salvar(Object o) {
        try {
            trx.begin();
            
            if (em.contains(o)) {
                em.merge(o);
            } else {
                em.persist(o);
            }
            
            trx.commit();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar no Banco");
        }
    }

    public static void remover(Object o) throws SQLIntegrityConstraintViolationException {
        trx.begin();
        em.remove(o);
        trx.commit();
    }

    public static List listar(Class c) {
        Query q = em.createQuery("Select o from " + c.getSimpleName() + " o"); //getSimpleName busca objeto de qualquer classe
        return q.getResultList(); //Retorna uma lista de objetos do banco
    }
    
     public static Livro busca(Integer id){ //Busca uma tupla do banco
        return em.find(Livro.class, id);
    }
    
    public static  List consultarTodos(Class c){
        Query q = em.createNamedQuery(c.getSimpleName()+".consultarTodos");
        
        return q.getResultList();
    }

    public static List<DiarioDeBordo> consultarDiarioHoje(){
        List<DiarioDeBordo> diarios;
        Query q = em.createNamedQuery("DiarioDeBordo.consultarHoje");
        diarios = q.getResultList();
        return diarios;
    }

}
