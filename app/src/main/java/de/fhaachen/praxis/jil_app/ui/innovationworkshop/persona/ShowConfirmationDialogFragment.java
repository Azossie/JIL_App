package de.fhaachen.praxis.jil_app.ui.innovationworkshop.persona;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.databinding.FragmentCreatePersonaBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentShowConfirmationDialogBinding;

public class ShowConfirmationDialogFragment extends AppCompatDialogFragment {

    private String textToshow;
    private String dialogTitle;
    private TextView showInfoText;

    public ShowConfirmationDialogFragment(){

    }

    public ShowConfirmationDialogFragment( String textToshow, String dialogTitle){

        this.textToshow = textToshow;
        this.dialogTitle = dialogTitle;
    }


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_show_confirmation_dialog, null);

        showInfoText = view.findViewById(R.id.confirmation_text);
        String infoText = textToshow + "\n" + showInfoText.getText().toString();
        showInfoText.setText( infoText );

        builder.setView(view)
                .setTitle(dialogTitle)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();

    }
}