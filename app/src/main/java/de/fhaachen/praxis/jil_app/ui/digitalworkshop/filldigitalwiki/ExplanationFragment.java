package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiExplanationInputBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentExplanationBinding;

public class ExplanationFragment extends Fragment {

    private FragmentExplanationBinding binding;

    private CategoryDao mCategoryDao;

    private String category;
    private String selectedTerm;

    public static ExplanationFragment newInstance() {
        return new ExplanationFragment();
    }

    public static ExplanationFragment newInstance( String category, String selectedTerm) {
        return new ExplanationFragment( category, selectedTerm);
    }

    public ExplanationFragment(){}

    public ExplanationFragment( String category, String selectedTerm ) {

        this.category = category;
        this.selectedTerm = selectedTerm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentExplanationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //to interact with firebase
        this.mCategoryDao = new CategoryDaoImpl( this, binding);

        //show all term´s explanations from firebase
        if( category != null && selectedTerm != null ){
            this.mCategoryDao.firebaseGetExplanationsAndShow( category, selectedTerm );
        }


        //return inflater.inflate(R.layout.fragment_explanation, container, false);
        return root;
    }

}