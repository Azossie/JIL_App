package de.fhaachen.praxis.jil_app.dao.categories;

public class CategoryTerm {


    private String term;
    private Post editedBy;

    public CategoryTerm() {

    }

    public CategoryTerm( String term, Post editedBy ) {

        this.term = term;
        this.editedBy = editedBy;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Post getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(Post editedBy) {
        this.editedBy = editedBy;
    }
}
