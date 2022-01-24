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

public class ShowInfoDialogFragment extends AppCompatDialogFragment {

    private TextView showInfoText;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_show_info_dialog, null);

        showInfoText = view.findViewById(R.id.show_info_text);
        String infoText = "Diese Zelle wird gerade von jemand anderem bearbeitet.";
        showInfoText.setText( infoText );

        builder.setView(view)
                .setTitle("Achtung!")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();
    }
}