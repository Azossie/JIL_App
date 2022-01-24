package de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.categories.CategoryDao;


public class CategorieTermInputDialogFragment extends AppCompatDialogFragment {

    private EditText editTextCategoryTerm;
    private TableLayout buttonsTableView;
    private Context mContext;
    private CategoryDao mCategoryDao;
    private String categoryName;

    public CategorieTermInputDialogFragment(TableLayout buttonsTableView, Context mContext, CategoryDao mCategoryDao, String categoryName) {

        this.buttonsTableView = buttonsTableView;
        this.mContext = mContext;
        this.mCategoryDao = mCategoryDao;
        this.categoryName = categoryName;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_categorie_term_input_dialog, null);

        editTextCategoryTerm= view.findViewById(R.id.edit_text_category_term);



        builder.setView(view)
                .setTitle("Einen Begriff eingeben")
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
                        if( editTextCategoryTerm!= null && editTextCategoryTerm.length() > 0 ){

                            String term = editTextCategoryTerm.getText().toString().trim();

                            //create a categoryTerm-object
                            mCategoryDao.setCategoryName(categoryName);

                            mCategoryDao.createPost("", "");
                            mCategoryDao.createCategoryTerm(term, mCategoryDao.getEditedBy());

                            //save the term-object in firebase
                           mCategoryDao.firebaseUpdateCategoryWithTerm( categoryName, mCategoryDao.getmCategoryTerm() );

                        }
                    }
                });


        return builder.create();

        // return super.onCreateDialog(savedInstanceState);
    }

}