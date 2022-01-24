package de.fhaachen.praxis.jil_app.dao.user;

public enum  SchoolNames {

    BISCHOEFLICHE_LIEBFRAUENSCHULE("Bischöfliche Liebfrauenschule", "bischoefliche-liebfrauenschule"),
    EUROPASCHULE_LANGERWEHE("Europaschule Langerwehe", "europaschule-langerwehe"),
    GYMNASIUM_HAUS_OVERBACH("Gymnasium Haus Overbach", "gymnasium-haus-overbach"),
    INDA_GYMNASIUM("Inda Gymnasium", "inda-gymnasium"),
    KAISER_KARLS_GYMNASIUM("Kaiser-Karls-Gymnasium", "kaiser-karls-gymnasium"),
    WALADSCHULE_ESCHWEILER("Waldschule Eschweiler", "waldschule-eschweiler");


    String viewName;
    String storedName;

    SchoolNames( String viewName, String storedName ){
        this.viewName = viewName;
        this.storedName = storedName;
    }


    public String getViewName() {
        return viewName;
    }

    public String getStoredName() {
        return storedName;
    }
}
