package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.categories.Category;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;

public class CategorieInputDialogFragment extends AppCompatDialogFragment {

    private EditText editTextCategory;
    private TableLayout buttonsTableView;
    private Context mContext;
    private CategoryDao mCategoryDao;
    private String termName;
    private String explanation;

    public CategorieInputDialogFragment(TableLayout buttonsTableView, Context mContext, CategoryDao mCategoryDao, String termName, String explanation ) {

        this.buttonsTableView = buttonsTableView;
        this.mContext = mContext;
        this.mCategoryDao = mCategoryDao;
        this.termName = termName;
        this.explanation = explanation;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_categorie_term_input_dialog, null);

        editTextCategory= view.findViewById(R.id.edit_text_category_term);



        builder.setView(view)
                .setTitle("Eine Kategorie eingeben")
                .setNegativeButton(R.string.abbrechen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.begriff_eingeben, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //make button out of the inserted term a add it to list of terms for correponding category
                        if( editTextCategory!= null && editTextCategory.length() > 0 ){

                            String category = editTextCategory.getText().toString().trim();

                            //create a category-object
                            Category mCategory = new Category( category, termName);

                            mCategoryDao.createCategory( category, termName, explanation, mCategory);

                            //save the term-object in firebase
                            mCategoryDao.firebaseUpdateTermWithCategory( category, termName );

                            //add the new category as a button
                            mCategoryDao.createCategoryButton( category, termName, mContext);

                        }
                    }
                });


        return builder.create();

        // return super.onCreateDialog(savedInstanceState);
    }
}
