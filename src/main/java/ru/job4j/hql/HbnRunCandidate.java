package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbnRunCandidate {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Candidate one = Candidate.of("Ivan", 3, 45000);
            Candidate two = Candidate.of("Sergey", 1, 22000);
            Candidate three = Candidate.of("Oleg", 6, 70000);
            session.save(one);
            session.save(two);
            session.save(three);

            List<Candidate> candidates = session.createQuery("from Candidate").list();
            System.out.println(candidates);

            Candidate candidateById = (Candidate) session.createQuery("from Candidate c where c.id = :cId")
                    .setParameter("cId", 2)
                    .uniqueResult();
            System.out.println(candidateById);

            Candidate candidateByName = (Candidate) session.createQuery("from Candidate c where c.name = :cName")
                    .setParameter("cName", "Ivan")
                    .uniqueResult();
            System.out.println(candidateByName);

            session.createQuery("update Candidate c set c.experience = :cExp, c.salary = :cSal where c.id = :cId")
                    .setParameter("cExp", 7)
                    .setParameter("cSal", 100000.0)
                    .setParameter("cId", 3)
                    .executeUpdate();

            session.createQuery("delete from Candidate c where c.id = :cId")
                    .setParameter("cId", 1)
                    .executeUpdate();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
