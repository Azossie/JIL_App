package de.fhaachen.praxis.jil_app.ui.innovationworkshop.persona;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.databinding.FragmentPersonaInstructionsBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentPersonaQuestionsBinding;

public class PersonaInstructionsFragment extends Fragment {

    private FragmentPersonaInstructionsBinding binding;


    public static PersonaInstructionsFragment newInstance() {
        return new PersonaInstructionsFragment();
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

        binding = FragmentPersonaInstructionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //zurück ans weiter
        binding.backToPersonaQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.persona_questions, bundle);
            }
        });

        binding.gotoCreatePersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.persona_create);
            }
        });


        return root;

    }


}