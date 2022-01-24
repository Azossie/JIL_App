package de.fhaachen.praxis.jil_app.dao.terms_table;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.databinding.FragmentAutomatisationTermsEntryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalClientAccessTermsEntryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalDataTermsEntryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiAllTermsBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentFillDigitalwikiCategoriesBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentFillDigitalwikiIntroBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentMethod101TermsTableBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentNetworkingTermsEntryBinding;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.AutomatisationTermsEntryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalClientAccessTermsEntryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalDataTermsEntryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalwikiAllTermsFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.FillDigitalwikiCategoriesFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.FillDigitalwikiIntroFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.NetworkingTermsEntryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag.Method101TermsTableFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag.ShowInfoDialogFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag.ShowTermDialogFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.packdigitalbag.TermInputDialogFragment;

public class TermsTableDaoImpl implements  TermsTableDao{

    private static final long START_TIME_IN_MILLIS = 30000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis; //= START_TIME_IN_MILLIS;
    private boolean endOfExercise = false;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase rootNode;

    private Map<String, Object> allTerms;
    private Map<String, Object> digitalwikiTerms;
    private Method101TermsTableFragment method101TermsTableFragment;
    private FragmentMethod101TermsTableBinding binding;

    private FillDigitalwikiCategoriesFragment fillDigitalwikiCategoriesFragment;
    private FragmentFillDigitalwikiCategoriesBinding fillDigitalwikiCategoriesBinding;

    private DigitalDataTermsEntryFragment digitalDataTermsEntryFragment;
    private FragmentDigitalDataTermsEntryBinding digitalDataTermsEntryBinding;

    private FillDigitalwikiIntroFragment fillDigitalwikiIntroFragment;
    private FragmentFillDigitalwikiIntroBinding fillDigitalwikiIntroBinding;

    private FragmentNetworkingTermsEntryBinding networkingTermsEntryBinding;
    private NetworkingTermsEntryFragment networkingTermsEntryFragment;

    private FragmentAutomatisationTermsEntryBinding automatisationTermsEntryBinding;
    private AutomatisationTermsEntryFragment automatisationTermsEntryFragment;

    private DigitalClientAccessTermsEntryFragment digitalClientAccessTermsEntryFragment;
    private FragmentDigitalClientAccessTermsEntryBinding digitalClientAccessTermsEntryBinding;

    private DigitalwikiAllTermsFragment digitalwikiAllTermsFragment;
    private FragmentDigitalwikiAllTermsBinding digitalwikiAllTermsBinding;

    private TermsTable mTermsTable;

    private HashSet<String> allUsersIDs;


    public TermsTableDaoImpl() {

    }

    public TermsTableDaoImpl( Method101TermsTableFragment method101TermsTableFragment, FragmentMethod101TermsTableBinding binding ){

        this.method101TermsTableFragment = method101TermsTableFragment;
        this.binding = binding;
        this.allTerms = new HashMap<>();
        this.digitalwikiTerms = new HashMap<>();

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();

        allUsersIDs = new HashSet<>();
    }

    public TermsTableDaoImpl( FillDigitalwikiCategoriesFragment fillDigitalwikiCategoriesFragment, FragmentFillDigitalwikiCategoriesBinding fillDigitalwikiCategoriesBinding ) {

        this.fillDigitalwikiCategoriesBinding = fillDigitalwikiCategoriesBinding;
        this.fillDigitalwikiCategoriesFragment = fillDigitalwikiCategoriesFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();

    }

    public TermsTableDaoImpl( DigitalDataTermsEntryFragment digitalDataTermsEntryFragment, FragmentDigitalDataTermsEntryBinding digitalDataTermsEntryBinding ) {

        this.digitalDataTermsEntryFragment = digitalDataTermsEntryFragment;
        this.digitalDataTermsEntryBinding = digitalDataTermsEntryBinding;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();
    }

    public TermsTableDaoImpl( FillDigitalwikiIntroFragment fillDigitalwikiIntroFragment, FragmentFillDigitalwikiIntroBinding fillDigitalwikiIntroBinding ) {

        this.fillDigitalwikiIntroFragment = fillDigitalwikiIntroFragment;
        this.fillDigitalwikiIntroBinding = fillDigitalwikiIntroBinding;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();
    }

    public TermsTableDaoImpl(NetworkingTermsEntryFragment networkingTermsEntryFragment, FragmentNetworkingTermsEntryBinding networkingTermsEntryBinding) {

        this.networkingTermsEntryBinding = networkingTermsEntryBinding;
        this.networkingTermsEntryFragment = networkingTermsEntryFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();

    }

    public TermsTableDaoImpl( AutomatisationTermsEntryFragment automatisationTermsEntryFragment, FragmentAutomatisationTermsEntryBinding automatisationTermsEntryBinding ) {

        this.automatisationTermsEntryBinding = automatisationTermsEntryBinding;
        this.automatisationTermsEntryFragment = automatisationTermsEntryFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();
    }

    public TermsTableDaoImpl( DigitalClientAccessTermsEntryFragment digitalClientAccessTermsEntryFragment, FragmentDigitalClientAccessTermsEntryBinding digitalClientAccessTermsEntryBinding ) {

        this.digitalClientAccessTermsEntryBinding = digitalClientAccessTermsEntryBinding;
        this.digitalClientAccessTermsEntryFragment = digitalClientAccessTermsEntryFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();
    }

    public TermsTableDaoImpl( DigitalwikiAllTermsFragment digitalwikiAllTermsFragment, FragmentDigitalwikiAllTermsBinding digitalwikiAllTermsBinding ) {

        this.digitalwikiAllTermsBinding = digitalwikiAllTermsBinding;
        this.digitalwikiAllTermsFragment = digitalwikiAllTermsFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mTermsTable = new TermsTable();
    }

    @Override
    public void allocateTermsToStudent() {

        if( firebaseUser  != null ){

            String userId = firebaseUser.getUid();

            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //Get the user´s schoolname
                    String schoolName = snapshot.getValue(String.class);

                    //get all actives Users´s ids from firebase
                    rootNode.getReference().child("schools").child(schoolName).child("active-users")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    String usersIds = snapshot.getValue(String.class);

                                    if (usersIds != null && usersIds.length() > 0) {

                                        String [] uIds = usersIds.split(", ");

                                        //System.out.println("Users to be allocated : " + uIds.toString() );

                                        //find out how many users
                                        int countUsersIds = uIds.length;

                                        if( countUsersIds > 0 ){

                                            //get all terms from firebase
                                            //firebaseGetAllTerms();

                                            //find out how many terms
                                            mTermsTable.setAllTerms(getAllTermsFromTableview());
                                            String allTerms = getAllTermsFromTableview(); //mTermsTable.getAllTerms();
                                            int countTerms = mTermsTable.getCountAllTerms();

                                            System.out.println( "All Terms = " + allTerms );
                                            System.out.println( "Count Terms = " + countTerms );

                                            //and divide the number of terms into the number of users
                                            //save the number of terms for each user
                                            int [] usersCountAllocatedTerms  = new int[countUsersIds];

                                            //how many terms each user after dividing
                                            int countTermsEachUser = countTerms/countUsersIds;
                                            //rest of division
                                            int restCountTerms = countTerms%countUsersIds;

                                            //save the number of terms for each user deriving
                                            //as well from the result of the division
                                            // and from the rest of the division
                                            for( int i = 0; i < countUsersIds; i++ ){

                                                usersCountAllocatedTerms[i] = countTermsEachUser;
                                            }

                                            for( int j = 0; j < restCountTerms; j++ ){

                                                usersCountAllocatedTerms[j]++;
                                            }

                                            // and allocate terms to each user
                                            //for mapping userId to his term
                                            HashMap<String, String> userTermsMap = new HashMap<>();

                                            //save the all terms from firebase in an array
                                            String [] uTerms = allTerms.split(", ");

                                            //prepare an array to get as much of terms from all terms
                                            //as the number of terms got from the division and modulo
                                            String userTerms [] = new String[countUsersIds];
                                            int userTermsCount = 1;
                                            int userTermsIndex = 0;

                                            for( int count = 0; count < uTerms.length; count++ ){

                                                if( userTermsIndex < usersCountAllocatedTerms.length ){

                                                    if( userTerms[userTermsIndex] == null){

                                                        userTerms[userTermsIndex] = uTerms[count];

                                                    }else{

                                                        userTerms[userTermsIndex] += ", " + uTerms[count];

                                                    }

                                                    if( userTermsCount == usersCountAllocatedTerms[userTermsIndex] ){
                                                        userTermsCount = 1;
                                                        userTermsIndex++;

                                                    }else{

                                                        userTermsCount++;
                                                    }

                                                }

                                            } // end of for-loop

                                            System.out.println("Terms to be allocated : " + userTerms.toString() );

                                            //map userId to his term
                                            for( int countId = 0; countId < uIds.length; countId++ ){

                                               // userTermsMap.put( uIds[countId], userTerms[countId] );

                                                //update users with their term in firebase
                                                rootNode.getReference().child("schools").child(schoolName).child("users")
                                                        .child(uIds[countId]).child("terms").setValue(userTerms[countId]);

                                            }




                                        }



                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }


    }

    @Override
    public void fillDigitalwikiWithTerms() {

        String allTerms = getAllTermsFromTableview();
        String wikiTerms [] = allTerms.split(", ");

        for( String term : wikiTerms ){

            digitalwikiTerms.put( term, new Term(term) );
        }


        if( firebaseUser != null && wikiTerms.length > 0 ) { //mindestens ein Begriff aus der Tabelle

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //Get the user´s schoolname
                    String schoolName = snapshot.getValue(String.class);

                    rootNode.getReference().child("schools").child(schoolName).child("digitalwiki").child("terms")
                            .setValue(digitalwikiTerms);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void firebaseInitializeTermTable() {

        //put the terms from the table in the HashMap
        mTermsTable = new TermsTable(); //creat an empty table
        Cell[][] termsCells = mTermsTable.getAllCells();
        for( int row = 0; row < 10; row++ ){

            for( int col = 0; col < 10; col++ ){

                String mKey = termsCells[row][col].getPosition();
                Cell mCell = termsCells[row][col];
                this.allTerms.put(mKey, mCell);
            }

        }

        //for the last cell(101.st)
        this.allTerms.put(termsCells[10][0].getPosition(), termsCells[10][0]);

        //update the database with the new terms
        if( firebaseUser != null ){

            String userId = firebaseUser.getUid();
            System.out.println("User-Id: " +  userId);

            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //Get the user´s schoolname
                    String schoolName = snapshot.getValue(String.class);

                    //System.out.println("Schoolname: " + schoolName);

                    //save all terms in the firebase
                    //go to the node /shools/<schoolname>/terms-table/ and initialize table with empty strings
                    //only if the exercise is set as 'STARTED'
                    if( schoolName != null ){

                        rootNode.getReference().child("schools").child(schoolName)
                                .addChildEventListener( new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        System.out.println("PreviousChildName: " + previousChildName);
                                        System.out.println("Key: " + snapshot.getKey());

                                        if( snapshot.getKey().equals("is-started")){

                                            //put the terms from the table in the HashMap
                                            mTermsTable = new TermsTable(); //creat an empty table
                                            Cell[][] termsCells = mTermsTable.getAllCells();
                                            for( int row = 0; row < 10; row++ ){

                                                for( int col = 0; col < 10; col++ ){

                                                    String mKey = termsCells[row][col].getPosition();
                                                    Cell mCell = termsCells[row][col];
                                                    allTerms.put(mKey, mCell);
                                                }

                                            }

                                            //for the last cell(101.st)
                                            allTerms.put(termsCells[10][0].getPosition(), termsCells[10][0]);

                                            String isStarted = snapshot.getValue(String.class);

                                            if( isStarted.equals("true") ){


                                                //Initialize the set of users´s ids
                                                allUsersIDs = new HashSet<>();

                                                rootNode.getReference().child("schools").child(schoolName).child("terms-table")
                                                        .setValue(allTerms).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                        //initialize the number of terms with 0
                                                        if( task.isSuccessful() ){

                                                            //set number of terms to 0
                                                            rootNode.getReference().child("schools").child(schoolName)
                                                                    .child("count-terms").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                    if( task.isSuccessful() ){

                                                                        //set the countdown in milliseconds
                                                                        rootNode.getReference().child("schools").child(schoolName)
                                                                                .child("countdown").setValue("30000").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                                if( task.isSuccessful() ){

                                                                                    //Initialize the string of all terms with an empty string
                                                                                    firebaseInitializeTerms();

                                                                                    //INitialize the string of all users´s IDs
                                                                                    firebaseInitializeActiveUsers();
                                                                                }
                                                                            }
                                                                        });

                                                                    }

                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }

                                        }


                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void firebaseInitializeTerms() {

        //Initialize the string of all terms with an empty string
        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            System.out.println("User-Id: " + userId);

            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //Get the user´s schoolname
                    String schoolName = snapshot.getValue(String.class);

                    rootNode.getReference().child("schools").child(schoolName).child("terms").setValue("");

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void firebaseUpdateTermCell( Cell mCell ) {

        String posNode = mCell.getPosition();

        //update the database with the new terms
        if( firebaseUser != null ){

            String userId = firebaseUser.getUid();

            //set User as 'active'
            firebaseSetUserAsActive();
            //update all users´s IDs
            firebaseUpdateActiveUsers();

            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //Get the user´s schoolname
                    String schoolName = snapshot.getValue(String.class);

                    //get user´s name
                    rootNode.getReference().child("users").child(userId).child("username").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String username = snapshot.getValue(String.class);
                            mCell.setPostedBy(username); //save the name of who posted the term

                            //update the cell
                            rootNode.getReference().child("schools").child(schoolName).child("terms-table")
                                    .child(mCell.getPosition()).setValue(mCell)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {


                                             if(task.isSuccessful()){

                                                 //increment the number of terms
                                                 //and save it in the firebase
                                                 //if a new term is sent to database
                                                 if( mCell.getTerm().length() > 0 ){

                                                     //update the number of terms in firebase
                                                     rootNode.getReference().child("schools").child(schoolName)
                                                             .child("count-terms").addValueEventListener( new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange( @NonNull @NotNull DataSnapshot snapshot ) {

                                                            //update the number of terms in firebase
                                                             rootNode.getReference().child("schools").child(schoolName)
                                                                     .child("count-terms").setValue("" + mTermsTable.getCountAllTerms());

                                                         }

                                                         @Override
                                                         public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                         }
                                                     });

                                                 }

                                             }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }


    }

    @Override
    public String getAllTermsFromTableview(){

        String allTerms = "";

        for( int i = 0; i < binding.methodTable.getChildCount(); i++ ){

            View view = binding.methodTable.getChildAt(i);

            if( view instanceof  TableRow ){

                TableRow tr = (TableRow)view;

                for( int j = 0; j < tr.getChildCount(); j++ ){

                    TextView tv = (TextView)tr.getChildAt(j);
                    String tvText = tv.getText().toString();

                    if( tvText.length() > 0 ){

                        if( allTerms.length() == 0 ){

                            allTerms = tv.getText().toString();

                        }else if( allTerms.length() > 0 ){

                            allTerms = allTerms + ", " + tv.getText().toString();
                        }

                    }

                }
            }
        }

        //save them in the object 'mTermsTable'
        mTermsTable.setAllTerms(allTerms);

        return allTerms;
    }

    @Override
    public void firebaseGetAllTerms() {

        //get Terms from Firebase and pass them to the object 'mTermsTable'
        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    String schoolName = snapshot.getValue(String.class);

                    rootNode.getReference().child("schools").child(schoolName).child("terms")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    String allTerms = snapshot.getValue(String.class);

                                    mTermsTable.setAllTerms(allTerms);

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void firebaseUpdateAllTerms() {

        mTermsTable.setAllTerms( getAllTermsFromTableview() );

        //update all terms in firebase
        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //Get the user´s schoolname
                    String schoolName = snapshot.getValue(String.class);

                    //get user´s name
                    rootNode.getReference().child("schools").child(schoolName).child("terms").setValue( mTermsTable.getAllTerms() );

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void firebaseSetUserAsActive() {

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("is-active").setValue("true");

        }
    }

    @Override
    public void firebaseSetUserAsNotActive() {

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("is-active").removeValue();
        }

    }

    @Override
    public void firebaseUpdateActiveUsers() {

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();

            //get active users from firebase
            firebaseGetAllActiveUsers();

            rootNode.getReference().child("users").child(userId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    if( snapshot.getKey().equals("is-active") ){

                        //save this current user as active
                        allUsersIDs.add(userId);

                        rootNode.getReference().child("users").child(userId).child("role")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                         String role = snapshot.getValue(String.class);

                                         //save only student´s IDs
                                         if( role.equals("student") ){ //if this user is a student

                                             rootNode.getReference().child("users").child(userId).child("schoolname")
                                                     .addValueEventListener(new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                             String schoolName = snapshot.getValue(String.class);

                                                             String allUsers = "";
                                                             Iterator<String> it = allUsersIDs.iterator();

                                                             while( it.hasNext() ){

                                                                 if( allUsers.length() == 0 ){
                                                                     allUsers += (String)it.next();
                                                                 }else if( allUsers.length() > 0 ){

                                                                     allUsers += ", " + (String)it.next();
                                                                 }

                                                             }

                                                            // System.out.println("ALl Users1 = " + allUsers);

                                                             //save all users´s IDs in firebase
                                                             rootNode.getReference().child("schools").child(schoolName)
                                                                     .child("active-users").setValue(allUsers);


                                                         }

                                                         @Override
                                                         public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                         }
                                                     });

                                         }else{ // if user is not a student

                                             //remove his id from the set
                                             allUsersIDs.remove(userId);

                                         }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                    }
                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void firebaseGetAllActiveUsers() {

        if (firebaseUser != null) {

            String userId = firebaseUser.getUid();

            rootNode.getReference().child("schools").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    String key = snapshot.getKey(); //in this case, it´s the 'schoolname'

                    System.out.println("Get ALl Users - key: " +  key);

                   // if( snapshot.hasChild("active-users") ){

                        rootNode.getReference().child("schools").child(key).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                if( snapshot.getKey().equals("active-users") ){

                                    //get all users from firebase
                                    String allUsers = snapshot.getValue(String.class);

                                    if( allUsers != null && allUsers.length() > 0 ){

                                        String [] userIds = allUsers.split(", ");

                                        for( String s: userIds ){

                                            allUsersIDs.add(s);
                                        }
                                    }

                                    System.out.println("ALl Users2 = " + allUsers);


                                }
                            }

                            @Override
                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });


                    //}

                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void firebaseInitializeActiveUsers() {

        if( firebaseUser != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("active-users").setValue("");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void firebaseGetUserTermsAndShow() {

        //get the user´s terms from firebase and show it the view with categories
        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    String schoolName = snapshot.getValue(String.class);

                    if( schoolName != null ){

                        rootNode.getReference().child("schools").child(schoolName).child("users").child(userId).child("terms")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        String userTerms = snapshot.getValue(String.class);

                                        if( fillDigitalwikiIntroBinding != null ) {
                                            fillDigitalwikiIntroBinding.textWikiIntroYourTerms.setText(userTerms);
                                        }

                                        if( fillDigitalwikiCategoriesBinding != null ){
                                            fillDigitalwikiCategoriesBinding.textWikiCategoriesYourTerms.setText(userTerms);
                                        }

                                        if( digitalDataTermsEntryBinding != null ){
                                            digitalDataTermsEntryBinding.textDigitalDataYourTerms.setText(userTerms);
                                        }

                                        if( networkingTermsEntryBinding != null ){
                                            networkingTermsEntryBinding.textNetworkingYourTerms.setText(userTerms);
                                        }

                                        if( automatisationTermsEntryBinding != null ){
                                            automatisationTermsEntryBinding.textAutomatisationYourTerms.setText(userTerms);
                                        }

                                        if( digitalClientAccessTermsEntryBinding != null ){
                                            digitalClientAccessTermsEntryBinding.textDigitalClientAccessYourTerms.setText(userTerms);
                                        }

                                        if( digitalwikiAllTermsBinding != null ){
                                            digitalwikiAllTermsBinding.textYourTerms.setText(userTerms);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                    }


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

    }


    @Override
    public void showInfoDialogFragment(FragmentManager fragmentManager) {
        ShowInfoDialogFragment infoDialog = new ShowInfoDialogFragment();
        infoDialog.show(fragmentManager, "Info anzeigen Dialog");
    }

    @Override
    public void showAllTermsInView(TableLayout mTableview) {

        //get Terms from Firebase and pass them to the tableview
        if( firebaseUser.getUid() != null ){

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    String schoolName = snapshot.getValue(String.class);

                    if( schoolName != null ){

                        rootNode.getReference().child("schools").child(schoolName).child("terms-table")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        String allTerms = "";
                                        for( DataSnapshot childNode : snapshot.getChildren() ){

                                            String keyNode = childNode.getKey();
                                            String pos[] = keyNode.split("-");
                                            int row = Integer.parseInt(pos[0]);
                                            int col = Integer.parseInt(pos[1]);

                                            Cell aCell = childNode.getValue(Cell.class);

                                            ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setText( aCell.getTerm() );

                                            if( aCell.getTerm().length() > 0 ){
                                                ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setBackgroundResource(R.drawable.post_it);
                                                ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setEnabled(true);

                                                //update the string of all Terms in Firebase
                                                firebaseUpdateAllTerms();
                                            }

                                            if( aCell.isInBearbeitung() && aCell.getTerm().length() == 0 ){
                                                ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setBackgroundResource(R.drawable.post_it_grey);

                                                //Let all users know that the selected cell is been edited
                                                //by setting the cell´s background as a grey post-it
                                                ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        showInfoDialogFragment( method101TermsTableFragment.getParentFragmentManager() );
                                                    }
                                                });

                                            }

                                            if( !aCell.isInBearbeitung() && !aCell.isBearbeitet() && aCell.getTerm().length() == 0 ){
                                                ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setBackgroundResource(R.drawable.square);

                                            }

                                        }// end of for-loop

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }


    }

    @Override
    public void showTermDialog(TextView cellView, FragmentManager fragmentManager ) {

        ShowTermDialogFragment showTermDialog = new ShowTermDialogFragment( cellView );
        showTermDialog.show(fragmentManager, "Begriff anzeigen Dialog");
    }

    @Override
    public void openTermInputDialog(TextView tableCell, TableLayout mTableview, int rownumber, int colnumber, FragmentManager fragmentManager) {

        TermInputDialogFragment termInputDialog = new TermInputDialogFragment( tableCell,  mTableview, this, rownumber, colnumber );
        termInputDialog.show(fragmentManager, "Begriff eingeben Dialog");
    }


    @Override
    public void updateTermsTableView( TableLayout mTableview ) {

        //get Terms from Firebase and pass them to the tableview
        if( firebaseUser.getUid() != null ){

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    String schoolName = snapshot.getValue(String.class);

                    if( schoolName != null ){

                        rootNode.getReference().child("schools").child(schoolName).child("terms-table")
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        Cell aCell = snapshot.getValue(Cell.class);
                                        String pos[] = aCell.getPosition().split("-");
                                        int row = Integer.parseInt(pos[0]);
                                        int col = Integer.parseInt(pos[1]);

                                        //update the terms table array and -view
                                        mTermsTable.getAllCells()[row][col] = aCell;


                                        ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setText( aCell.getTerm() );

                                        if( aCell.getTerm().length() > 0 ){

                                            //update number of terms
                                            mTermsTable.setCountAllTerms( mTermsTable.getCountAllTerms() + 1 );

                                            //update the number of terms shown
                                            showCountTerms( binding.numberTerms );

                                            ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setBackgroundResource(R.drawable.post_it);

                                            ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setOnClickListener(new View.OnClickListener() {

                                                TextView cellView = ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col));
                                                @Override
                                                public void onClick(View v) {

                                                    //show the content of the selected cell
                                                    showTermDialog( cellView, method101TermsTableFragment.getParentFragmentManager() );

                                                }
                                            });


                                        }

                                        if( aCell.isInBearbeitung() && aCell.getTerm().length() == 0 ){
                                            ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setBackgroundResource(R.drawable.post_it_grey);

                                            //Let all users know that the selected cell is been edited
                                            //by setting the cell´s background as a grey post-it
                                            ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    showInfoDialogFragment( method101TermsTableFragment.getParentFragmentManager() );
                                                }
                                            });
                                        }

                                        if( !aCell.isInBearbeitung() && !aCell.isBearbeitet() && aCell.getTerm().length() == 0  ){
                                            ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setBackgroundResource(R.drawable.square);

                                            //enable each blank cell to be edited
                                            ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    TextView cellView = ((TextView)((TableRow) mTableview.getChildAt(row)).getChildAt(col));

                                                    openTermInputDialog( cellView, mTableview , row, col, method101TermsTableFragment.getParentFragmentManager());

                                                }
                                            });
                                        }



                                    } //End onChildChanged

                                    @Override
                                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                    }


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }

    }

    @Override
    public void initializeSetOfUsers() {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("schools").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    String key = snapshot.getKey();

                    rootNode.getReference().child("schools").child(key).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            if( snapshot.getKey().equals("is-started") ){

                                String status = snapshot.getValue(String.class);

                                if( status.equals("false") ){

                                    //set the set of users´s ids as empty
                                    //for the next exercise
                                    allUsersIDs = new HashSet<>();
                                }
                            }
                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void startCountdown() {

        mTimeLeftInMillis = START_TIME_IN_MILLIS;
       // System.out.println("mTimeLeftInMillis = " + mTimeLeftInMillis);
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

                //TODO update milliseconds in firebase
                if( firebaseUser.getUid() != null ) {

                    String userId = firebaseUser.getUid();
                    rootNode.getReference().child("users").child(userId).child("schoolname")
                            .addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    String schoolName = snapshot.getValue(String.class);
                                    String leftTimeInMillis = String.valueOf(mTimeLeftInMillis);

                                    rootNode.getReference().child("schools").child(schoolName).child("countdown")
                                            .setValue(leftTimeInMillis).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            //update the view for all users with the current left minutes or seconds
                                            if( task.isSuccessful() ){

                                                updateCountDownView();
                                            }

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                }


            }

            @Override
            public void onFinish() {

                binding.buttonStart.setEnabled(true);
                setMethodAsNotStarted();
                fillDigitalwikiWithTerms();

            }

        }.start();

    }

    private void disableTableviewCells(){

        for( int i = 0; i < binding.methodTable.getChildCount(); i++ ){

            View view = binding.methodTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow row = (TableRow) view;

                for( int j = 0; j < row.getChildCount(); j++ ){

                    //disable only empty cells
                    if( ((TextView)row.getChildAt(j)).getText().length() == 0 ){
                        row.getChildAt(j).setEnabled(false);
                    }else{
                        row.getChildAt(j).setEnabled(true);
                    }


                }
            }
        }
    }

    private void enableTableviewCells(){

        for( int i = 0; i < binding.methodTable.getChildCount(); i++ ){

            View view = binding.methodTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow row = (TableRow) view;

                for( int j = 0; j < row.getChildCount(); j++ ){

                    //disable only empty cells
                    row.getChildAt(j).setEnabled(true);


                }
            }
        }
    }

    @Override
    public void updateCountDownView() {

        //get milliseconds from firebase and show it to users
        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            if( schoolName != null ){

                                rootNode.getReference().child("schools").child(schoolName).child("countdown")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                String currentLeftTime = snapshot.getValue(String.class);
                                                long currentLeftTimeInMillis = Long.parseLong(currentLeftTime);

                                                int minutes = (int) (currentLeftTimeInMillis / 1000) / 60;
                                                int seconds = (int) (currentLeftTimeInMillis / 1000) % 60;

                                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                                                binding.countdown.setText(timeLeftFormatted);

                                                //if the countdowm stop
                                                if( currentLeftTimeInMillis < 1000 ){ //|| currentLeftTimeInMillis == START_TIME_IN_MILLIS

                                                    //disable all tablecells
                                                    disableTableviewCells();
                                                    firebaseSetUserAsNotActive();
                                                    allocateTermsToStudent();
                                                    //fill the sector 'digitalwiki' with all inserted terms
                                                    //fillDigitalwikiWithTerms();

                                                }else{

                                                    enableTableviewCells();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }


    }

    @Override
    public void setMethodAsStarted() {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("is-started")
                                    .setValue("true");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void setMethodAsNotStarted() {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("is-started")
                                    .setValue("false");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void showCountTerms( TextView countTermsView ) {

        //get the number of terms from the firebase and show it
        if( firebaseUser.getUid() != null ){

            String userId = firebaseUser.getUid();
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            if( schoolName != null ){

                                rootNode.getReference().child("schools").child(schoolName).child("count-terms")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                final String countTerms = snapshot.getValue(String.class);
                                                System.out.println("Number of terms: " + countTerms );
                                                countTermsView.setText( countTerms );
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public TermsTable getTermsTable() {
        return this.mTermsTable;
    }

    @Override
    public Map<String, Object> getAllTerms() {
        return this.allTerms;
    }



}
