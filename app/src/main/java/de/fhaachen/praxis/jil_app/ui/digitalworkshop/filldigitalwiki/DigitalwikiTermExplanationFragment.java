package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiAllTermsBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiTermExplanationBinding;

public class DigitalwikiTermExplanationFragment extends Fragment {

    private FragmentDigitalwikiTermExplanationBinding binding;

    private CategoryDao mCategoryDao;

    public static DigitalwikiTermExplanationFragment newInstance() {
        return new DigitalwikiTermExplanationFragment();
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

        if (getArguments() != null && getArguments().getStringArray("categoryAndTerm") != null && getArguments().getStringArray("categoryAndTerm") .length == 3 && getArguments().getStringArray("categoryAndTerm")[2].equals("true")) {
            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_left));
        } else {

            setEnterTransition(inflater.inflateTransition(R.transition.slide_in_right));
        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDigitalwikiTermExplanationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Füllt das Digitalisierungswiki");

        //show the term´s name
        String term = null;
        String categories = null;
        if( getArguments() != null && getArguments().getStringArray("categoryAndTerm") != null ){

            String termAndCategories [] = getArguments().getStringArray("categoryAndTerm");
            term = termAndCategories[0];
            categories = termAndCategories[1];

            binding.termName.setText(term);

        }

        //to interact with firebase
        this.mCategoryDao = new CategoryDaoImpl( this, binding);
        //Get term´s explanation
        this.mCategoryDao.showTermExplanationAndShow( term );
        //Get all term´s categories from firebase  and show them as buttons
        //this.mCategoryDao.firebaseGetTermsCategoriesAndShow();
        this.mCategoryDao.showTermsCategories( categories, term );

        final String[] explanation = {""};
        if( binding.buttonSaveExplanation.getVisibility() == View.VISIBLE ){

            binding.buttonSaveExplanation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //save the inserted explanation
                    String mTerm = binding.termName.getText().toString().trim();
                    explanation[0] = binding.termEdit.getText().toString().trim();
                    mCategoryDao.firebaseSaveTermExplanation( mTerm, explanation[0]);
                }
            });
        }

        //open show all category´s terms once click  on a category
        //except for the floating button
        for( int i = 1; i < binding.allCategories.getChildCount(); i++ ){

            View view = binding.allCategories.getChildAt(i);

            if( view instanceof  TableRow ){

                TableRow tbr = (TableRow) view;

                for( int j = 0; j < tbr.getChildCount(); j++ ){

                    Button btn = (Button)(((TableRow)(binding.allCategories.getChildAt(i))).getChildAt(j));
                    String categoryName = btn.getText().toString().trim();
                    String finalTerm = term;
                    ((Button)(((TableRow)(binding.allCategories.getChildAt(i))).getChildAt(j)))
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //pass the category´s term (which is actually the button´s name)
                                    //System.out.println("Button clicked!2");
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArray("categoryAndTerm", new String [] {categoryName, finalTerm});
                                    Navigation.findNavController(v).navigate(R.id.digitalwiki_show_category, bundle);

                                }
                            });

                }
            }
        }


        //Zurück to the previous view
        binding.backToAllTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                Navigation.findNavController(v).navigate(R.id.digitalwiki_all_terms, bundle);
            }
        });

        //Selectd one term to insert an explanation


        //Add new Term to the Category
        binding.buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( binding.termExplanation.getVisibility() == View.VISIBLE ){

                    explanation[0] = binding.termExplanation.getText().toString().trim();
                }
                //Open dialog to insert the term
                openCategoryInputDialog(binding.allCategories, getContext(), mCategoryDao, binding.termName.getText().toString(), explanation[0] );
            }
        });



        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openCategoryInputDialog(TableLayout buttonsTableView , Context mContext, CategoryDao mCategoryDao, String termName, String explanation ){

        CategorieInputDialogFragment termInputDialog = new CategorieInputDialogFragment( buttonsTableView , mContext, mCategoryDao, termName, explanation );
        termInputDialog.show(this.getParentFragmentManager(), "Kategorie eingeben Dialog");
    }


}