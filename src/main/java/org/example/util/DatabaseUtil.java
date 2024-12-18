package org.example.util;

import org.example.modelRepo.StudentRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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

    @Nested
    class DatabaseUtilTest {

        private SessionFactory sessionFactoryMock;
        private Session sessionMock;
        private Transaction transactionMock;

        @BeforeEach
        void setUp() {
            sessionFactoryMock = mock(SessionFactory.class);
            sessionMock = mock(Session.class);
            transactionMock = mock(Transaction.class);

            // Настраиваем мок для HibernateUtil
            HibernateUtil.setSessionFactory(sessionFactoryMock);

            // Настройка поведения мока
            when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
            when(sessionMock.beginTransaction()).thenReturn(transactionMock);
        }

        @Test
        void testSaveStudent() {
            // Arrange
            StudentRepo studentRepo = new StudentRepo("1", 100, 5, "City");

            // Act
            DatabaseUtil.saveStudent(studentRepo);

            // Assert
            verify(sessionMock, times(1)).saveOrUpdate(studentRepo);
            verify(transactionMock, times(1)).commit();
        }

        @Test
        void testSaveStudent_withException() {
            // Arrange
            StudentRepo studentRepo = new StudentRepo("1", 100, 5, "City");
            doThrow(new RuntimeException("Database error")).when(sessionMock).saveOrUpdate(studentRepo);

            // Act
            DatabaseUtil.saveStudent(studentRepo);

            // Assert
            verify(transactionMock, times(1)).rollback();
        }

        @Test
        void testSaveStudentsInParallel() {
            // Arrange
            List<StudentRepo> students = new ArrayList<>();
            students.add(new StudentRepo("1", 100, 5, "City1"));
            students.add(new StudentRepo("2", 95, 3, "City2"));
            students.add(new StudentRepo("3", 80, 2, "City3"));

            // Act
            DatabaseUtil.saveStudentsInParallel(students);

            // Assert
            verify(sessionMock, times(students.size())).saveOrUpdate(any(StudentRepo.class));
        }

        @Test
        void testReadStudents() {
            // Arrange
            List<StudentRepo> mockStudents = new ArrayList<>();
            mockStudents.add(new StudentRepo("1", 100, 5, "City1"));
            mockStudents.add(new StudentRepo("2", 95, 3, "City2"));

            Query<StudentRepo> queryMock = mock(Query.class);
            when(sessionMock.createQuery("FROM StudentRepo", StudentRepo.class)).thenReturn(queryMock);
            when(queryMock.list()).thenReturn(mockStudents);

            // Act
            List<StudentRepo> students = DatabaseUtil.readStudents();

            // Assert
            assertNotNull(students);
            assertEquals(2, students.size());
            assertEquals("City1", students.get(0).getCityName());
        }

        @Test
        void testReadStudents_withException() {
            // Arrange
            when(sessionMock.createQuery("FROM StudentRepo", StudentRepo.class)).thenThrow(new RuntimeException("Database error"));

            // Act
            List<StudentRepo> students = DatabaseUtil.readStudents();

            // Assert
            assertNull(students);
        }

        @Test
        void testPrintStudents_withData() {
            // Arrange
            List<StudentRepo> students = new ArrayList<>();
            students.add(new StudentRepo("1", 100, 5, "City1"));
            students.add(new StudentRepo("2", 95, 3, "City2"));

            // Act & Assert
            assertDoesNotThrow(() -> DatabaseUtil.printStudents(students));
        }

        @Test
        void testPrintStudents_noData() {
            // Arrange
            List<StudentRepo> students = new ArrayList<>();

            // Act & Assert
            assertDoesNotThrow(() -> DatabaseUtil.printStudents(students));
        }
    }
}
