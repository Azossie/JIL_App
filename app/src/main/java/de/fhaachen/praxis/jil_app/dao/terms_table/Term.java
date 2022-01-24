package de.fhaachen.praxis.jil_app.dao.terms_table;

import java.util.ArrayList;
import java.util.List;

import de.fhaachen.praxis.jil_app.dao.categories.Post;

public class Term {

    private String termName;
    private String explained; //"false" or "true"
    private String explanation;
    private String editedBy;
    private String editDatetime;
    private String categories;

    public Term( ) {}

    public Term( String termName ) {

        this.termName = termName;
        this.explained = "false";
        this.explanation = "";
        this.editedBy = "";
        this.editDatetime = "";
        this.categories = "";
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getExplained() {
        return explained;
    }

    public void setExplained(String explained) {
        this.explained = explained;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }


}
