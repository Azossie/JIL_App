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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import com.google.android.material.tabs.TabLayout;

import de.fhaachen.praxis.jil_app.R;

import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiExplanationInputBinding;

public class DigitalwikiExplanationInputFragment extends Fragment {

    private FragmentDigitalwikiExplanationInputBinding binding;

    private CategoryDao mCategoryDao;
    private String category;
    private String selectedTerm;



    public static DigitalwikiExplanationInputFragment newInstance() {
        return new DigitalwikiExplanationInputFragment();
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

        binding = FragmentDigitalwikiExplanationInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Füllt das Digitalisierungswiki");

        //to interact with firebase
        this.mCategoryDao = new CategoryDaoImpl( this, binding);

        //Zurück to the previous view
        binding.backToCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( getArguments() != null && getArguments().getStringArray("categoryTerm") != null ){

                    String categoryTerm [] = getArguments().getStringArray("categoryTerm");

                    System.out.println("DigitalWIkiExplantion - CategoryTerm = " + categoryTerm[0]);
                    System.out.println("DigitalWIkiExplantion - CategoryName = " + categoryTerm[1]);

                    Bundle bundle = new Bundle();
                    bundle.putString("backToView", "true");

                    TransitionInflater inflater = TransitionInflater.from(requireContext());
                    setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));

                    switch ( categoryTerm[1] ){

                        case "digitale-daten":
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_category_digital_data, bundle);
                            break;

                        case "vernetzung":
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_category_networking, bundle);
                            break;

                        case "automatisierung":
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_category_automatisation, bundle);
                            break;

                        case "digitaler-kundenzugang":
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_category_digital_client_access, bundle);
                            break;

                        default: break;
                    }

                }


               ;


            }
        });

        //show your custom tab_icon from the tablayout
        binding.tablayoutDigitalwikiExplanation.setTabIconTint(null);

        //show the category´s terms
       if( getArguments() != null && getArguments().getStringArray("categoryTerm") != null ){

            String categoryTerm = getArguments().getStringArray("categoryTerm")[0];
            binding.termTitle.setText(categoryTerm);
        }


        //we will need the selected term and corresponding category in the next fragment
        if( getArguments() != null && getArguments().getStringArray("categoryTerm") != null ) {

            String [] categoryTerm = getArguments().getStringArray("categoryTerm");

            category = categoryTerm[1];
            selectedTerm = categoryTerm[0];

            System.out.println("DigitalWIkiExplantion - CategoryTerm = " + categoryTerm[0]);
            System.out.println("DigitalWIkiExplantion - CategoryName = " + categoryTerm[1]);
        }

        //set the first tab as selected on default
        Fragment fragment = new ExplanationFragment( category, selectedTerm );
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.tab_content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

       //action on tabs
        binding.tablayoutDigitalwikiExplanation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;

                switch (tab.getPosition()) {
                    case 0:
                        fragment = new ExplanationFragment( category, selectedTerm );
                        break;

                    case 1:
                        fragment = new EditFragment( category, selectedTerm );
                        break;

                    case 2:
                        fragment = new EditDevelopmentFragment( category, selectedTerm );
                        break;

                }

                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.tab_content, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return root;

    } //end of onCreateView

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}