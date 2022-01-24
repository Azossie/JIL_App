package de.fhaachen.praxis.jil_app.ui.jil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.databinding.FragmentGettingStartedBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentJilBinding;
import de.fhaachen.praxis.jil_app.ui.gettingstarted.GettingStartedViewModel;
import de.fhaachen.praxis.jil_app.ui.innovationworkshop.InnovationworkshopFragment;


public class JilFragment extends Fragment {

    private FragmentJilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentJilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}