package de.fhaachen.praxis.jil_app.ui.logout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.LoginActivity;
import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.databinding.ActivityMainBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentLoginBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentLogoutBinding;
import de.fhaachen.praxis.jil_app.ui.indemessage.IndeMessageFragment;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private FirebaseUser firebaseUser;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLogoutBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //if the user is signed in
        if( firebaseUser != null ){ //user != null
            //FirebaseAuth.getInstance().signOut();

            //then delete the user from the database
            //TODO Delete User form the database
            System.out.println("UserId - " + firebaseUser.getUid());
            FirebaseDatabase.getInstance().getReference("users").child( firebaseUser.getUid())
                    .child("schoolname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if( task.isSuccessful() ){
                        // remove user-data from the school-section
                        String schoolName = task.getResult().getValue(String.class);
                        //System.out.println("Schoolname: " + schoolName);

                        if( schoolName != null ){

                            FirebaseDatabase.getInstance().getReference("schools").child(schoolName)
                                    .child("users").child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    if( task.isSuccessful() ) {

                                        // and remove user-data from the users-section with given name
                                        FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    //Signing out user
                                                    FirebaseAuth.getInstance().signOut();

                                                    //then go to the login view
                                                    Intent mIntent = new Intent(getActivity(), LoginActivity.class);
                                                    startActivity(mIntent);
                                                }
                                            }
                                        });
                                    }

                                }
                            });


                        }else{

                            FirebaseAuth.getInstance().signOut();

                            //then go to the login view
                            Intent mIntent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(mIntent);
                        }

                    }

                }
            });



        }else{

            System.out.println("CurrentUser ist null! - User konnte nicht ausgeloggt werden!");
        }


        //return inflater.inflate(R.layout.fragment_logout, container, false);

        return root;
    }
}