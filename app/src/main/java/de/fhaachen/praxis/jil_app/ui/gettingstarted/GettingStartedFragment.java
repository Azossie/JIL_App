package de.fhaachen.praxis.jil_app.ui.gettingstarted;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.fhaachen.praxis.jil_app.databinding.FragmentGettingStartedBinding;

public class GettingStartedFragment extends Fragment {

    private GettingStartedViewModel GettingStartedViewModel;
    private FragmentGettingStartedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GettingStartedViewModel =
                new ViewModelProvider(this).get(GettingStartedViewModel.class);

        binding = FragmentGettingStartedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGettingStarted;
        GettingStartedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}