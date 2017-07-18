package com.articulator.dao;

import com.articulator.pojo.Article;

import java.util.List;

public interface ArticleDao {

    public void save(Article article);

    public void saveAll(List<Article> articles);

    public List findAll();

}
