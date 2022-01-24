package de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.databinding.FragmentPackDigitalbagBinding;


public class PackDigitalbagFragment extends Fragment {


    public static PackDigitalbagFragment newInstance() {
        return new PackDigitalbagFragment();
    }
    private FragmentPackDigitalbagBinding binding;

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

        binding = FragmentPackDigitalbagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Packt euren Digitalisierungskoffer");

       binding.backToDigitalworkshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.nav_digital_workshop, bundle);
            }
        });

       binding.gotoMethode101Intro.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Navigation.findNavController(v).navigate(R.id.digitalworkshop_digitalbag_method101_intro);
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