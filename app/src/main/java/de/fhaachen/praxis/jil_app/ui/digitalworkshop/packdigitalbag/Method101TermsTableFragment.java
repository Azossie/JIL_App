package de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.terms_table.Cell;
import de.fhaachen.praxis.jil_app.dao.terms_table.FirebaseTermsTableInListener;
import de.fhaachen.praxis.jil_app.dao.terms_table.TermsTableDao;
import de.fhaachen.praxis.jil_app.dao.terms_table.TermsTableDaoImpl;
import de.fhaachen.praxis.jil_app.databinding.FragmentMethod101TermsTableBinding;

public class Method101TermsTableFragment extends Fragment implements FirebaseTermsTableInListener {

    private FragmentMethod101TermsTableBinding binding;

    private TermsTableDao mTermsTableDao;

    FirebaseDatabase rootNode;


    public static Method101TermsTableFragment newInstance() {
        return new Method101TermsTableFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_right));
        setExitTransition(inflater.inflateTransition(R.transition.slide_in_left));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMethod101TermsTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Packt euren Digitalisierungskoffer");

        //Enable button "zurück" to go back to previous fragment
        binding.backToMethod101StartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("backToView", "true");

                TransitionInflater inflater = TransitionInflater.from(requireContext());
                setExitTransition(inflater.inflateTransition(R.transition.slide_in_right));
                Navigation.findNavController(v).navigate(R.id.digitalworkshop_digitalbag_method101_start_process, bundle);
            }
        });

        //Enable the button "Alle Begriffe anzeigen" to show all entred terms
        binding.buttonShowAllTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openShowAllTermDialog(binding.methodTable);

            }
        });

        //show the button 'START' only for teachers
        binding.buttonStart.setVisibility(View.GONE);
        rootNode = FirebaseDatabase.getInstance();
        rootNode.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String role = snapshot.getValue(String.class);

                if( role != null && role.equals("teacher") ){

                    binding.buttonStart.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        
        //to interact with firebase
        this.mTermsTableDao = new TermsTableDaoImpl(this, binding);


        //Once clicked on the button 'START', the table should be filled with empty strings
        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set the status of the exercise as 'STARTED' in firebase
                mTermsTableDao.setMethodAsStarted();

                //Disable Button 'START'
                binding.buttonStart.setEnabled(false);

                //start the countdown
                mTermsTableDao.startCountdown();

            }
        });


        //create the table
        Context mainContext = getActivity().getApplicationContext();

        //show terms in tableview with empty strings
        createTable(mainContext);

        //Initialize the terms-table in firebase
        //if the 101-Method-exercise has started
        mTermsTableDao.firebaseInitializeTermTable();

        //pass all terms from firebase to tableview
        mTermsTableDao.showAllTermsInView( binding.methodTable );

        //mTermsTableDao.firebaseGetAllTerms();


        //always update Tableview, if a new term has been sent to firebase
        mTermsTableDao.updateTermsTableView(binding.methodTable);

        //show the number of terms
        mTermsTableDao.showCountTerms(binding.numberTerms);

        //show current countdown
        mTermsTableDao.updateCountDownView();

        return root;
    }


    private void createTable( Context aContext ){

        TableRow newRow;
        TextView cell;

        int count = 1;


        for( int row = 0; row < 10; row++ ){
            // delcare a new row
            newRow = new TableRow( aContext );
            for( int col = 0; col < 10; col++ ){
                // add views to the row
                cell = new TextView( aContext );
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                cell.setBackgroundResource(R.drawable.square);
                cell.setMaxWidth(50);
                //params.weight = 4.0f;
                cell.setSingleLine(true);
                cell.setLayoutParams(params);
                //cell.setWidth(50);
                cell.setHeight(50);
                cell.setText(""); //(count)+
                cell.setGravity(Gravity.CENTER);
                cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                cell.setPadding(10,10,10,10);
                cell.setTextSize(15);

                //open a Dialog to insert a term, once a cell is clicked
                TextView finalCell = cell;
                //String actualTerm = "";

                int finalRow = row;
                int finalCol = col;

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Just show the content of a cell, if it´s already fill
                        if( finalCell != null && finalCell.getText().length() > 0 ){

                            //show the content of the selected cell
                            openShowTermDialog( finalCell );

                        }else{
                            //finalCell.setBackgroundResource(R.drawable.post_it_grey);

                            //update the status of the cell as "inBearbeitung" in firebase
                            String position = "" + finalRow + "-" + finalCol;
                            Cell mCell = new Cell(position, "", "");
                            mCell.setInBearbeitung(true);
                            mTermsTableDao.firebaseUpdateTermCell( mCell );

                            //let the user insert his term
                            openTermInputDialog( finalCell, binding.methodTable, finalRow, finalCol );

                        }

                    }

                });


                newRow.addView(cell); // you would actually want to set properties on this before adding it

                count++;
            }

            // add the row to the table layout
            binding.methodTable.addView(newRow);

            //the last cell in a new row
            if( row == 9){

                row = 10;

                binding.methodTable.setStretchAllColumns(false);

                newRow = new TableRow( aContext );
                // add views to the row
                cell = new TextView( aContext );
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                cell.setBackgroundResource(R.drawable.square);
                params.column = 0;
                cell.setMaxWidth(50);
                //params.weight = 4.0f;
                cell.setSingleLine(true);
                cell.setLayoutParams(params);
                //cell.setWidth(0);
                cell.setGravity(Gravity.CENTER);
                cell.setHeight(50);
                cell.setText("");
                cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                cell.setPadding(10,10,10,10);
                cell.setTextSize(15);

                //open a Dialog to insert a term, once a cell is clicked
                TextView finalCellLast = cell;
                int finalLastRow = row;

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Just show the content of a cell, if it´s already fill
                        if( finalCellLast != null && finalCellLast.getText().length() > 0 ){

                            //show the content of the selected cell
                            openShowTermDialog( finalCellLast );

                        }else{

                            //finalCellLast.setBackgroundResource(R.drawable.post_it_grey);

                            //update the status of the cell as "inBearbeitung" in firebase
                            String position = "" + finalLastRow + "-" + 0;
                            Cell mCell = new Cell(position, "", "");
                            mCell.setInBearbeitung(true);
                            mTermsTableDao.firebaseUpdateTermCell( mCell );

                            //let the user insert his term
                            openTermInputDialog( finalCellLast, binding.methodTable, finalLastRow, 0 );

                        }

                    }
                });

                newRow.addView(cell); // you would actually want to set properties on this before adding it
                binding.methodTable.addView(newRow);
            }

        }
    } //End methode createTable



    private void openTermInputDialog( TextView tableCell, TableLayout mTableview , int rownumber, int colnumber ) {

        TermInputDialogFragment termInputDialog = new TermInputDialogFragment( tableCell,  mTableview, this.mTermsTableDao, rownumber, colnumber );
        termInputDialog.show(this.getParentFragmentManager(), "Begriff eingeben Dialog");

        //get all

    }

    private void openShowTermDialog( TextView tableCell ){

        ShowTermDialogFragment showTermDialog = new ShowTermDialogFragment( tableCell );
        showTermDialog.show(getParentFragmentManager(), "Begriff anzeigen Dialog"); //getSupportFragmentManager()
    }

    private void openShowAllTermDialog( TableLayout table ){

        ShowAllTermsDialogFragment showAllTermsDialog = new ShowAllTermsDialogFragment( table );
        showAllTermsDialog.show(this.getParentFragmentManager(), "Alle Begriffe anzeigen Dialog");
    }

   /* private void openShowAllTermDialog( String allTerms){

        ShowAllTermsDialogFragment showAllTermsDialog = new ShowAllTermsDialogFragment( allTerms );
        showAllTermsDialog.show(this.getParentFragmentManager(), "Alle Begriffe anzeigen Dialog");
    }
*/
    private void openShowInfoDialog(){

        ShowInfoDialogFragment infoDialog = new ShowInfoDialogFragment();
        infoDialog.show(this.getParentFragmentManager(), "Info anzeigen Dialog");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void updateTermsTableView(Context aContext) {

    }

    @Override
    public void updateTermCellView(Context aContext) {

    }
}