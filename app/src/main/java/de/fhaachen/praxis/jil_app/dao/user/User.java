package de.fhaachen.praxis.jil_app.dao.user;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String schoolName;
    private String role;
    private String userId;
    private String terms;

    public User() {
    }

    public User(String firstname, String lastname, String phoneNumber, String schoolName, String role) { //, String schoolName

        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.role = role;
        this.terms = "";
    }

    public User(Parcel source) {

        firstname = source.readString();
        lastname = source.readString();
        phoneNumber = source.readString();
        schoolName = source.readString();
        role = source.readString();
        terms = source.readString();

    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //Getters und Setters
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSchoolName() {
        return schoolName;

    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(phoneNumber);
        dest.writeString(schoolName);
        dest.writeString(role);
        dest.writeString(terms);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", role='" + role + '\'' +
                ", userId='" + userId + '\'' +
                ", terms='" + terms + '\'' +
                '}';
    }
}
