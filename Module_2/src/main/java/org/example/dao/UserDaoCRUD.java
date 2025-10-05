package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class UserDaoCRUD implements UserDao<User> {

    private static final Logger log = LoggerFactory.getLogger(UserDaoCRUD.class);

    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            log.info("Transaction is created in save method, {}", transaction);
            session.save(user);
            log.info("User is saved, {}", user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error in save method, {}", e.getMessage());
        }
    }

    @Override
    public User get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
           log.error("Error in get method, {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<User> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        } catch (Exception e) {
            log.error("Error in getAll method, {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            log.info("Transaction is created in update method, {}", transaction);
            session.update(user);
            log.info("User is updated, {}", user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.info("Transaction is rolled in update method, {}", transaction);
            }
            log.error("Error in update method, {}", e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            log.info("Transaction is created in method delete, {}", transaction);
            User user = session.get(User.class, id);
            if(user!= null) {
                session.delete(user);
                log.info("User is deleted, {}", user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.info("Transaction is rolled back, {}", transaction);
            }
           log.error("Error in delete method, {}", e.getMessage());
        }
    }
}
