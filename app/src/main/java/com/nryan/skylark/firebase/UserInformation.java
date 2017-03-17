package com.nryan.skylark.firebase;

/**
 * Created by nathan on 10/03/2017.
 */

public class UserInformation {
    public String name;
    public String address;

    public UserInformation(){

    }

    public UserInformation(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
