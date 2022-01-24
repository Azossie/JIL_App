package de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.databinding.FragmentShowAllTermsDialogBinding;

public class ShowAllTermsDialogFragment extends AppCompatDialogFragment {

    private TableLayout table;
    private TextView showTextAllTerms;
    private ImageView mascot_great_done;
    private ImageView bubble_great_done;
    //private FragmentShowAllTermsDialogBinding binding;

    //private String allTerms;

    public ShowAllTermsDialogFragment( TableLayout table ){
        this.table = table;
    }

   /* public ShowAllTermsDialogFragment( String allTerms ){
        this.allTerms = allTerms;
    }*/


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_show_all_terms_dialog, null);

        showTextAllTerms = view.findViewById(R.id.show_text_all_terms);
        mascot_great_done = view.findViewById(R.id.mascot_great_done);
        bubble_great_done = view.findViewById(R.id.bubble_great_done);

        String text = "";

        for( int row = 0; row < table.getChildCount(); row++ ){

            TableRow rowView = (TableRow) table.getChildAt(row);

            for( int col = 0; col < rowView.getChildCount(); col++ ){
                TextView colView =  (TextView)rowView.getChildAt(col);

                if(colView != null && colView.getText().length() > 0 ){
                    text = text + colView.getText().toString() + ", ";
                }
            }

        }


        //show all terms in the dialog
        if( text != "" ){
            //remove the last ", "
            text = text.substring(0,text.length()-2);
            showTextAllTerms.setText( text );

        }else{
            mascot_great_done.setVisibility(View.GONE);
            bubble_great_done.setVisibility(View.GONE);
            showTextAllTerms.setText( "Es gibt noch keinen Begriff!" );
        }

       /* if( allTerms != null && allTerms.length() > 0 ){
            //remove the last ", "
            text = text.substring(0,text.length()-2);

            //remove the first ", "
//            allTerms = allTerms.substring(1);
//            showTextAllTerms.setText( allTerms );

        }else{
            mascot_great_done.setVisibility(View.GONE);
            bubble_great_done.setVisibility(View.GONE);
            showTextAllTerms.setText( "Es gibt noch keinen Begriff!" );
        }*/


        builder.setView(view)
                .setTitle("Alle eingegebenen Begriffe")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();

        // return super.onCreateDialog(savedInstanceState);
    }

}