package com.articulator.dao;

import com.articulator.pojo.Article;
import org.hamcrest.CoreMatchers;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ArticleDaoTest {

    private static ArticleDao articleDao = HibernateArticleDao.getInstance();

    @Test
    public void testSave() throws Exception {

        // setup
        Article article = createArticle(999999, 1, "attribute", 1, 1, "value");

        // test
        articleDao.save(article);

        // assert
        Session session = getSessionFactory().openSession();
        Article actualResult = (Article)session.get(Article.class, article.getId());

        assertThat(actualResult, CoreMatchers.notNullValue());
        assertThat(actualResult.getId(), equalTo(999999));

    }

    @Test
    public void testSaveAll() throws Exception {
        // setup
        Session session = getSessionFactory().openSession();
        session.createQuery("delete from Article").executeUpdate();

        Article article1 = createArticle(888888, 1, "attribute1", 1, 1, "value1");
        Article article2 = createArticle(777777, 2, "attribute1", 2, 2, "value2");
        List<Article> articles = new ArrayList<Article>();
        articles.add(article1);
        articles.add(article2);

        // test
        articleDao.saveAll(articles);

        // assert
        List<Article> actualResult = session.createQuery("from Article").list();

        assertThat(actualResult.size(), equalTo(2));

    }

    @Test
    public void testFindAll() throws Exception {
        // setup

        Session session = getSessionFactory().openSession();
        session.createQuery("delete from Article").executeUpdate();

        Article article1 = createArticle(888888, 1, "attribute1", 1, 1, "value1");
        Article article2 = createArticle(666666, 2, "attribute2", 2, 2, "value2");
        Article article3 = createArticle(444444, 3, "attribute3", 3, 3, "value3");
        List<Article> articles = new ArrayList<Article>();
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);

        save(articles);

        // test
        List<Article> savedArticles = articleDao.findAll();

        // assert
        assertThat(savedArticles.size(), equalTo(3));

    }

    private void save(List<Article> articles) {
        Session session = getSessionFactory().openSession();

        for(Article article : articles) {
            session.save(article);

        }
        session.flush();
        session.close();

    }

    private Article createArticle(Integer id, Integer articleId, String attribute, Integer language, Integer type, String value) {
        Article article = new Article();
        article.setId(id);
        article.setArticleId(articleId);
        article.setAttribute(attribute);
        article.setLanguage(language);
        article.setType(type);
        article.setValue(value);
        return article;

    }

    private SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:testdb")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "");

        configuration.addAnnotatedClass(Article.class);

        return configuration.buildSessionFactory(new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry());

    }
}
