package de.fhaachen.praxis.jil_app.dao.categories;

public class Post {

    private String editDatetime;
    private String explanation;
    private String username;

    public Post() {

        editDatetime = "";
        explanation = "";
        username = "";
    }

    public Post( String editDatetime, String explanation, String username ){

        this.editDatetime = editDatetime;
        this.explanation = explanation;
        this.username = username;
    }

    public String getEditDatetime() {
        return editDatetime;
    }

    public void setEditDatetime(String editDatime) {
        this.editDatetime = editDatime;
    }



    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
