package de.fhaachen.praxis.jil_app.dao.terms_table;

public class Cell {

    private String position;
    private String postedBy;
    private String term;
    private boolean inBearbeitung;
    private boolean bearbeitet;

    public Cell() {
    }

    public Cell(String position, String postedBy, String term) {
        this.position = position;
        this.postedBy = postedBy;
        this.term = term;
        this.inBearbeitung = false;
        this.bearbeitet = false;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public boolean isInBearbeitung() {
        return inBearbeitung;
    }

    public void setInBearbeitung(boolean inBearbeitung) {
        this.inBearbeitung = inBearbeitung;
    }

    public boolean isBearbeitet() {
        return bearbeitet;
    }

    public void setBearbeitet(boolean bearbeitet) {
        this.bearbeitet = bearbeitet;
    }
}
