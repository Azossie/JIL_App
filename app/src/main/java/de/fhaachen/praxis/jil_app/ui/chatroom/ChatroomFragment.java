package de.fhaachen.praxis.jil_app.ui.chatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.ui.indemessage.IndeMessageFragment;


public class ChatroomFragment extends Fragment {

    public static ChatroomFragment newInstance() {
        return new ChatroomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatroom, container, false);
    }
}