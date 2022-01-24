package de.fhaachen.praxis.jil_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.dao.user.User;
import de.fhaachen.praxis.jil_app.dao.user.UserDao;
import de.fhaachen.praxis.jil_app.dao.user.UserDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private  boolean mToolBarNavigationListenerIsRegistered = false;

    //private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //binding.logoutProgressBar.setVisibility(View.GONE);

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        drawer = binding.drawerLayout;
        drawerToggle = new ActionBarDrawerToggle( this, drawer, binding.appBarMain.toolbar,
                                                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.nav_jil, R.id.nav_getting_started, R.id.nav_digitalbag,
                R.id.nav_inde_message, R.id.nav_ring_lectures, R.id.nav_tutorials, R.id.nav_digital_workshop,
                R.id.nav_innovation_workshop, R.id.nav_chatroom, R.id.nav_assignment_delivery, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //show your custom menu_icon from the navigation drawer
        navigationView.setItemIconTintList(null);

        displayHomeUpOrHamburger();

        //singing out from the app
        //userDao = new UserDaoImpl();
       /* binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                //User currentUser = userDao.getCurrentUser();
                switch ( item.getItemId() ){

                    //User signs out
                    case R.id.nav_logout:

                        binding.logoutProgressBar.setVisibility(View.VISIBLE);

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
                                         System.out.println("Schoolname: " + schoolName);

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
                                                                 Intent mIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                 startActivity(mIntent);
                                                             }
                                                         }
                                                     });
                                                 }

                                             }
                                         });

                                     }

                                }
                            });



                        }else{

                        System.out.println("CurrentUser ist null! - User konnte nicht ausgeloggt werden!");
                        }
                        return true;
                }
                return false;
            }
        });
    */
    }


    private void displayHomeUpOrHamburger(){

        //Enables Up button only if there are entries in the back stack
        boolean upButton = getSupportFragmentManager().getBackStackEntryCount() > 0;

        if( upButton ){

            //can´t swipe left to open drawer
            drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            //remove hamburger
            drawerToggle.setDrawerIndicatorEnabled(false);

            //need listener for up Button

            if( !mToolBarNavigationListenerIsRegistered ){
                drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Doens´t have to be onBackpressed
                    //onBackPressed();
                    getSupportFragmentManager().popBackStackImmediate();
                }
            });
                mToolBarNavigationListenerIsRegistered = true;
            }

        }else{

            //enable swiping
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            // Show hamburger
            drawerToggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            drawerToggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}