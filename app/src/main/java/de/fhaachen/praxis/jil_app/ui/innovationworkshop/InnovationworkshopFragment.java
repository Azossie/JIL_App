package de.fhaachen.praxis.jil_app.ui.innovationworkshop;

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
import de.fhaachen.praxis.jil_app.databinding.FragmentInnovationworkshopBinding;


public class InnovationworkshopFragment extends Fragment {

   /* public static InnovationworkshopFragment newInstance() {
        return new InnovationworkshopFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_innovationworkshop, container, false);
    }*/

    private FragmentInnovationworkshopBinding binding;


    public static InnovationworkshopFragment newInstance() {
        return new InnovationworkshopFragment();
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

        binding = FragmentInnovationworkshopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //go to the next steps for etablishing a persona
        binding.imagePersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_left));

                Navigation.findNavController(v).navigate(R.id.persona_definition);
            }
        });

        binding.imageMascotAsking2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_left));

                Navigation.findNavController(v).navigate(R.id.persona_definition);
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}