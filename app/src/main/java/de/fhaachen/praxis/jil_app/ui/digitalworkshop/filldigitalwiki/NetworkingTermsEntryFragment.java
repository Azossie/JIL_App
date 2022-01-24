package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDaoImpl;
import de.fhaachen.praxis.jil_app.dao.terms_table.TermsTableDao;
import de.fhaachen.praxis.jil_app.dao.terms_table.TermsTableDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalDataTermsEntryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentNetworkingTermsEntryBinding;

public class NetworkingTermsEntryFragment extends Fragment {

    private FragmentNetworkingTermsEntryBinding binding;

    private CategoryDao mCategoryDao;
    private TermsTableDao mTermsTableDao;


    public static NetworkingTermsEntryFragment newInstance() {
        return new NetworkingTermsEntryFragment();
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

        binding = FragmentNetworkingTermsEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Füllt das Digitalisierungswiki");

        //to interact with firebase
        this.mTermsTableDao = new TermsTableDaoImpl(this, binding);
        this.mTermsTableDao.firebaseGetUserTermsAndShow();

        this.mCategoryDao = new CategoryDaoImpl( this, binding);
        //show categories´s terms as button, so user can give an explanation after a click
        this.mCategoryDao.firebaseGetCategoryTermsAndShow("vernetzung");


        //show the user´s terms
       /* if( getArguments() != null ){

            String userTerms = getArguments().getString("userTerms");
            binding.textDigitalDataYourTerms.setText(userTerms);
        }*/

        //Zurück to the previous view
        binding.backToCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.digitalwiki_categories, bundle);
            }
        });

        //Selectd one term to insert an explanation


        //Add new Term to the Category
        binding.buttonAddCategoryTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open dialog to insert the term
                openCategoryTermInputDialog(binding.categoryTermsTable, getContext(), mCategoryDao, "vernetzung");
            }
        });



        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void openCategoryTermInputDialog(TableLayout buttonsTableView , Context mContext, CategoryDao mCategoryDao, String categoryName ){

        CategorieTermInputDialogFragment termInputDialog = new CategorieTermInputDialogFragment( buttonsTableView , mContext, mCategoryDao, categoryName);
        termInputDialog.show(this.getParentFragmentManager(), "Kategorie-Begriff eingeben Dialog");
    }

}