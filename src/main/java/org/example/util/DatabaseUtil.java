package org.example.util;

import org.example.modelRepo.StudentRepo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseUtil {

    private static final int THREAD_POOL_SIZE = 4; // Размер пула потоков

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

    public static void saveStudentsInParallel(List<StudentRepo> studentRepos) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (StudentRepo studentRepo : studentRepos) {
            executor.submit(() -> saveStudent(studentRepo));
        }
        executor.shutdown(); // Закрываем Executor после выполнения задач
        while (!executor.isTerminated()) {
            // Ожидаем завершения всех потоков
        }
        System.out.println("Все студенты сохранены.");
    }

    // Метод для чтения данных из базы данных
    public static List<StudentRepo> readStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Создание HQL-запроса для получения всех студентов
            Query<StudentRepo> query = session.createQuery("FROM StudentRepo", StudentRepo.class);
            return query.list(); // Возвращаем список студентов
        } catch (Exception e) {
            System.out.println("Ошибка при чтении данных: " + e.getMessage());
            return null; // В случае ошибки возвращаем null
        }
    }

    // Метод для вывода данных в консоль
    public static void printStudents(List<StudentRepo> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("Нет данных для отображения.");
            return;
        }
        for (StudentRepo student : students) {
            System.out.println(student);
        }
    }
}
