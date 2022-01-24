package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;

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
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentCategoryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiTermExplanationBinding;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;

    private CategoryDao mCategoryDao;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());

        if (getArguments() != null && getArguments().getString("backToView") != null && getArguments().getString("backToView").equals("true")) {
            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
        } else {

            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_right));
        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Füllt das Digitalisierungswiki");

        //show the category´s name
        String categoryName = null;
        String termName = null;
        if( getArguments() != null && getArguments().getStringArray("categoryAndTerm") != null ){

            categoryName = getArguments().getStringArray("categoryAndTerm")[0];
            termName = getArguments().getStringArray("categoryAndTerm")[1];

            binding.textCategoryName.setText(categoryName);

        }

        //to interact with firebase
        this.mCategoryDao = new CategoryDaoImpl( this, binding);
        //Get all term´s categories from firebase  and show them as buttons
        //this.mCategoryDao.firebaseGetTermsCategoriesAndShow();
        this.mCategoryDao.firebaseGetCategoryTerms( categoryName );


        //Zurück to the previous view
        String finalTermName = termName;
        binding.backToTermExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putStringArray("categoryAndTerm", new String[]{ finalTermName, null, "true"});

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.digitalwiki_all_terms, bundle);
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