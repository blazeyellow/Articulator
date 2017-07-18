package com.articulator.reader;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReaderTest {

    private static Reader reader = ArticleReader.getInstance();

    @Test
    public void testReadAll_ok() throws Exception {
        List articles = reader.readAll("src/main/resources/test.csv", new String[]{"ID", "ARTICLEID", "ATTRIBUTE", "VALUE", "LANGUAGE", "TYPE"});

        assertThat(articles.size(), equalTo(70));
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadAll_filenotfound() throws Exception {
        List articles = reader.readAll("src/main/resources/test2.csv", new String[]{"ID", "ARTICLEID", "ATTRIBUTE", "VALUE", "LANGUAGE", "TYPE"});

    }
}
