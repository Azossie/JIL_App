package de.fhaachen.praxis.jil_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhaachen.praxis.jil_app.dao.user.SchoolNames;
import de.fhaachen.praxis.jil_app.dao.user.User;
import de.fhaachen.praxis.jil_app.dao.user.UserDao;
import de.fhaachen.praxis.jil_app.dao.user.UserDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private String enteredFirstname;
    private String enteredLastname;
    private String enteredPhonenumber;
    private String selectedSchoolname;

    private User currentUser;

    private FirebaseUser firebaseUser;
    //private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //userDao = new UserDaoImpl();

        //Access current user from database
        //if the user is already signed in
        if ( firebaseUser != null ) { //
            //just give ihm acces to the app
            Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mIntent);

        } else {
            binding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());


            //Show the spinner with alls schools names
            //Initializing a String Array
        /*String[] schools = new String[]{
                "Schule auswählen",
                "Bischöfliche Liebfrauenschule",
                "Europaschule Langerwehe",
                "Gymnasium Haus Overbach",
                "Inda-Gymnasium",
                "Kaiser-Karls-Gymnasium",
                "Waldschule Eschweiler"
        };*/

            String[] schools = new String[7];
            schools[0] = "Schule auswählen";
            int i = 1;
            for (SchoolNames sName : SchoolNames.values()) {
                schools[i] = sName.getViewName();
                i++;
            }

            Spinner spinner = (Spinner) findViewById(R.id.login_spinner);

            final List<String> schoolsList = new ArrayList<>(Arrays.asList(schools));
            // Create an ArrayAdapter using the string array and a default spinner layout
            //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schools, android.R.layout.simple_spinner_item);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schoolsList) {

                //disable the first item
                @Override
                public boolean isEnabled(int position) {
                    //return super.isEnabled(position);
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                //Set the first item to grey color
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }

            };

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedSchoolname = (String) parent.getItemAtPosition(position);
                    // If user change the default selection
                    // First item is disable and it is used for hint
                    if (position > 0) {
                        //get the stored schoolname
                        for (SchoolNames sn : SchoolNames.values()) {

                            if (sn.getViewName().equals(selectedSchoolname)) {
                                selectedSchoolname = sn.getStoredName();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //show verfication view after clicking on the button "weiter"
            binding.continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lastCharacterFromPhone = null;
                    enteredPhonenumber = binding.loginPhonenumber.getText().toString().trim();
                    enteredPhonenumber = enteredPhonenumber.substring(1);
                    lastCharacterFromPhone = enteredPhonenumber.substring(enteredPhonenumber.length()-1);

                    enteredFirstname = binding.loginFirstname.getText().toString().trim();
                    enteredLastname = binding.loginLastname.getText().toString().trim();

                    //Phonenummer for Teacher should end with a '#'
                    //In this case the user is a teacher
                    if( lastCharacterFromPhone.equals("#") ){

                        enteredPhonenumber = enteredPhonenumber.substring(0, enteredPhonenumber.length()-1);
                        //Create the user as teacher
                        currentUser = new User(enteredFirstname, enteredLastname, enteredPhonenumber, selectedSchoolname, "teacher");

                    }else{  //if not the user is a student

                        //Create the user as student
                        currentUser = new User(enteredFirstname, enteredLastname, enteredPhonenumber, selectedSchoolname, "student");
                    }



                    Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                    //intent.putExtra("phoneNumber", enteredPhonenumber);
                    //intent.putExtra("storedSchoolname", selectedSchoolname);
                    //Pass the user-object to the OTPActivity
                    intent.putExtra("loginUser", currentUser);

                    startActivity(intent);
                }
            });

        }//End of first if

    }
}