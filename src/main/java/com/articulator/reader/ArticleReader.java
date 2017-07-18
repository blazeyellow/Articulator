package com.articulator.reader;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import com.articulator.pojo.Article;
import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class ArticleReader implements Reader {

    private static Logger log = Logger.getLogger(ArticleReader.class);

    private static ArticleReader articleReader = new ArticleReader();

    private ArticleReader() {}

    public static Reader getInstance() {
        return articleReader;

    }
    @Override
    public List readAll(String filename, String[] columns) throws FileNotFoundException {

        log.debug("Starting readAll()...");

        ColumnPositionMappingStrategy<Article> strategy = new ColumnPositionMappingStrategy<Article >();
        strategy.setType(Article.class);
        strategy.setColumnMapping(columns);

        CsvToBean<Article> csv = new CsvToBean<Article>() {

            @Override
            protected Object convertValue(String value, PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {

                // TODO integers do not seem to be dealt with as integers but as we know certain columns will be integers when persisted, we ensure they are converted
                // to integers and if no real value can be determined from the string value, replace with a zero

                if (prop.getName().equals("id") || prop.getName().equals("articleId") || prop.getName().equals("language") || prop.getName().equals("type")) {
                    Integer newValue = null;
                    try {
                        newValue = Integer.parseInt(value);

                    } catch (NumberFormatException e) {

                    }

                    if(newValue==null) {
                        log.warn("'" + prop.getName() + "' is non-numeric but should not be. Replaced with zero.");
                        return 0;

                    }

                    return newValue;

                }

                return super.convertValue(value, prop);

            }
        };

        return csv.parse(strategy, new CSVReader(new FileReader(filename), ',', '"', 1));

    }
}
