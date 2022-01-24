package de.fhaachen.praxis.jil_app.dao.persona;

public class Persona {

    private String age;
    private String characterictics;
    private String expectations;
    private String hobbies;
    private String job;
    private String name;
    private String profil;
    private String todo;

    public Persona(){

        this.age = "";
        this.characterictics = "";
        this.expectations = "";
        this.hobbies = "";
        this.job = "";
        this.name = "";
        this.profil = "";
        this.todo = "";

    }

    public Persona( String age, String characterictics, String expectations, String hobbies,
                    String job, String name, String profil, String todo){

        this.age = age;
        this.characterictics = characterictics;
        this.expectations = expectations;
        this.hobbies = hobbies;
        this.job = job;
        this.name = name;
        this.profil = profil;
        this.todo = todo;

    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCharacterictics() {
        return characterictics;
    }

    public void setCharacterictics(String characterictics) {
        this.characterictics = characterictics;
    }

    public String getExpectations() {
        return expectations;
    }

    public void setExpectations(String expectations) {
        this.expectations = expectations;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
