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
import de.fhaachen.praxis.jil_app.databinding.FragmentCreatePersonaBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentPersonaQuestionsBinding;

public class CreatePersonaFragment extends Fragment {

    private FragmentCreatePersonaBinding binding;


    public static CreatePersonaFragment newInstance() {
        return new CreatePersonaFragment();
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

        binding = FragmentCreatePersonaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //zurück ans weiter
        binding.backToPersonaInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.persona_instructions, bundle);
            }
        });

        //edit Persona
        binding.gotoEditPersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.persona_edit);
            }
        });

        binding.imageMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("image", "man");

                Navigation.findNavController(v).navigate(R.id.persona_edit, bundle);
            }
        });

        binding.imageWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("image", "woman");

                Navigation.findNavController(v).navigate(R.id.persona_edit, bundle);
            }
        });


        return root;

    }

}