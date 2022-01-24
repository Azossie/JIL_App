package de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag;

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
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalworkshopBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentPackDigitalbagBinding;


public class DigitalworkshopFragment extends Fragment {

    private FragmentDigitalworkshopBinding binding;
    private FragmentPackDigitalbagBinding packDigitalbagBindingBinding;


    public static DigitalworkshopFragment newInstance() {
        return new DigitalworkshopFragment();
    }


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());

        if( getArguments() != null && getArguments().getString("backToView").equals("true") ){
            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
        }else{

            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_right));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDigitalworkshopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.imageDigitalbag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_left));

                Navigation.findNavController(v).navigate(R.id.digitalworkshop_digitalbag);
            }
        });

        binding.imageDigitalwiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_left));

                Navigation.findNavController(v).navigate(R.id.digitalwiki_definition);
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