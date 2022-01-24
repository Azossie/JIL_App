package de.fhaachen.praxis.jil_app.dao.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class UserDaoImpl implements  UserDao{

   /*private FirebaseUser firebaseUser;
    private FirebaseDatabase rootNode;

   private User currentUser;

    private String userSchoolname;
    private String currenUserId;*/

    public UserDaoImpl() {

    }

    public FirebaseUser getCurrentUser(){

        return FirebaseAuth.getInstance().getCurrentUser();
    }


    /*@Override
    public User getCurrentUser() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();

        final User[] user = new User[1];

        if( firebaseUser != null ){

            currenUserId = firebaseUser.getUid();
            System.out.println("User-id: " + currenUserId);

            //get the schoolname of the user
           rootNode.getReference("users").child(currenUserId).child("schoolname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
               @Override
               public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if( task.isSuccessful() ){

                        userSchoolname = task.getResult().getValue(String.class);

                        //get user´s data as Object
                        System.out.println("Schoolname: " + userSchoolname);
                        rootNode.getReference("schools").child(userSchoolname).child("users").child(currenUserId)
                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                if( task.isSuccessful() ){

                                    user[0] = task.getResult().getValue(User.class);
                                }

                            }
                        });
                    }
               }

           });

        }

        currentUser = user[0];

        return user[0];
    }*/


}
