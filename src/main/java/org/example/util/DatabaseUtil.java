package org.example.util;

import org.example.modelRepo.StudentRepo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DatabaseUtil {

    public static void saveStudent(StudentRepo studentRepo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(studentRepo);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                System.out.println("Ошибка при сохранении: " + e.getMessage());
            }
        }
    }

    public static void readAndPrintStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Создание HQL-запроса для получения всех студентов
            Query<StudentRepo> query = session.createQuery("FROM StudentRepo", StudentRepo.class);
            List<StudentRepo> students = query.list();

            // Вывод данных в консоль
            for (StudentRepo student : students) {
                System.out.println(student);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при чтении данных: " + e.getMessage());
        }
    }

}
