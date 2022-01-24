package de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.R;


public class ShowTermDialogFragment extends AppCompatDialogFragment  {

    private TextView tableCell;
    private TextView showTextTerm;

    public ShowTermDialogFragment( TextView tableCell ){
        this.tableCell = tableCell;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_show_term_dialog, null);

        showTextTerm = view.findViewById(R.id.show_text_term);
        showTextTerm.setText( "Eingegebener Begriff: " + tableCell.getText() );

        builder.setView(view)
                .setTitle("Zelle wurde schon bearbeitet!")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();

        // return super.onCreateDialog(savedInstanceState);
    }



}