package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.terms_table.TermsTableDao;
import de.fhaachen.praxis.jil_app.dao.terms_table.TermsTableDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentFillDigitalwikiCategoriesBinding;


public class FillDigitalwikiCategoriesFragment extends Fragment {

    private FragmentFillDigitalwikiCategoriesBinding binding;

    private TermsTableDao mTermsTableDao;


    public static FillDigitalwikiCategoriesFragment newInstance() {
        return new FillDigitalwikiCategoriesFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());

        if (getArguments() != null && getArguments().getString("backToView") != null && getArguments().getString("backToView") .equals("true")) {
            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
        } else {

            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_right));
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFillDigitalwikiCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Füllt das Digitalisierungswiki");

        //to interact with firebase
        this.mTermsTableDao = new TermsTableDaoImpl(this, binding);
        this.mTermsTableDao.firebaseGetUserTermsAndShow();


        binding.backToDigitalwikiIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.digitalwiki_intro, bundle);
            }
        });

        binding.buttonDigitalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.digitalwiki_category_digital_data);
            }
        });

        binding.buttonNetworking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.digitalwiki_category_networking);
            }
        });

        binding.buttonAutomatisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.digitalwiki_category_automatisation);
            }
        });

        binding.buttonDigitalClientAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.digitalwiki_category_digital_client_access);
            }
        });




        //show the user´s terms
       /* if( getArguments() != null ){

            String userTerms = getArguments().getString("userTerms");
            binding.textWikiCategoriesYourTerms.setText(userTerms);
        }*/

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}