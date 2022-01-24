package de.fhaachen.praxis.jil_app.ui.ringlectures;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhaachen.praxis.jil_app.R;

public class RingLecturesFragment extends Fragment {

    public static RingLecturesFragment newInstance() {
        return new RingLecturesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ring_lectures, container, false);
    }
}