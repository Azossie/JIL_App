package de.fhaachen.praxis.jil_app.dao.categories;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private String categoryTerms;

    public Category(){}

    public Category( String name ){

        this.name = name;
        this.categoryTerms = "";
    }

    public Category( String name, String categoryTerms ){

        this.name = name;
        this.categoryTerms = categoryTerms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryTerms() {
        return categoryTerms;
    }

    public void setCategoryTerms(String categoryTerms) {
        this.categoryTerms = categoryTerms;
    }
}
