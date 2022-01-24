package de.fhaachen.praxis.jil_app.dao.terms_table;

public class TermsTable {

    private Cell[][] allCells;
    private boolean isFilled;
    private String allTerms;
    private int countAllTerms;

    public TermsTable(){

        allCells = new Cell[11][10];

        for( int row  = 0; row < 10; row++ ){

            for( int col = 0; col < 10; col++ ){

                String position = "" + row + "-" + col;
                allCells[row][col] = new Cell( position, "", "");

            }
        }

        //for the 101st Cell
        allCells[10][0] = new Cell( "" + 10 + "-" + 0, "", "" );

        this.isFilled = false;

        this.allTerms = "";

        this.countAllTerms = 0;
    }


    //Getters and Setters
    public Cell[][] getAllCells() {
        return allCells;
    }

    public void setAllCells(Cell[][] allCells) {
        this.allCells = allCells;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public String getAllTerms(){

        return this.allTerms;
    }

    public void setAllTerms(String allTerms) {
        this.allTerms = allTerms;

        //set the number of Terms
        String terms [] = this.allTerms.split(", ");
        this.countAllTerms = terms.length;

    }

    public int getCountAllTerms() {
        return countAllTerms;
    }

    public void setCountAllTerms(int countAllTerms) {
        this.countAllTerms = countAllTerms;
    }

    /**
     * This method check if the table is emty or not
     * @return true if the table is empty. If not false
     */
    public boolean isEmpty(){

        for( int row = 0; row < 11; row++ ){

            for( int col = 0; col < 10; col++ ){

                if( allCells[row][col] != null ){

                    if( allCells[row][col].getTerm() != "" ){

                        return false;
                    }
                }
            }
        }

        return true;
    }



    /**
     * This Method provide a cell of the table with a term
     * @param row is the rownumber of the position ofe the cell in the table
     * @param col is the columnnumber of the position ofe the cell in the table
     * @param term is the term to be inserted in the cell´s table
     */
    public void fillCellTable( int row, int col, String term ){

        String position = "" + row + "-" + col;
        this.allCells[row][col] = new Cell( position,"", term);
        this.allCells[row][col].setBearbeitet(true);
        this.countAllTerms++;
    }


    /**
     * This method update the status of a cell as being edited
     * @param row
     * @param col
     */
    public void setEditingCell( int row, int col ){

        this.allCells[row][col].setInBearbeitung(true);
        this.allCells[row][col].setBearbeitet(false);
    }



}
