package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentEditBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentExplanationBinding;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;

    private CategoryDao mCategoryDao;

    private String category;
    private String selectedTerm;

    public EditFragment( ) {
    }

    public EditFragment( String category, String selectedTerm ) {

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

        binding = FragmentEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //to interact with firebase
        this.mCategoryDao = new CategoryDaoImpl( this, binding);

        //save the given explanation in firebase
        binding.buttonSaveExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String explanation = binding.textEditTerm.getText().toString().trim();

                mCategoryDao.firebaseUpdateExplanation( category, selectedTerm, explanation );

                //show the "ERKLÄRUNG"-tab
                Fragment fragment = new ExplanationFragment( category, selectedTerm );
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.tab_content, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });



        return root;
        //return inflater.inflate(R.layout.fragment_edit, container, false);
    }

}