package de.fhaachen.praxis.jil_app.ui.gettingstarted;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GettingStartedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GettingStartedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Erste Schritte");
    }

    public LiveData<String> getText() {
        return mText;
    }
}