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
import de.fhaachen.praxis.jil_app.databinding.FragmentFillDigitalwikiIntroBinding;


public class FillDigitalwikiIntroFragment extends Fragment {

    private FragmentFillDigitalwikiIntroBinding binding;

    private TermsTableDao mTermsTableDao;

    public static FillDigitalwikiIntroFragment newInstance() {
        return new FillDigitalwikiIntroFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());

        if (getArguments() != null && getArguments().getString("backToView").equals("true")) {
            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
        } else {

            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_right));
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFillDigitalwikiIntroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Füllt das Digitalisierungswiki");

        //to interact with firebase
        this.mTermsTableDao = new TermsTableDaoImpl(this, binding);
        this.mTermsTableDao.firebaseGetUserTermsAndShow();

        //Zurück
        binding.backToWikiDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.digitalwiki_definition, bundle);
            }
        });

        //Weiter
        binding.gotoFillDigitalwikiAllTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle bundle = new Bundle();
                bundle.putString( "userTerms", binding.textWikiIntroYourTerms.getText().toString() );*/

                Navigation.findNavController(v).navigate(R.id.digitalwiki_all_terms);
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