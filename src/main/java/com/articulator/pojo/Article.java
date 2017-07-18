package com.articulator.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ARTICLE")
public class Article {

    @Id
    private Integer id;
    private Integer articleId;
    private String attribute;
    private String value;
    private Integer language;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", articleId='" + articleId + '\'' +
                ", attribute='" + attribute + '\'' +
                ", value='" + value + '\'' +
                ", language='" + language + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
