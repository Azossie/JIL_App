package de.fhaachen.praxis.jil_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import de.fhaachen.praxis.jil_app.dao.user.User;
import de.fhaachen.praxis.jil_app.databinding.ActivityOtpactivityBinding;

public class OTPActivity extends AppCompatActivity {

    private String selectedSchoolname;
    private String firstname;
    private String lastname;
    private String enteredPhonenumber;
    private String codeBySystem;

    private User loginUser;

    private ActivityOtpactivityBinding binding;

    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //On Start the progressbar wont be seen
        binding.otpProgressBar.setVisibility(View.VISIBLE);

        //Get the entered phoneNumber by the user
        Intent intent = getIntent();

        //Get user-data through Object
        loginUser = (User)intent.getParcelableExtra("loginUser");

        enteredPhonenumber = loginUser.getPhoneNumber(); //intent.getStringExtra("phoneNumber");
        //Get the selected schoolname by the user
        selectedSchoolname = loginUser.getSchoolName(); //intent.getStringExtra("storedSchoolname");


        //Send Code to user
        sendCodeToUser(enteredPhonenumber);

        //If the BUtton "Überprüfen" is clicked
        binding.otpVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCode();
            }
        });

        //If the user needs to resend the code
        binding.otpResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtpCode(enteredPhonenumber);
            }
        });


    }

    /**
     * Resend a code to the user´s phonenumber
     * @param enteredPhonenumber is the number to which the code is to be sent
     */
    private void resendOtpCode(String enteredPhonenumber) {

        sendCodeToUser(enteredPhonenumber);
    }


    /**
     * Gets the entred code by the user
     * and terminate the process by trying to singIn with the entered Code
     *
     */
    private void checkCode() {

        String enteredOtpCode = binding.otpCode.getText().toString();

        if( enteredOtpCode.isEmpty() || enteredOtpCode.length() < 6 ){
            binding.otpCode.setError("Der eingegebener Code ist falsch!");
            binding.otpCode.requestFocus();
            return;
        }

        binding.otpProgressBar.setVisibility(View.VISIBLE);
        terminateProcess(enteredOtpCode);
    }

    /**
     * Sends code to a user´s phonenumber
     * @param enteredPhonenumber the Phonenumber to which the code is to be sent
     */
    private void sendCodeToUser(String enteredPhonenumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+49"+enteredPhonenumber,
                60,
                TimeUnit.SECONDS,
                this, //TaskExecutors.MAIN_THREAD
                mCallbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

            String sentCode = phoneAuthCredential.getSmsCode();

            if( sentCode != null ){
                binding.otpProgressBar.setVisibility(View.VISIBLE);
                terminateProcess(sentCode);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    };

    /**
     * The Methods compares the code entered by the user with the code sent
     * and go further with the login-process(signIn)
     * @param sentCode is the code entered by the user
     */
    private void terminateProcess( String sentCode ){
        binding.otpCode.setText( sentCode );
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, sentCode);
        signInUser(credential);
    }

    /**
     *Try to login the user with the entred code
     * If a wrong code is entred then show notification message
     * else go to JIL Nav
     * @param credential
     */
    private void signInUser( PhoneAuthCredential credential ){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                if( task.isSuccessful() ){


                    //store user´s data in the firebase and pass his data to the next Activity
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("schools").child(selectedSchoolname).child("users");

                    //store the user with the schoolname in at the level "users"
                    String name = loginUser.getFirstname()+" "+loginUser.getLastname();

                    //Save user´s role
                    rootNode.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("role").setValue(loginUser.getRole())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    if( task.isSuccessful() ){
                                        binding.otpProgressBar.setVisibility(View.GONE);
                                        //Save username
                                        rootNode.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(name)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if( task.isSuccessful() ){

                                                            //Save schoolname
                                                            rootNode.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .child("schoolname").setValue(loginUser.getSchoolName())
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                            if( task.isSuccessful() ){

                                                                                //Save user´s data marked by his uid as node
                                                                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(loginUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                        if( task.isSuccessful() ){

                                                                                            //Go to JIL-Mainactivity
                                                                                            Intent intent = new Intent( getApplicationContext(), MainActivity.class);
                                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                            startActivity(intent);

                                                                                        }
                                                                                    }
                                                                                });
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
                    binding.otpCode.setError("Der eingegebener Code ist falsch!");
                }
            }
        });

        firebaseAuth.setLanguageCode("de");
    }
}