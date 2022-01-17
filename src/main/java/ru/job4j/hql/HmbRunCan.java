package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HmbRunCan {
    public static void main(String[] args) {
        List<Candidate> candidates = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Vacancy vacancyOne = Vacancy.of("Строитель", "Москва");
            Vacancy vacancyTwo = Vacancy.of("Курьер", "Казань");
            Vacancy vacancyThree = Vacancy.of("Водитель", "Нижний Новгород");
            Vacancy vacancyFour = Vacancy.of("Продавец", "Красноярск");

            VacanciesBase oneBase = VacanciesBase.of("One");
            VacanciesBase twoBase = VacanciesBase.of("Two");

            oneBase.addVacancy(vacancyOne);
            oneBase.addVacancy(vacancyTwo);
            twoBase.addVacancy(vacancyThree);
            twoBase.addVacancy(vacancyFour);

            Candidate candidate1 = Candidate.of("Ivan", 2, 50000);
            Candidate candidate2 = Candidate.of("Oleg", 9, 12500);

            candidate1.setVacanciesBase(oneBase);
            candidate2.setVacanciesBase(twoBase);

            session.save(oneBase);
            session.save(twoBase);
            session.save(candidate1);
            session.save(candidate2);

            candidates = session.createQuery(
                    "select distinct c from Candidate c "
                    + "join fetch c.vacanciesBase vb "
                    + "join fetch vb.vacancies v "
            ).list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        System.out.println(candidates);
    }
}
