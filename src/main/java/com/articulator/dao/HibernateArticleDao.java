package com.articulator.dao;

import com.articulator.pojo.Article;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class HibernateArticleDao implements ArticleDao {

    private SessionFactory sessionFactory;
    private static HibernateArticleDao instance;

    private static Logger log = Logger.getLogger(HibernateArticleDao.class);

    static {
        try {
            instance = new HibernateArticleDao();

        } catch (InitializationException e) {
            throw new RuntimeException("Error creating HibernateArticleDao:  " + e);

        }
    }

    private HibernateArticleDao() throws InitializationException {

        log.debug("Initializing Article table...");

        try {
            Class.forName("org.hsqldb.jdbcDriver");

            Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "sa", "");

            connection.createStatement().execute("CREATE TABLE ARTICLE (ID integer IDENTITY, "+
                    "ARTICLEID integer, " +
                    "ATTRIBUTE varchar(20)," +
                    "VALUE varchar(2000)," +
                    "LANGUAGE integer," +
                    "TYPE integer)");

        } catch (ClassNotFoundException e) {
            throw new InitializationException("Hsqldb driver class not found");

        } catch (SQLException e) {
            throw new InitializationException("Error creating Article table");

        }

        try {
            Configuration configuration = new Configuration()
                    .setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:testdb")
                    .setProperty("hibernate.connection.username", "sa")
                    .setProperty("hibernate.connection.password", "");

            configuration.addAnnotatedClass(Article.class);

            sessionFactory = configuration.buildSessionFactory(new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry());

            System.out.println();
        } catch (HibernateException e) {
            throw new InitializationException("Error creating session factory");

        }

    }

    public static HibernateArticleDao getInstance()  {
        return instance;

    }

    public void save(Article article) {

        log.debug("Starting save()...");

        Session session = sessionFactory.openSession();

        log.debug("Saving: " + article);
        session.save(article);
        session.flush();
        session.close();

    }

    public void saveAll(List<Article> articles) {

        log.debug("Starting saveAll()...");

        Session session = sessionFactory.openSession();

        for (Article article : articles) {
            log.debug("Saving: " + article);
            session.save(article);
            session.flush();

        }

        session.close();

    }

    public List findAll() {

        log.debug("Starting findAll()...");

        Session session = sessionFactory.openSession();
        List<HibernateArticleDao> articles = session.createCriteria(Article.class).list();
        session.close();

        return articles;

    }
}
