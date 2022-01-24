package de.fhaachen.praxis.jil_app.dao.categories;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Iterator;

import de.fhaachen.praxis.jil_app.R;
import de.fhaachen.praxis.jil_app.dao.terms_table.Term;
import de.fhaachen.praxis.jil_app.databinding.FragmentAutomatisationTermsEntryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentCategoryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalClientAccessTermsEntryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalDataTermsEntryBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiAllTermsBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiExplanationInputBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentDigitalwikiTermExplanationBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentEditBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentEditDevelopmentBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentExplanationBinding;
import de.fhaachen.praxis.jil_app.databinding.FragmentNetworkingTermsEntryBinding;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.AutomatisationTermsEntryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.CategoryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalClientAccessTermsEntryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalDataTermsEntryFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalwikiAllTermsFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalwikiExplanationInputFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.DigitalwikiTermExplanationFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.EditDevelopmentFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.EditFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.ExplanationFragment;
import de.fhaachen.praxis.jil_app.ui.digitalworkshop.filldigitalwiki.NetworkingTermsEntryFragment;

public class CategoryDaoImpl implements CategoryDao{

    private FirebaseUser firebaseUser;
    private FirebaseDatabase rootNode;

    private String categoryName;
    private Post editedBy;
    private CategoryTerm mCategoryTerm;

    private DigitalDataTermsEntryFragment digitalDataTermsEntryFragment;
    private FragmentDigitalDataTermsEntryBinding digitalDataTermsEntryBinding;


    private FragmentNetworkingTermsEntryBinding networkingTermsEntryBinding;
    private NetworkingTermsEntryFragment networkingTermsEntryFragment;

    private AutomatisationTermsEntryFragment automatisationTermsEntryFragment;
    private FragmentAutomatisationTermsEntryBinding automatisationTermsEntryBinding;

    private DigitalClientAccessTermsEntryFragment digitalClientAccessTermsEntryFragment;
    private FragmentDigitalClientAccessTermsEntryBinding digitalClientAccessTermsEntryBinding;

    private DigitalwikiExplanationInputFragment digitalwikiExplanationInputFragment;
    private FragmentDigitalwikiExplanationInputBinding digitalwikiExplanationInputBinding;

    private EditFragment editFragment;
    private FragmentEditBinding editBinding;

    private EditDevelopmentFragment editDevelopmentFragment;
    private FragmentEditDevelopmentBinding editDevelopmentBinding;

    private ExplanationFragment explanationFragment;
    private FragmentExplanationBinding explanationBinding;

    private DigitalwikiAllTermsFragment digitalwikiAllTermsFragment;
    private FragmentDigitalwikiAllTermsBinding digitalwikiAllTermsBinding;

    private DigitalwikiTermExplanationFragment digitalwikiTermExplanationFragment;
    private FragmentDigitalwikiTermExplanationBinding digitalwikiTermExplanationBinding;

    private CategoryFragment categoryFragment;
    private FragmentCategoryBinding categoryBinding;

    public CategoryDaoImpl(DigitalDataTermsEntryFragment digitalDataTermsEntryFragment, FragmentDigitalDataTermsEntryBinding digitalDataTermsEntryBinding ){

        this.digitalDataTermsEntryBinding = digitalDataTermsEntryBinding;
        this.digitalDataTermsEntryFragment = digitalDataTermsEntryFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        categoryName = "digitale-daten";

    }

    public CategoryDaoImpl(NetworkingTermsEntryFragment networkingTermsEntryFragment, FragmentNetworkingTermsEntryBinding networkingTermsEntryBinding) {

        this.networkingTermsEntryBinding = networkingTermsEntryBinding;
        this.networkingTermsEntryFragment = networkingTermsEntryFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        categoryName = "vernetzung";

    }

    public CategoryDaoImpl( AutomatisationTermsEntryFragment automatisationTermsEntryFragment, FragmentAutomatisationTermsEntryBinding automatisationTermsEntryBinding ) {

        this.automatisationTermsEntryBinding = automatisationTermsEntryBinding;
        this.automatisationTermsEntryFragment = automatisationTermsEntryFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        categoryName = "automatisierung";
    }

    public CategoryDaoImpl( DigitalClientAccessTermsEntryFragment digitalClientAccessTermsEntryFragment, FragmentDigitalClientAccessTermsEntryBinding digitalClientAccessTermsEntryBinding ) {

        this.digitalClientAccessTermsEntryBinding = digitalClientAccessTermsEntryBinding;
        this.digitalClientAccessTermsEntryFragment = digitalClientAccessTermsEntryFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        categoryName = "digitaler-kundenzugang";
    }

    public CategoryDaoImpl( EditFragment editFragment, FragmentEditBinding editBinding ) {

        this.editBinding = editBinding;
        this.editFragment = editFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public CategoryDaoImpl( DigitalwikiExplanationInputFragment digitalwikiExplanationInputFragment, FragmentDigitalwikiExplanationInputBinding digitalwikiExplanationInputBinding ) {

        this.digitalwikiExplanationInputBinding = digitalwikiExplanationInputBinding;
        this.digitalwikiExplanationInputFragment = digitalwikiExplanationInputFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public CategoryDaoImpl( EditDevelopmentFragment editDevelopmentFragment, FragmentEditDevelopmentBinding editDevelopmentBinding ) {

        this.editDevelopmentBinding = editDevelopmentBinding;
        this.editDevelopmentFragment = editDevelopmentFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public CategoryDaoImpl( ExplanationFragment explanationFragment, FragmentExplanationBinding explanationBinding ) {

        this.explanationBinding = explanationBinding;
        this.explanationFragment = explanationFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public CategoryDaoImpl( DigitalwikiAllTermsFragment digitalwikiAllTermsFragment, FragmentDigitalwikiAllTermsBinding digitalwikiAllTermsBinding ) {

        this.digitalwikiAllTermsBinding = digitalwikiAllTermsBinding;
        this.digitalwikiAllTermsFragment = digitalwikiAllTermsFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public CategoryDaoImpl( DigitalwikiTermExplanationFragment digitalwikiTermExplanationFragment, FragmentDigitalwikiTermExplanationBinding digitalwikiTermExplanationBinding ) {

        this.digitalwikiTermExplanationBinding = digitalwikiTermExplanationBinding;
        this.digitalwikiTermExplanationFragment = digitalwikiTermExplanationFragment;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public CategoryDaoImpl( CategoryFragment categoryFragment, FragmentCategoryBinding categoryBinding ) {

        this.categoryFragment = categoryFragment;
        this.categoryBinding = categoryBinding;

        rootNode = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public void firebaseUpdateCategoryWithTerm( String categoryName, CategoryTerm mCategoryTerm ) {

        editedBy = mCategoryTerm.getEditedBy();

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            //get the current usr´s name from firebase
            rootNode.getReference().child("users").child(userId).child("username")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    String username = snapshot.getValue(String.class);
                    editedBy.setUsername(username);

                    //get the schoolname from firebase
                    rootNode.getReference().child("users").child(userId).child("schoolname")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    String schoolName = snapshot.getValue(String.class);

                                    //let it know as a term without an explanation
                                    rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                            .child("categories").child(categoryName).child("terms")
                                            .child(mCategoryTerm.getTerm()).child("explained").setValue("false")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                    if( task.isSuccessful() ){

                                                        //save the term in the corresponding category
                                                        rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                .child("categories").child(categoryName).child("terms")
                                                                .child(mCategoryTerm.getTerm()).child("term")
                                                                .setValue(mCategoryTerm.getTerm()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                if( task.isSuccessful() ){
                                                                    //save the infos about the user who posted the term, the time and the explanation
                                                                    //all of them save as a Post-object
                                                                    rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                            .child("categories").child(categoryName).child("terms")
                                                                            .child(mCategoryTerm.getTerm()).child("edited-by")
                                                                            .child(editedBy.getUsername()).setValue(editedBy)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                                                @Override
                                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                                    if(task.isSuccessful()){

                                                                                        //add the new term as button in the category´s view
                                                                                        if( digitalDataTermsEntryFragment != null ){
                                                                                            createTermButton( mCategoryTerm.getTerm(), digitalDataTermsEntryFragment.getContext(), "false" );
                                                                                        }

                                                                                        if( networkingTermsEntryFragment != null ){
                                                                                            createTermButton( mCategoryTerm.getTerm(), networkingTermsEntryFragment.getContext(), "false" );
                                                                                        }

                                                                                        if( automatisationTermsEntryFragment != null ){
                                                                                            createTermButton( mCategoryTerm.getTerm(), automatisationTermsEntryFragment.getContext(), "false" );
                                                                                        }

                                                                                        if( digitalClientAccessTermsEntryFragment != null ){
                                                                                            createTermButton( mCategoryTerm.getTerm(), digitalClientAccessTermsEntryFragment.getContext(), "false" );
                                                                                        }

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
    public void firebaseUpdateTermWithCategory(String category, String termName) {


        if (firebaseUser.getUid() != null) {

            String userId = firebaseUser.getUid();

            //save category´s name with short letters
            String finalCategory = category.toLowerCase();

            //get the schoolname from firebase
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                    .child("terms").child(termName).child("categories")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            String categories = snapshot.getValue(String.class);
                                            if( categories != null && categories.length() == 0 ){
                                                categories = finalCategory;
                                            }else if( categories != null && categories.length() > 0 ){
                                                categories = categories + ", " + finalCategory;
                                            }


                                            //a category can´t be save twice
                                            HashSet<String> allCategories = new HashSet<>();
                                            String [] cat = categories.split(", ");

                                            for( String c : cat ){

                                                allCategories.add(c);
                                            }

                                            int allCategoriesLength = allCategories.toString().length();
                                            categories = allCategories.toString().substring(1,allCategoriesLength-1);
                                            /*Iterator<String> it = allCategories.iterator();
                                            while( it.hasNext() ){

                                                if( categories.length() == 0 ){
                                                    categories = (String)it.next();
                                                }else if( categories.length() > 0 ){

                                                    categories += ", " + (String)it.next();
                                                }
                                            }*/

                                            //update with the new inserted category
                                            rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                    .child("terms").child(termName).child("categories").setValue(categories);
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
    public void firebaseGetCategoryTerms(String categoryName) {

        if (firebaseUser.getUid() != null) {

            String userId = firebaseUser.getUid();

            //get the schoolname from firebase
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                    .child("categories").child(categoryName).child("terms")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            for( DataSnapshot childNode : snapshot.getChildren() ){

                                                if( categoryFragment != null ){

                                                    String mTerm = childNode.getKey();
                                                    String explained = childNode.child("explained").getValue(String.class);
                                                    createCategoryTermButton( mTerm, categoryFragment.getContext(), explained );
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
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Post getEditedBy() {
        return editedBy;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPost( String explanation, String username ) {

        LocalDateTime mDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        String datetimeString = editDatetimeToString( mDateTime );

        this.editedBy = new Post( datetimeString, explanation, username );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String editDatetimeToString( LocalDateTime editDatime ){

        String dateToString = "";

        //set day
        int day = editDatime.getDayOfMonth();
        if( day < 10 ){
            dateToString = "0" + day;
        }else{
            dateToString = "" + day;
        }

        //set month
        int month = editDatime.getMonthValue();
        if( month < 10 ){

            dateToString += ".0" + month;

        }else{
            dateToString += "." + month;
        }

        //add year to string
        dateToString += "." + editDatime.getYear();

        //set hour
        int hour = editDatime.getHour();
        if( hour < 10 ){
            dateToString += " " + "0" + hour;
        }else{
            dateToString += " " + hour;
        }

        int minutes = editDatime.getMinute();
        if( minutes < 10 ){
            dateToString += ":0" + minutes;
        }else{
            dateToString += ":" + minutes;
        }

        int seconds = editDatime.getSecond();
        if( seconds < 10 ){
            dateToString += ":0" + seconds;
        }else{
            dateToString += ":" + seconds;
        }

        return dateToString;
    }

    @Override
    public CategoryTerm getmCategoryTerm() {
        return mCategoryTerm;
    }

    @Override
    public void createCategoryTerm( String term, Post editedBy ) {

        this.mCategoryTerm = new CategoryTerm(term, editedBy);
    }

    @Override
    public void firebaseGetCategoryTermsAndShow( String categoryName ) {

        //Get all category´s terms and show them as buttons
        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();

            //get the schoolname from firebase
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            if( schoolName != null ){

                                //Get all category´s terms
                                rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                        .child("categories").child(categoryName).child("terms")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                for( DataSnapshot childNode : snapshot.getChildren() ){

                                                    String keyNode = childNode.getKey();
                                                    rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                            .child("categories").child(categoryName).child("terms")
                                                            .child(keyNode).child("explained")
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                    String explained = snapshot.getValue(String.class);

                                                                    //and show them as buttons
                                                                    if( digitalDataTermsEntryFragment != null ){
                                                                        createTermButton( keyNode, digitalDataTermsEntryFragment.getContext(), explained );
                                                                    }

                                                                    if( networkingTermsEntryFragment != null ){
                                                                        createTermButton( keyNode, networkingTermsEntryFragment.getContext(), explained );
                                                                    }

                                                                    if( automatisationTermsEntryFragment != null ){
                                                                        createTermButton( keyNode, automatisationTermsEntryFragment.getContext(), explained );
                                                                    }

                                                                    if( digitalClientAccessTermsEntryFragment != null ){
                                                                        createTermButton( keyNode, digitalClientAccessTermsEntryFragment.getContext(), explained );
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                }
                                                            });


                                                }

                                                //once a button clicked, show the corresponding view for inserting an explanation
                                                if( categoryName != null && categoryName.equals("digitale-daten") ){
                                                    digitalDataOpenExplanationFragmentForTerm();
                                                }

                                                if( categoryName != null && categoryName.equals("vernetzung") ){
                                                    networkingOpenExplanationFragmentForTerm();
                                                }

                                                if( categoryName != null && categoryName.equals("automatisierung") ){
                                                    automatisationOpenExplanationFragmentForTerm();
                                                }

                                                if( categoryName != null && categoryName.equals("digitaler-kundenzugang") ){
                                                    digitalClientAccessOpenExplanationFragmentForTerm();
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
    public void firebaseGetTermsCategoriesAndShow() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void firebaseSaveTermExplanation( String term, String explanation) {

        LocalDateTime mDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        String editDatetime = editDatetimeToString( mDateTime );

        Post postBy = new Post( editDatetime, explanation, "");

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();

            //get username
            rootNode.getReference().child("users").child(userId).child("username")
                    .addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                           String username = snapshot.getValue(String.class);
                           postBy.setUsername(username);

                           //get the schoolname from firebase
                           rootNode.getReference().child("users").child(userId).child("schoolname")
                                   .addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                           String schoolName = snapshot.getValue(String.class);

                                           if (schoolName != null) {

                                               //save the edittime
                                               rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                       .child("terms").child(term).child("editDatetime").setValue(editDatetime)
                                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                               //save the explanation
                                                               if(task.isSuccessful() ){

                                                                   rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                           .child("terms").child(term).child("explanation").setValue(explanation + " -- hinzgefügt von " + username +" --")
                                                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                                   //set the term as explained
                                                                                   if(task.isSuccessful() ){

                                                                                       rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                                               .child("terms").child(term).child("explained").setValue("true")
                                                                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                   @Override
                                                                                                   public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                                                       //save the username
                                                                                                       if( task.isSuccessful() ){

                                                                                                           rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                                                                   .child("terms").child(term).child("editedBy").setValue( username )
                                                                                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                       @Override
                                                                                                                       public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                           //save the explanation also
                                                                                                                           //in ../digitalwiki/categories/terms/<termname>/explanation
                                                                                                                           if( task.isSuccessful() ){

                                                                                                                               rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                                                                                       .child("terms").child(term).child("categories")
                                                                                                                                       .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                           @Override
                                                                                                                                           public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                                                                                                               String [] allCategories = snapshot.getValue(String.class).split(",");
                                                                                                                                               //System.out.println("All Categories = " + allCategories.toString());

                                                                                                                                               for( String category : allCategories ){

                                                                                                                                                   rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                                                                                                           .child("categories").child(category).child("terms").child(term)
                                                                                                                                                           .child("edited-by").child(username).setValue(postBy)
                                                                                                                                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                               @Override
                                                                                                                                                               public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                                                                                                                   if( task.isSuccessful() ){

                                                                                                                                                                       rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                                                                                                                               .child("categories").child(category).child("terms").child(term)
                                                                                                                                                                               .child("explained").setValue("true")
                                                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                    @Override
                                                                                                                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                                                                                                                                        if( task.isSuccessful() ){

                                                                                                                                                                                            rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                                                                                                                                                    .child("categories").child(category).child("terms").child(term)
                                                                                                                                                                                                    .child("term").setValue(term);
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                });
                                                                                                                                                                   }
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
                                                                                                                   });

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void showTermsCategories( String categories, String term ) {

        final String[][] allCategories = {null};

        if( categories != null && categories.length() > 0 ){

            allCategories[0] = categories.split(", ");

            for( String category : allCategories[0]){

                category = Character.toUpperCase(category.charAt(0)) + category.substring(1);
                System.out.println("Category to be inserted 1: " +  category);
                createCategoryButton( category, term, digitalwikiTermExplanationFragment.getContext() );
            }

        }else{

            //get term´s categories from firebase
            //Get all category´s terms and show them as buttons
            if( firebaseUser.getUid() != null ) {

                String userId = firebaseUser.getUid();

                //get the schoolname from firebase
                rootNode.getReference().child("users").child(userId).child("schoolname")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                String schoolName = snapshot.getValue(String.class);

                                if ( schoolName != null ) {

                                    rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                            .child("terms").child(term)
                                            .child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            String categories = snapshot.getValue(String.class);
                                            allCategories[0] = categories.split(", ");

                                            if( categories != null && categories.length() > 0 ){

                                                for( String category : allCategories[0]){

                                                    category = Character.toUpperCase(category.charAt(0)) + category.substring(1);
                                                    System.out.println("Category to be inserted 2 : " +  category);
                                                    createCategoryButton( category, term, digitalwikiTermExplanationFragment.getContext() );
                                                }

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
        }//end of if-else

    }

    @Override
    public void showTermExplanationAndShow( String term ) {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();

            //get the schoolname from firebase
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            if (schoolName != null) {

                                rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                        .child("terms").child(term).child("explanation")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                String explanation = snapshot.getValue(String.class);

                                                if( digitalwikiTermExplanationBinding != null ){

                                                    if( explanation != null && explanation.length() > 0 ){

                                                        digitalwikiTermExplanationBinding.termExplanation.setText(explanation);
                                                        digitalwikiTermExplanationBinding.buttonSaveExplanation.setVisibility( View.INVISIBLE);

                                                    }else{

                                                        digitalwikiTermExplanationBinding.termExplanation.setVisibility(View.INVISIBLE);
                                                        digitalwikiTermExplanationBinding.termEdit.setVisibility(View.VISIBLE);
                                                        digitalwikiTermExplanationBinding.buttonSaveExplanation.setVisibility( View.VISIBLE);
                                                    }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void createCategory(String category, String termName, String explanation, Category mCategory) {

        LocalDateTime mDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        String editDatetime = editDatetimeToString( mDateTime );

        Post postBy = new Post( editDatetime, explanation, "");
        String explained = "false";

        if( explanation != null && explanation.length() > 0 ){

            explained = "true";
        }

        //category´s name should be with short letters
        //category = category.toLowerCase();
        if (firebaseUser.getUid() != null) {

            String userId = firebaseUser.getUid();

            String finalExplained = explained;
            //category´s name should be with short letters
            String finalCategory = category.toLowerCase();

            rootNode.getReference().child("users").child(userId).child("username")
                    .addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                           String username = snapshot.getValue(String.class);
                           postBy.setUsername(username);

                           //get the schoolname from firebase
                           rootNode.getReference().child("users").child(userId).child("schoolname")
                                   .addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                           String schoolName = snapshot.getValue(String.class);

                                           if (schoolName != null) {

                                             /*  rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                       .child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                       boolean categoryFound = false;

                                                       for( DataSnapshot childNode : snapshot.getChildren() ){

                                                           System.out.println("Search category1: " + category );
                                                           System.out.println("childNode-key: " + childNode.getKey());

                                                           String key = childNode.getKey();
                                                           if( !key.equals(category) ){

                                                               System.out.println("Search category2: " + category );

                                                               categoryFound = true;
                                                               DatabaseReference mDbRef = rootNode.getReference("schools/"+schoolName+"/digitalwiki/categories/"+category+"/terms/");
                                                               mDbRef.child(termName).child("explained").setValue(finalExplained);
                                                               break;
                                                           }
                                                       }
                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                   }
                                               });*/

                                               //save the category and his term in /schools/<schoolname>/digitalwiki/categories/<categoryname>/terms/<termname>/...
                                               rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                       .child("categories").child(finalCategory).child("terms").child(termName)
                                                       .child("explained").setValue(finalExplained).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                       if( task.isSuccessful() ){

                                                           rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                   .child("categories").child(finalCategory).child("terms").child(termName)
                                                                   .child("term").setValue(termName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                               @Override
                                                               public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                   if( task.isSuccessful() ){

                                                                       rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                               .child("categories").child(finalCategory).child("terms").child(termName)
                                                                               .child("edited-by").child(username).setValue(postBy);
                                                                   }
                                                               }
                                                           });
                                                       }
                                                   }
                                               });
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
    public void networkingOpenExplanationFragmentForTerm() {

        //except the floating button
        for( int i = 1; i < networkingTermsEntryBinding.categoryTermsTable.getChildCount(); i++ ){

            View view = networkingTermsEntryBinding.categoryTermsTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow tableRow = (TableRow) view;
                //System.out.println("Button clicked!1");

                for( int j = 0; j < tableRow.getChildCount(); j++ ){

                    Button btn = (Button)tableRow.getChildAt(j);
                    String categoryTerm = btn.getText().toString().trim();

                    ((Button)((TableRow)networkingTermsEntryBinding.categoryTermsTable.getChildAt(i)).getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //pass the category´s term (which is actually the button´s name)
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("categoryTerm", new String [] {categoryTerm, categoryName});
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_explanation_input, bundle);
                        }
                    });
                }
            }
        }

    }

    @Override
    public void automatisationOpenExplanationFragmentForTerm() {

        //except the floating button
        for( int i = 1; i < automatisationTermsEntryBinding.categoryTermsTable.getChildCount(); i++ ){

            View view = automatisationTermsEntryBinding.categoryTermsTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow tableRow = (TableRow) view;

                for( int j = 0; j < tableRow.getChildCount(); j++ ){

                    Button btn = (Button)tableRow.getChildAt(j);
                    String categoryTerm = btn.getText().toString().trim();

                    ((Button)((TableRow)automatisationTermsEntryBinding.categoryTermsTable.getChildAt(i)).getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //pass the category´s term (which is actually the button´s name)
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("categoryTerm", new String [] {categoryTerm, categoryName});
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_explanation_input, bundle);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void digitalClientAccessOpenExplanationFragmentForTerm() {

        //except the floating button
        for( int i = 1; i < digitalClientAccessTermsEntryBinding.categoryTermsTable.getChildCount(); i++ ){

            View view = digitalClientAccessTermsEntryBinding.categoryTermsTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow tableRow = (TableRow) view;
                //System.out.println("Button clicked!1");

                for( int j = 0; j < tableRow.getChildCount(); j++ ){

                    Button btn = (Button)tableRow.getChildAt(j);
                    String categoryTerm = btn.getText().toString().trim();

                    ((Button)((TableRow)digitalClientAccessTermsEntryBinding.categoryTermsTable.getChildAt(i)).getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //pass the category´s term (which is actually the button´s name)
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("categoryTerm", new String [] {categoryTerm, categoryName});
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_explanation_input, bundle);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void digitalDataOpenExplanationFragmentForTerm(){

        //except the floating button
        for( int i = 1; i < digitalDataTermsEntryBinding.categoryTermsTable.getChildCount(); i++ ){

            View view = digitalDataTermsEntryBinding.categoryTermsTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow tableRow = (TableRow) view;
                //System.out.println("Button clicked!1");

                for( int j = 0; j < tableRow.getChildCount(); j++ ){

                    Button btn = (Button)tableRow.getChildAt(j);
                    String categoryTerm = btn.getText().toString().trim();

                    ((Button)((TableRow)digitalDataTermsEntryBinding.categoryTermsTable.getChildAt(i)).getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //pass the category´s term (which is actually the button´s name)
                            //System.out.println("Button clicked!2");
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("categoryTerm", new String [] {categoryTerm, categoryName});
                            Navigation.findNavController(v).navigate(R.id.digitalwiki_explanation_input, bundle);
                        }
                    });
                }
            }
        }

    }


    @Override
    public void openExplanationFragmentForTerm( Term mTerm ){

        for( int i = 0; i < digitalwikiAllTermsBinding.allTerms.getChildCount(); i++ ){

            View view = digitalwikiAllTermsBinding.allTerms.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow tableRow = (TableRow) view;
                //System.out.println("Button clicked!1");

                for( int j = 0; j < tableRow.getChildCount(); j++ ){


                    Button btn = (Button)tableRow.getChildAt(j);
                    String term = btn.getText().toString().trim();

                    if( term.equals( mTerm.getTermName()) ){

                        String categories = mTerm.getCategories();

                        ((Button)((TableRow)digitalwikiAllTermsBinding.allTerms.getChildAt(i)).getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //pass the category´s term (which is actually the button´s name)
                                //System.out.println("Button clicked!2");
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("categoryAndTerm", new String [] {term, categories});
                                Navigation.findNavController(v).navigate(R.id.digitalwiki_term_explanation, bundle);
                            }
                        });

                        break;
                    }

                }
            }
        }

    }

    @Override
    public void openCategoryFragment( String categoryName, String term ) {

        for( int i = 0; i < categoryBinding.categoryTermsTable.getChildCount(); i++ ){

            View view = categoryBinding.categoryTermsTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow tableRow = (TableRow) view;
                //System.out.println("Button clicked!1");

                for( int j = 0; j < tableRow.getChildCount(); j++ ){


                    Button btn = (Button)tableRow.getChildAt(j);
                    String category = btn.getText().toString().trim();

                    if( category.equals( categoryName ) ){

                        ((Button)((TableRow)digitalwikiAllTermsBinding.allTerms.getChildAt(i)).getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //pass the category´s term (which is actually the button´s name)
                                //System.out.println("Button clicked!2");
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("categoryAndTerm", new String [] {categoryName, term});
                                Navigation.findNavController(v).navigate(R.id.digitalwiki_show_category, bundle);
                            }
                        });

                        break;
                    }

                }
            }
        }

    }

    @Override
    public void openExplanationFragmentForTerm( String mTerm ) {

        for( int i = 0; i < categoryBinding.categoryTermsTable.getChildCount(); i++ ){

            View view = categoryBinding.categoryTermsTable.getChildAt(i);

            if( view instanceof TableRow ){

                TableRow tableRow = (TableRow) view;
                //System.out.println("Button clicked!1");

                for( int j = 0; j < tableRow.getChildCount(); j++ ){


                    Button btn = (Button)tableRow.getChildAt(j);
                    String term = btn.getText().toString().trim();

                    if( term.equals( mTerm ) ){

                        String categories = null;

                        ((Button)((TableRow)categoryBinding.categoryTermsTable.getChildAt(i)).getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //pass the category´s term (which is actually the button´s name)
                                //System.out.println("Button clicked!2");
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("categoryAndTerm", new String [] {term, categories});
                                Navigation.findNavController(v).navigate(R.id.digitalwiki_term_explanation, bundle);
                            }
                        });

                        break;
                    }

                }
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void firebaseUpdateExplanation( String category, String selectedTerm, String explanation ) {

        LocalDateTime mDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        String editDatetime = editDatetimeToString( mDateTime );

        editedBy = new Post( editDatetime, explanation, "");

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();
            //get the current usr´s name from firebase
            rootNode.getReference().child("users").child(userId).child("username")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String username = snapshot.getValue(String.class);
                            editedBy.setUsername(username);

                            //get the schoolname from firebase
                            rootNode.getReference().child("users").child(userId).child("schoolname")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            String schoolName = snapshot.getValue(String.class);

                                            //save the explanation in the corresponding category
                                            if( schoolName != null ){

                                                rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                        .child("categories").child(category).child("terms")
                                                        .child(selectedTerm).child("edited-by").child(username)
                                                        .setValue(editedBy).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                        //let it know as a term with a given explanation
                                                        if( task.isSuccessful() ){

                                                            rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                                    .child("categories").child(category).child("terms")
                                                                    .child(selectedTerm).child("explained").setValue("true");

                                                        }
                                                    }
                                                });

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
    public void firebaseGetExplanationsAndShow( String category, String selectedTerm ) {

        if( firebaseUser.getUid() != null ) {

//            System.out.println( " Category = " + category );
//            System.out.println( " Term = " + selectedTerm );
            String userId = firebaseUser.getUid();
            //get the current usr´s name from firebase
            rootNode.getReference().child("users").child(userId).child("username")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String username = snapshot.getValue(String.class);

                            //get the schoolname from firebase
                            rootNode.getReference().child("users").child(userId).child("schoolname")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            String schoolName = snapshot.getValue(String.class);

                                            //save the explanation in the corresponding category
                                            if( schoolName != null ){

                                                rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                        .child("categories").child(category).child("terms")
                                                        .child(selectedTerm).child("edited-by")
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                                for( DataSnapshot childNode : snapshot.getChildren() ) {
                                                                    // System.out.println(" Post : " + childNode.getValue(String.class));
                                                                    //get a post
                                                                    Post aPost = childNode.getValue(Post.class);

                                                                    //get the explanation and show it in tab 'ERKLÄRUNG'

                                                                    if( aPost.getExplanation().length() > 0 && explanationFragment != null && explanationFragment.getContext() != null ){
                                                                        createExplanationTextview( aPost, explanationFragment.getContext() );
                                                                    }


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

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public void firebaseGetDevelopmentsAndShow(String category, String selectedTerm) {

        if( firebaseUser.getUid() != null ) {

//            System.out.println( " Category = " + category );
//            System.out.println( " Term = " + selectedTerm );
            String userId = firebaseUser.getUid();
            //get the current usr´s name from firebase
            rootNode.getReference().child("users").child(userId).child("username")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String username = snapshot.getValue(String.class);

                            //get the schoolname from firebase
                            rootNode.getReference().child("users").child(userId).child("schoolname")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            String schoolName = snapshot.getValue(String.class);

                                            //save the explanation in the corresponding category
                                            if( schoolName != null ){

                                                rootNode.getReference().child("schools").child(schoolName).child("digitalwiki")
                                                        .child("categories").child(category).child("terms")
                                                        .child(selectedTerm).child("edited-by")
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                                for( DataSnapshot childNode : snapshot.getChildren() ) {
                                                                    // System.out.println(" Post : " + childNode.getValue(String.class));
                                                                    //get a post
                                                                    Post aPost = childNode.getValue(Post.class);

                                                                    //get the explanation and show it in tab 'ERKLÄRUNG'

                                                                    if( aPost.getExplanation().length() > 0 && editDevelopmentFragment != null && editDevelopmentFragment.getContext() != null ){
                                                                        createDevelopmentTextview( aPost, editDevelopmentFragment.getContext() );
                                                                    }


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

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void firebaseGetAllTermsAndShow() {

        if( firebaseUser.getUid() != null ) {

            String userId = firebaseUser.getUid();

            //get the schoolname from firebase
            rootNode.getReference().child("users").child(userId).child("schoolname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String schoolName = snapshot.getValue(String.class);

                            if (schoolName != null) {

                                rootNode.getReference().child("schools").child(schoolName).child("digitalwiki").child("terms")
                                        .addValueEventListener(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                //get all Terms as nodes
                                                for( DataSnapshot childNode : snapshot.getChildren() ){

                                                    Term mTerm = childNode.getValue( Term.class );

                                                    if( digitalwikiAllTermsFragment != null ){
                                                        createTermButton( mTerm, digitalwikiAllTermsFragment.getContext() );
                                                    }


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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createCategoryButton(String categoryName, String term, Context mContext ){

        Button btn = new Button(mContext);

        TableRow row = new TableRow(mContext);

        TableRow.LayoutParams params = new TableRow.LayoutParams( 400, TableRow.LayoutParams.WRAP_CONTENT);
        //layout.setWeightSum(12.0f);
        params.weight = 0;
        btn.setLayoutParams(params);
        btn.setTextColor(mContext.getResources().getColor(R.color.black));
        btn.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.jil_green));
        btn.setHeight(TableRow.LayoutParams.WRAP_CONTENT);

        //btn.setWidth(300);
        //btn.setId(1);
        btn.setSingleLine(false);
        btn.setText( categoryName );
        row.addView(btn);

        if( digitalwikiTermExplanationBinding != null ){
            digitalwikiTermExplanationBinding.allCategories.addView(row);
            //enale the new button to show the explanation´s view once clicked
            //openCategoryFragment( categoryName, term );
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createCategoryTermButton(String mTerm, Context mContext, String explained ){

        Button btn = new Button(mContext);

        TableRow row = new TableRow(mContext);

        TableRow.LayoutParams params = new TableRow.LayoutParams( 400, TableRow.LayoutParams.WRAP_CONTENT);
        //layout.setWeightSum(12.0f);
        params.weight = 0;
        btn.setLayoutParams(params);
        btn.setTextColor(mContext.getResources().getColor(R.color.black));

        if( explained.equals("false") ) {
            btn.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.jil_grey));
        }

        if( explained.equals("true") ){
            btn.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.jil_yellow));
        }

        btn.setHeight(TableRow.LayoutParams.WRAP_CONTENT);

        //btn.setWidth(300);
        //btn.setId(1);
        btn.setSingleLine(false);
        btn.setText( mTerm );
        row.addView(btn);

        if( categoryBinding!= null ){
            categoryBinding.categoryTermsTable.addView(row);
            //enale the new button to show the explanation´s view once clicked
            openExplanationFragmentForTerm( mTerm );
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createTermButton(Term mTerm, Context mContext ){

        Button btn = new Button(mContext);

        TableRow row = new TableRow(mContext);

        TableRow.LayoutParams params = new TableRow.LayoutParams( 400, TableRow.LayoutParams.WRAP_CONTENT);
        //layout.setWeightSum(12.0f);
        params.weight = 0;
        btn.setLayoutParams(params);
        btn.setTextColor(mContext.getResources().getColor(R.color.black));

        if( mTerm.getExplained().equals("false") ) {
            btn.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.jil_grey));
        }

        if( mTerm.getExplained().equals("true") ){
            btn.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.jil_yellow));
        }

        btn.setHeight(TableRow.LayoutParams.WRAP_CONTENT);

        //btn.setWidth(300);
        //btn.setId(1);
        btn.setSingleLine(false);
        btn.setText( mTerm.getTermName() );
        row.addView(btn);

        if( digitalwikiAllTermsBinding != null ){
            digitalwikiAllTermsBinding.allTerms.addView(row);
            //enale the new button to show the explanation´s view once clicked
            openExplanationFragmentForTerm( mTerm );
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createTermButton( String term, Context mContext, String explained ){

        //name of the button
        String s [] = term.split(" ");
        String btnName = "";

        for( String n : s ){

            btnName += "_" + n;
        }

        btnName = "button" + btnName; //for setting the id

        Button btn = new Button(mContext);

        TableRow row = new TableRow(mContext);

        TableRow.LayoutParams params = new TableRow.LayoutParams( 400, TableRow.LayoutParams.WRAP_CONTENT);
        //layout.setWeightSum(12.0f);
        params.weight = 0;
        btn.setLayoutParams(params);
        btn.setTextColor(mContext.getResources().getColor(R.color.black));

        if( explained.equals("false") ) {
            btn.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.jil_grey));
        }

        if( explained.equals("true") ){
            btn.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.jil_yellow));
        }

        btn.setHeight(TableRow.LayoutParams.WRAP_CONTENT);

        //btn.setWidth(300);
        //btn.setId(1);
        btn.setSingleLine(false);
        btn.setText(term);
        row.addView(btn);

        if( digitalDataTermsEntryBinding != null ){
            digitalDataTermsEntryBinding.categoryTermsTable.addView(row);
            //enale the new button to show the explanation´s view once clicked
            digitalDataOpenExplanationFragmentForTerm();
        }

        if( networkingTermsEntryBinding != null ){
            networkingTermsEntryBinding.categoryTermsTable.addView(row);
            //enale the new button to show the explanation´s view once clicked
            networkingOpenExplanationFragmentForTerm();
        }

        if( automatisationTermsEntryBinding != null ){
            automatisationTermsEntryBinding.categoryTermsTable.addView(row);
            //enale the new button to show the explanation´s view once clicked
            automatisationOpenExplanationFragmentForTerm();
        }

        if( digitalClientAccessTermsEntryBinding != null ){
            digitalClientAccessTermsEntryBinding.categoryTermsTable.addView(row);
            //enale the new button to show the explanation´s view once clicked
            digitalClientAccessOpenExplanationFragmentForTerm();
        }
    }


    private void createExplanationTextview( Post aPost, Context mContext ){

        String exp = aPost.getExplanation();
        String userName = aPost.getUsername();
        String editDatetime = aPost.getEditDatetime();

        TextView tInfos = new TextView(mContext);
        tInfos.setText("    -   hinzugefügt von " + userName + " am " + editDatetime);
        tInfos.setTypeface(null, Typeface.ITALIC);

        TextView tv = new TextView(mContext);

        TableRow row = new TableRow(mContext);

        TableRow.LayoutParams params = new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        //layout.setWeightSum(12.0f);
        params.weight = 0;
        tv.setLayoutParams(params);
        tv.setTextColor(mContext.getResources().getColor(R.color.black));
        tv.setHeight(TableRow.LayoutParams.WRAP_CONTENT);

        //btn.setWidth(300);
        tv.setSingleLine(false);
        tv.setText( exp + tInfos.getText() );
        row.addView(tv);

        if( explanationBinding != null ){
            explanationBinding.explanationsTable.addView(row);
        }

    }

    private void createDevelopmentTextview( Post aPost, Context mContext ){

        String userName = aPost.getUsername();
        String editDatetime = aPost.getEditDatetime();

        TextView tv = new TextView(mContext);

        TableRow row = new TableRow(mContext);

        TableRow.LayoutParams params = new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        //layout.setWeightSum(12.0f);
        params.weight = 0;
        tv.setLayoutParams(params);
        tv.setTextColor(mContext.getResources().getColor(R.color.black));
        tv.setHeight(TableRow.LayoutParams.WRAP_CONTENT);

        //btn.setWidth(300);
        tv.setSingleLine(false);
        tv.setText( "-  Eine neue Eklärung wurde von " + userName + " am " + editDatetime + " hinzugefügt.");
        tv.setTypeface(null, Typeface.ITALIC);
        row.addView(tv);

        if( editDevelopmentBinding != null ){
            editDevelopmentBinding.developmentsTable.addView(row);
        }

    }



}
