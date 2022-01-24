package de.fhaachen.praxis.jil_app.dao.user;

import com.google.firebase.auth.FirebaseUser;

public interface UserDao {

    //public User getCurrentUser();

    public FirebaseUser getCurrentUser();
}

