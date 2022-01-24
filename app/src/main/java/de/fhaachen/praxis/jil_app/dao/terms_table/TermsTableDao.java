package de.fhaachen.praxis.jil_app.dao.terms_table;

import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import java.util.Map;


public interface TermsTableDao {

    public void allocateTermsToStudent();

    public void fillDigitalwikiWithTerms();
    public void firebaseInitializeTermTable();
    public void firebaseInitializeTerms();

    public void firebaseUpdateTermCell( Cell mCell );
    public void firebaseGetAllTerms();
    public void firebaseUpdateAllTerms();
    public void firebaseSetUserAsActive();
    public void firebaseSetUserAsNotActive();
    public void firebaseUpdateActiveUsers();
    public void firebaseGetAllActiveUsers();
    public void firebaseInitializeActiveUsers();
    public void firebaseGetUserTermsAndShow();


    public TermsTable getTermsTable();
    public Map<String, Object> getAllTerms();
    public String getAllTermsFromTableview();

    public void initializeSetOfUsers();

    public void openTermInputDialog( TextView tableCell, TableLayout mTableview , int rownumber, int colnumber, FragmentManager fragmentManager );

    public void setMethodAsStarted();
    public void setMethodAsNotStarted();

    public void showAllTermsInView( TableLayout mTableview );
    public void showInfoDialogFragment(FragmentManager fragmentManager );
    public void showTermDialog( TextView cellView, FragmentManager fragmentManager  );
    public void showCountTerms( TextView countTermsView );
    public void startCountdown();

    public void updateCountDownView();
    public void updateTermsTableView( TableLayout mTableview );



}
