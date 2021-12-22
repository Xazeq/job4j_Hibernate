package ru.job4j.many.cars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Model three = Model.of("3 series");
            Model four = Model.of("4 series");
            Model five = Model.of("5 series");
            Model seven = Model.of("7 series");
            Model eight = Model.of("8 series");
            session.save(three);
            session.save(four);
            session.save(five);
            session.save(seven);
            session.save(eight);
            Brand bmw = Brand.of("BMW");
            bmw.addModel(three);
            bmw.addModel(four);
            bmw.addModel(five);
            bmw.addModel(seven);
            bmw.addModel(eight);
            session.save(bmw);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
