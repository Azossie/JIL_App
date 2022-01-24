package de.fhaachen.praxis.jil_app.dao.categories;

import android.content.Context;

import java.time.LocalDateTime;

import de.fhaachen.praxis.jil_app.dao.terms_table.Term;

public interface CategoryDao {

    public String getCategoryName();
    public void setCategoryName(String categoryName);
    public Post getEditedBy();
    public void createPost( String explanation, String username );
    public CategoryTerm getmCategoryTerm();
    public void createCategoryTerm( String term, Post editedBy );
    public void firebaseGetCategoryTermsAndShow( String categoryName );
    public void firebaseGetTermsCategoriesAndShow();
    public void firebaseSaveTermExplanation( String term, String explanation );

    public void showTermsCategories( String categories, String term );
    public void showTermExplanationAndShow( String term );
    public void firebaseUpdateCategoryWithTerm( String categoryName, CategoryTerm mCategoryTerm );
    public void firebaseUpdateTermWithCategory( String category, String termName );
    public void firebaseGetCategoryTerms( String categoryName );

    public void createCategory( String category, String termName, String explanation, Category mCategory );
    public void firebaseUpdateExplanation( String category, String selectedTerm, String explanation );
    public void firebaseGetExplanationsAndShow( String category, String selectedTerm  );
    public void firebaseGetDevelopmentsAndShow( String category, String selectedTerm );
    public void firebaseGetAllTermsAndShow();

    public void digitalDataOpenExplanationFragmentForTerm();
    public void networkingOpenExplanationFragmentForTerm();
    public void automatisationOpenExplanationFragmentForTerm();
    public void digitalClientAccessOpenExplanationFragmentForTerm();
    public void openExplanationFragmentForTerm( Term mTerm );
    public void openCategoryFragment( String categoryName, String term );
    public void openExplanationFragmentForTerm( String mTerm );

    public void createCategoryButton(String categoryName, String term, Context mContext );

}
