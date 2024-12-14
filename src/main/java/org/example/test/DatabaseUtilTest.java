package org.example.test;

import org.example.modelRepo.StudentRepo;
import org.example.util.DatabaseUtil;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
