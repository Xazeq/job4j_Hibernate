package ru.job4j.manytomany.books;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbnRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book book1 = Book.of("book1");
            Book book2 = Book.of("book2");
            Book book3 = Book.of("book3");
            Book book4 = Book.of("book4");
            Book book5 = Book.of("book5");

            Author author1 = Author.of("author1");
            author1.getBooks().add(book1);
            author1.getBooks().add(book2);
            author1.getBooks().add(book3);

            Author author2 = Author.of("author2");
            author2.getBooks().add(book2);
            author2.getBooks().add(book3);
            author2.getBooks().add(book4);

            Author author3 = Author.of("author3");
            author3.getBooks().add(book3);
            author3.getBooks().add(book4);
            author3.getBooks().add(book5);

            session.save(author1);
            session.save(author2);
            session.save(author3);

            Author author = session.get(Author.class, 2);
            session.remove(author);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
