package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentEditDevelopmentBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentExplanationBinding;

public class EditDevelopmentFragment extends Fragment {

    private FragmentEditDevelopmentBinding binding;

    private CategoryDao mCategoryDao;

    private String category;
    private String selectedTerm;


    public static EditDevelopmentFragment newInstance() {
        return new EditDevelopmentFragment();
    }

    public static EditDevelopmentFragment newInstance( String category, String selectedTerm) {
        return new EditDevelopmentFragment( category, selectedTerm);
    }

    public EditDevelopmentFragment(){ }

    public EditDevelopmentFragment( String category, String selectedTerm ) {

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

        binding = FragmentEditDevelopmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //to interact with firebase
        this.mCategoryDao = new CategoryDaoImpl( this, binding);

        //TODO show all term´s developments from firebase
        if( category != null && selectedTerm != null ){
            this.mCategoryDao.firebaseGetDevelopmentsAndShow( category, selectedTerm );
        }

        //return inflater.inflate(R.layout.fragment_explanation, container, false);
        return root;
    }

}