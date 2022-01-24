package de.fhaachen.praxis.jil_app.dao.persona;

import android.os.CountDownTimer;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import de.fhaachen.praxis.jil_app.databinding.FragmentEditPersonaBinding;
import de.fhaachen.praxis.jil_app.ui.innovationworkshop.persona.EditPersonaFragment;

public class PersonaDaoImpl implements PersonaDao {

    private EditPersonaFragment editPersonaFragment;
    private FragmentEditPersonaBinding editPersonaBinding;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase rootNode;

    private Persona mPersona;

    private HashSet<String> allUsersNames;


    private static final long START_TIME_IN_MILLIS = 30000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;

    public PersonaDaoImpl( EditPersonaFragment editPersonaFragment, FragmentEditPersonaBinding editPersonaBinding ) {

        this.editPersonaBinding = editPersonaBinding;
        this.editPersonaFragment = editPersonaFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mPersona = new Persona();
        allUsersNames = new HashSet<>();


    }

    @Override
    public void firebaseInitializePersona() {

        //update the database with the new terms
        if( firebaseUser != null ){

            String userId = firebaseUser.getUid();
            //System.out.println("User-Id: " +  userId);

            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //Get the user´s schoolname
                    String schoolName = snapshot.getValue(String.class);

                    //System.out.println("Schoolname: " + schoolName);

                    //save persona´s details in the firebase
                    //only if the exercise is set as 'STARTED'
                    if( schoolName != null ){

                        rootNode.getReference().child("schools").child(schoolName)
                                .addChildEventListener( new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        //System.out.println("PreviousChildName: " + previousChildName);
                                        //System.out.println("Key: " + snapshot.getKey());

                                        if( snapshot.getKey().equals("persona-started")){

                                            String isStarted = snapshot.getValue(String.class);

                                            if( isStarted.equals("true") ){ // if the exercise has been set as started

                                                mPersona = new Persona(); //initialize persona´s details with empty strings

                                                rootNode.getReference().child("schools").child(schoolName).child("persona")
                                                        .setValue( mPersona ).addOnCompleteListener( new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                        //initialize the number of terms with 0
                                                        if( task.isSuccessful() ){

                                                            //set the countdown for the persona´s exercise in milliseconds
                                                            rootNode.getReference().child("schools").child(schoolName)
                                                                    .child("persona-countdown").setValue("30000")
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                            if( task.isSuccessful() ){

                                                                                //enable the EditTexts, so that the user can do input
                                                                                enableDetailsEditText();
                                                                                //INitialize the string of all users´s names
                                                                                firebaseInitializeActiveUsers();
                                                                                showActiveUsersNames();
                                                                            }
                                                                        }
                                                                    });

                                                        }
                                                    }
                                                });
                                            }

                                        }


                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                    }


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void firebaseUpdatePersona( EditText personaEditText, String keyNode ) {

        String keyValue = personaEditText.getText().toString().trim();

        firebaseSetUserAsActive();
        firebaseUpdateActiveUsers();

        if (firebaseUser.getUid() != null) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona")
                                    .child(keyNode).setValue(keyValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    if( task.isSuccessful() ){

                                        updatePersonaDetailsView(); //show other user the edited part in persona
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void firebaseUpdateActiveUsers() {

        firebaseGetAllParticipantsNames();

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();

            //get active users from firebase
            firebaseGetAllActiveUsers();

            rootNode.getReference().child("users").child(userId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    if( snapshot.getKey().equals("is-active") ){

                        //save this current user as active
                        rootNode.getReference().child("users").child(userId).child("username")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        String username = snapshot.getValue(String.class);
                                        allUsersNames.add(username); //save current user´s name

                                        rootNode.getReference().child("users").child(userId).child("role")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        String role = snapshot.getValue(String.class);

                                                        //save only student´s IDs
                                                        if( role.equals("student") ){ //if this user is a student

                                                            rootNode.getReference().child("users").child(userId).child("schoolname")
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                                            String schoolName = snapshot.getValue(String.class);

                                                                            String allUsers = "";
                                                                            Iterator<String> it = allUsersNames.iterator();

                                                                            while( it.hasNext() ){

                                                                                if( allUsers.length() == 0 ){
                                                                                    allUsers += (String)it.next();
                                                                                }else if( allUsers.length() > 0 ){

                                                                                    allUsers += ", " + (String)it.next();
                                                                                }

                                                                            }

                                                                            // System.out.println("ALl Users1 = " + allUsers);

                                                                            //save all users´s IDs in firebase
                                                                            rootNode.getReference().child("schools").child(schoolName)
                                                                                    .child("persona-active-users").setValue(allUsers);


                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                        }
                                                                    });

                                                        }else{ // if user is not a student

                                                            //remove his id from the set
                                                            allUsersNames.remove(username);

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });




                    }
                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }


    }

    @Override
    public void firebaseGetAllActiveUsers() {

        if (firebaseUser != null) {

            String userId = firebaseUser.getUid();

            rootNode.getReference().child("schools").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    String key = snapshot.getKey(); //in this case, it´s the 'schoolname'

                    //System.out.println("Get ALl Users - key: " +  key);

                    rootNode.getReference().child("schools").child(key).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            if( snapshot.getKey().equals("persona-active-users") ){

                                //get all users from firebase
                                String allUsers = snapshot.getValue(String.class);

                                if( allUsers != null && allUsers.length() > 0 ){

                                    String [] userIds = allUsers.split(", ");

                                    for( String s: userIds ){

                                        allUsersNames.add(s);
                                    }
                                }

                                //System.out.println("ALl Users2 = " + allUsers);


                            }
                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void firebaseSpeicherePersona() {

        String age = editPersonaBinding.editTextAge.getText().toString().trim();
        String characterictics = editPersonaBinding.editTextCharacteristics.getText().toString().trim();
        String expectations  = editPersonaBinding.editTextExpectations.getText().toString().trim();
        String hobbies = editPersonaBinding.editTextHobbies.getText().toString().trim();
        String job = editPersonaBinding.editTextJob.getText().toString().trim();
        String name = editPersonaBinding.editTextName.getText().toString().trim();
        String profil = editPersonaBinding.editTextProfil.getText().toString().trim();
        String todo = editPersonaBinding.editTextTodo.getText().toString().trim();

        mPersona = new Persona( age, characterictics, expectations, hobbies, job, name, profil, todo );

        firebaseSetUserAsActive();
        firebaseUpdateActiveUsers();

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona")
                                    .setValue(mPersona).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    if(task.isSuccessful() ){

                                        showAllPersonaDetails();
                                        //disableDetailsEditText();
                                        //showActiveUsersNames();

                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public void firebaseSetUserAsActive() {

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("is-active").setValue("true");

        }

    }

    @Override
    public void firebaseSetUserAsNotActive() {

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("is-active").removeValue();

        }

    }

    @Override
    public void firebaseInitializeActiveUsers() {

        allUsersNames = new HashSet<>();

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona-active-users").setValue("");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public void firebaseGetAllParticipantsNames() {

        if (firebaseUser.getUid() != null) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona-active-users")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    String names = snapshot.getValue(String.class);
                                    String userNames [] = names.split(", ");

                                    for( String un : userNames ){
                                        allUsersNames.add(un);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public void disableDetailsEditText() {

        //disable all EditText(s)
        editPersonaBinding.editTextName.setEnabled(false);
        editPersonaBinding.editTextAge.setEnabled(false);
        editPersonaBinding.editTextJob.setEnabled(false);
        editPersonaBinding.editTextProfil.setEnabled(false);
        editPersonaBinding.editTextExpectations.setEnabled(false);
        editPersonaBinding.editTextCharacteristics.setEnabled(false);
        editPersonaBinding.editTextTodo.setEnabled(false);
        editPersonaBinding.editTextHobbies.setEnabled(false);
    }

    @Override
    public void enableDetailsEditText() {

        editPersonaBinding.editTextName.setEnabled(true);
        editPersonaBinding.editTextAge.setEnabled(true);
        editPersonaBinding.editTextJob.setEnabled(true);
        editPersonaBinding.editTextProfil.setEnabled(true);
        editPersonaBinding.editTextExpectations.setEnabled(true);
        editPersonaBinding.editTextCharacteristics.setEnabled(true);
        editPersonaBinding.editTextTodo.setEnabled(true);
        editPersonaBinding.editTextHobbies.setEnabled(true);
    }


    @Override
    public void setPersonaExerciseAsStarted() {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona-started")
                                    .setValue("true");

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public void setPersonaExerciseAsNotStarted() {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona-started")
                                    .setValue("false");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }


    @Override
    public void startCountdown() {

        mTimeLeftInMillis = START_TIME_IN_MILLIS;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

                //TODO update milliseconds in firebase
                if( firebaseUser.getUid() != null ) {

                    String userId = firebaseUser.getUid();
                    rootNode.getReference().child("users").child(userId).child("schoolname")
                            .addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    String schoolName = snapshot.getValue(String.class);
                                    String leftTimeInMillis = String.valueOf(mTimeLeftInMillis);

                                    rootNode.getReference().child("schools").child(schoolName).child("persona-countdown")
                                            .setValue(leftTimeInMillis).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            //update the view for all users with the current left minutes or seconds
                                            if( task.isSuccessful() ){

                                                updateCountDownView();
                                            }

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                }


            }

            @Override
            public void onFinish() {

                editPersonaBinding.buttonPesonaStart.setEnabled(true);
                setPersonaExerciseAsNotStarted();
            }

        }.start();

    }


    @Override
    public void updateCountDownView() {

        //get milliseconds from firebase and show it to users
        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            if( schoolName != null ){

                                rootNode.getReference().child("schools").child(schoolName).child("persona-countdown")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                String currentLeftTime = snapshot.getValue(String.class);
                                                long currentLeftTimeInMillis = Long.parseLong(currentLeftTime);

                                                int minutes = (int) (currentLeftTimeInMillis / 1000) / 60;
                                                int seconds = (int) (currentLeftTimeInMillis / 1000) % 60;

                                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                                                String restzeit = "Restzeit: ";
                                                restzeit += timeLeftFormatted;
                                                editPersonaBinding.textCountdown.setText( restzeit );

                                                //if the countdowm stop
                                                if( currentLeftTimeInMillis < 1000 ){ //|| currentLeftTimeInMillis == START_TIME_IN_MILLIS

                                                    disableDetailsEditText();
                                                    firebaseSetUserAsNotActive();

                                                }else{

                                                    //enableDetailsEditText();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }


    }

    @Override
    public void updatePersonaDetailsView() {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona")
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                            switch( snapshot.getKey() ){

                                                case "name":
                                                    editPersonaBinding.editTextName.setText( snapshot.getValue(String.class) );
                                                    break;

                                                case "age":
                                                    editPersonaBinding.editTextAge.setText( snapshot.getValue(String.class) );
                                                    break;

                                                case "job":
                                                    editPersonaBinding.editTextJob.setText( snapshot.getValue(String.class) );
                                                    break;

                                                case "profil":
                                                    editPersonaBinding.editTextProfil.setText( snapshot.getValue(String.class) );
                                                    break;

                                                case "todo":
                                                    editPersonaBinding.editTextTodo.setText( snapshot.getValue(String.class) );
                                                    break;

                                                case "expectations":
                                                    editPersonaBinding.editTextExpectations.setText( snapshot.getValue(String.class) );
                                                    break;

                                                case "characteristics":
                                                    editPersonaBinding.editTextCharacteristics.setText( snapshot.getValue(String.class) );
                                                    break;

                                                case "hobbies":
                                                    editPersonaBinding.editTextHobbies.setText( snapshot.getValue(String.class) );
                                                    break;

                                                default:
                                                    break;

                                            }
                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public void showAllPersonaDetails() {

        //show all details of the persona
        //disable all EditTexts
        //and show the name of the participants

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            //get the persona-object
                            if( schoolName != null ){

                                rootNode.getReference().child("schools").child(schoolName).child("persona")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                mPersona = snapshot.getValue(Persona.class);

                                                editPersonaBinding.editTextName.setText( mPersona.getName() );
                                                editPersonaBinding.editTextAge.setText( mPersona.getAge() );
                                                editPersonaBinding.editTextJob.setText( mPersona.getJob() );
                                                editPersonaBinding.editTextHobbies.setText( mPersona.getHobbies() );
                                                editPersonaBinding.editTextCharacteristics.setText( mPersona.getCharacterictics() );
                                                editPersonaBinding.editTextExpectations.setText( mPersona.getExpectations() );
                                                editPersonaBinding.editTextProfil.setText( mPersona.getProfil() );
                                                editPersonaBinding.editTextTodo.setText( mPersona.getTodo() );

                                                rootNode.getReference().child("schools").child(schoolName).child("persona-started")
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                                String personaStatus = snapshot.getValue(String.class);

                                                                if( personaStatus.equals("false") ){

                                                                    disableDetailsEditText();
                                                                }else if( personaStatus.equals("true") ){
                                                                    enableDetailsEditText();
                                                                }

                                                                showActiveUsersNames();
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public void showActiveUsersNames() {

        //get all user´s names
        if (firebaseUser.getUid() != null) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("persona-active-users")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            String names = snapshot.getValue(String.class);

                                            editPersonaBinding.activeUsers.setText("Bearbeitet von " + names);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }



    }


}
