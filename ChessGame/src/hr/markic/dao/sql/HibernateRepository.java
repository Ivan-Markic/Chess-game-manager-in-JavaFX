/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.markic.dao.sql;

import hr.markic.dao.Repository;
import hr.markic.models.ChessGame;
import hr.markic.models.Person;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author ivanm
 */
public class HibernateRepository implements Repository{
    
    @Override
    public int addPerson(Person data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // in order to use in in transaction scope, we must create new Person that with data details
            Person person = new Person(data);
            em.persist(person);
            em.getTransaction().commit();
            return person.getIDPerson();
        }
    }
    
    @Override
    public int addChessGame(ChessGame data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            ChessGame chessGame = new ChessGame(data);
            em.persist(chessGame);
            em.merge(chessGame);
            em.getTransaction().commit();
            return chessGame.getIDChessGame();
        }
    }

    @Override
    public void deletePerson(Person person) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            em.remove(em.contains(person) ? person : em.merge(person));
            em.getTransaction().commit();
        }
    }
    
    @Override
    public void deleteChessGame(ChessGame chessGame) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            em.remove(em.contains(chessGame) ? chessGame : em.merge(chessGame));
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Person> getPeople() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getEntityManagerFactory().getCache().evictAll();
            List resultList = em.createNamedQuery(HibernateFactory.SELECT_PEOPLE).getResultList();            
            return resultList;
        }
    }
    
    @Override
    public List<ChessGame> getChessGames() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            return entityManager.get().createNamedQuery(HibernateFactory.SELECT_CHESS_GAMES).getResultList();
        }
    }

    @Override
    public Person getPerson(int idPerson) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            return em.find(Person.class, idPerson);
        }
    }
    
    @Override
    public ChessGame getChessGame(int idChessGame) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            return em.find(ChessGame.class, idChessGame);
        }
    }

    @Override
    public void updatePerson(Person person) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();

            em.find(Person.class, person.getIDPerson()).updateDetails(person);
            em.getTransaction().commit();            
        }
    }
    
    @Override
    public void updateChessGame(ChessGame chessGame) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();

            em.find(ChessGame.class, chessGame.getIDChessGame()).updateDetails(chessGame);
            em.merge(chessGame);
            em.getTransaction().commit();            
        }
    }

    @Override
    public void release() {
        HibernateFactory.release();
    }
}
