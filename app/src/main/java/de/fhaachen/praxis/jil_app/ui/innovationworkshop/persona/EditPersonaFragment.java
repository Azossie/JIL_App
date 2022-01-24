package de.fhaachen.praxis.jil_app.ui.innovationworkshop.persona;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.persona.PersonaDao;
import de.fhaachen.praxis.jil_app.dao.persona.PersonaDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentEditPersonaBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentPersonaQuestionsBinding;


public class EditPersonaFragment extends Fragment {

    private FragmentEditPersonaBinding binding;

    private PersonaDao mPersonaDao;
    private EditText personaEditTexts [];
    private String keyNode [];

    private FirebaseDatabase rootNode;



    public static EditPersonaFragment newInstance() {
        return new EditPersonaFragment();
    }


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());

        if( getArguments() != null && getArguments().getString("backToView") != null && getArguments().getString("backToView").equals("true") ){
            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
        }else{

            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_right));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditPersonaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //zurück
        binding.backToCreatePersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.persona_create, bundle);
            }
        });

        //find wout which image has been selected(man or woman)
        if( getArguments() != null && getArguments().get("image") != null ){

            String image = getArguments().get("image").toString();

            if( image.equals("man") ){

                binding.imagePersona.setImageDrawable( getResources().getDrawable(R.drawable.man));

            } else if( image.equals("woman") ){

                binding.imagePersona.setImageDrawable( getResources().getDrawable(R.drawable.woman));
            }
        }

        //for the interction with firebase
        this.mPersonaDao = new PersonaDaoImpl( this, binding );

        //show the button 'START' only for teachers
        binding.buttonPesonaStart.setVisibility(View.GONE);
        rootNode = FirebaseDatabase.getInstance();
        rootNode.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String role = snapshot.getValue(String.class);

                if( role != null && role.equals("teacher") ){

                    binding.buttonPesonaStart.setVisibility(View.VISIBLE);
                    binding.buttonPesonaSpeichern.setEnabled(false);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Once clicked on the button 'START', the Persona should be filled with empty strings
        binding.buttonPesonaStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set the status of the exercise as 'STARTED' in firebase
                mPersonaDao.setPersonaExerciseAsStarted();

                //Disable Button 'START'
                binding.buttonPesonaStart.setEnabled(false);

                //start the countdown
                mPersonaDao.startCountdown();

            }
        });



        //to save all details from the edited persona
        binding.buttonPesonaSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPersonaDao.firebaseSpeicherePersona();
            }
        });


        //Initilize the persona-object( attributes will be filled with empty strings)
        mPersonaDao.firebaseInitializePersona();

        //show which part of the persona is already being edited
        //mPersonaDao.updatePersonaDetailsView();
        mPersonaDao.showAllPersonaDetails();

        mPersonaDao.updateCountDownView();


        return root;

    }

}