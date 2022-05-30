package dao;

import connection.HibernateUtil;
import entity.Student;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentDao {
    private static final Logger logger = LoggerFactory.getLogger(StudentDao.class);

    //Data Access Object

    public static Student findByName(String name) {
        Transaction transaction = null;
        Student student = new Student();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            student = (Student) session.createQuery("FROM Student WHERE first_Name = :name")
                    .setParameter("name", name)
                    .uniqueResult();
            transaction.commit();
            return student;
        } catch (HibernateException exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(exception.getMessage());
        }
        return student;
    }

    public static void addStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(student);

            transaction.commit();
        } catch (HibernateException exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(exception.getMessage());
        }
    }
}