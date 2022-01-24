package de.fhaachen.praxis.jil_app.ui.tutorials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.ui.indemessage.IndeMessageFragment;


public class TutorialsFragment extends Fragment {

    public static TutorialsFragment newInstance() {
        return new TutorialsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorials, container, false);
    }
}