package de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.terms_table.Cell;
import de.fhaachen.praxis.jil_app.dao.terms_table.TermsTableDao;


public class TermInputDialogFragment extends AppCompatDialogFragment implements DialogInterface.OnDismissListener{

    private EditText editTextTerm;
    private TextView tableCell;
    private TableLayout mTableView;

    private InputTermDialogListener contextListener;
    private InputTermDialogListener parentFragmentListener;

    private TermsTableDao mTermsTableDao;

    private int colnumber;
    private int rownumber;


    public TermInputDialogFragment( TextView tableCell,  TableLayout mTableView, TermsTableDao mTermsTableDao, int rownumber, int colnumber){
        this.tableCell = tableCell;
        this.mTableView = mTableView;
        this.mTermsTableDao = mTermsTableDao;
        this.rownumber = rownumber;
        this.colnumber = colnumber;
    }

    public TermInputDialogFragment(TextView tableCell, FragmentManager fragmentManager) {
        
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_term_input, null);

        editTextTerm= view.findViewById(R.id.edit_text_term);


        builder.setView(view)
                .setTitle("Einen Begriff eingeben")
                .setNegativeButton(R.string.abbrechen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editedTerm = null;

                        String position = "" +  rownumber + "-" + colnumber;
                        Cell mCell = new Cell(position, "", "");

                        //Send term to firebase
                        mTermsTableDao.firebaseUpdateTermCell( mCell );
                    }
                })
                .setPositiveButton(R.string.begriff_eingeben, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editedTerm = editTextTerm.getText().toString();

                        //Set Backgroud as post-it only if the user inserted a term
                        if( editedTerm != null && editTextTerm.length() > 0 ){

                            String position = "" +  rownumber + "-" + colnumber;
                            Cell mCell = new Cell(position, "", editedTerm);
                            mCell.setBearbeitet(true);

                            //Send term to firebase
                            mTermsTableDao.firebaseUpdateTermCell( mCell );

                        }else{

                            String position = "" +  rownumber + "-" + colnumber;
                            Cell mCell = new Cell(position, "", "");

                            //Send term to firebase
                            mTermsTableDao.firebaseUpdateTermCell( mCell );
                        }

                    }
                });



        return builder.create();

       // return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if( this.tableCell.getText().length() == 0 ){
            this.tableCell.setBackgroundResource(R.drawable.square);
        }

    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            contextListener = (InputTermDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Muss InputTermDialogListener Implementieren");
        }
    }*/

    public interface InputTermDialogListener {
        void applyTexts(String editedTerm, TextView cell);
    }

}