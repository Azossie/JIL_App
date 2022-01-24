package de.fhaachen.praxis.jil_app.dao.persona;

import android.widget.EditText;

public interface PersonaDao {

    public void firebaseInitializePersona();
    public void firebaseUpdatePersona( EditText personaEditText, String keyNode );
    public void firebaseUpdateActiveUsers();
    public void firebaseGetAllActiveUsers();
    public void firebaseSpeicherePersona();
    public void firebaseSetUserAsActive();
    public void firebaseSetUserAsNotActive();
    public void firebaseInitializeActiveUsers();
    public void firebaseGetAllParticipantsNames();

    public void disableDetailsEditText();
    public void enableDetailsEditText();
    public void setPersonaExerciseAsStarted();
    public void setPersonaExerciseAsNotStarted();
    public void startCountdown();
    public void updateCountDownView();
    public void updatePersonaDetailsView();
    public void showAllPersonaDetails();
    public void showActiveUsersNames();

}
