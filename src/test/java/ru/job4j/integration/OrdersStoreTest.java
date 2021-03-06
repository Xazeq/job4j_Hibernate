package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("./db/update_001.sql"))
        )) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void dropTable() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DROP TABLE orders")) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindById() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name", "description"));
        Order result = store.findById(1);
        assertThat(result.getName(), is("name"));
    }

    @Test
    public void whenFindOneOrderByName() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name", "description"));
        List<Order> result = (List<Order>) store.findOrdersByName("name");
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getDescription(), is("description"));
    }

    @Test
    public void whenFindTwoOrdersByName() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name", "description"));
        store.save(Order.of("name", "description2"));
        List<Order> result = (List<Order>) store.findOrdersByName("name");
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getDescription(), is("description"));
        assertThat(result.get(1).getDescription(), is("description2"));
    }

    @Test
    public void whenReplaceOrder() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name", "description"));
        store.replace(new Order(
                1,
                "newName",
                "newDescription",
                new Timestamp(System.currentTimeMillis())));
        Order result = store.findById(1);
        assertThat(result.getName(), is("newName"));
        assertThat(result.getDescription(), is("newDescription"));
    }
}