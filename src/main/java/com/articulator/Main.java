package com.articulator;

import com.articulator.dao.ArticleDao;
import com.articulator.dao.HibernateArticleDao;
import com.articulator.reader.ArticleReader;
import com.articulator.reader.Reader;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    /**
     * Assumptions
     * -----------
     * 1. From the tech spec there appears to be no restrictions on technologies allowed and no stipulation on versions. I have therefore
     *      decided to use OpenCSV 2.0 because it better integrates with hibernate pojo lists as hibernate has been chosen as the ORM
     *      strategy
     *
     */

    private static Reader reader = ArticleReader.getInstance();
    private static ArticleDao articleDao = HibernateArticleDao.getInstance();
    private static Logger log;

    public static void main(String[] args) {

        createLogging();

        log.info("Starting database article restoration");

        List articles = null;
        try {
            articles = reader.readAll("src/main/resources/test.csv", new String[]{"ID", "ARTICLEID", "ATTRIBUTE", "VALUE", "LANGUAGE", "TYPE"});

        } catch (FileNotFoundException e) {
            log.error("Database dump not found");
            return;

        }

        if (articles!=null && articles.size()>0) {
            articleDao.saveAll(articles);

        }

        log.info("Restored database dump with " + articleDao.findAll().size() + " articles");


    }

    private static void createLogging() {
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(new PatternLayout("%d [%p|%c|%C{1}] %m%n"));
        consoleAppender.setThreshold(Level.DEBUG);
        consoleAppender.activateOptions();
        Logger.getRootLogger().addAppender(consoleAppender);

        Logger.getRootLogger().getLoggerRepository().getLogger("org.hibernate").setLevel(Level.INFO);

        log = Logger.getLogger(Main.class);


    }
}
