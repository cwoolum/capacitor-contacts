package com.cwoolum.contacts.models;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;

import java.util.ArrayList;

public class Contact {
    String id;
    String name;
    ArrayList<String> phoneNumbers;

    ArrayList<String> emails;

    public Contact(){
        this.phoneNumbers = new ArrayList<>();
        this.emails = new ArrayList<>();
    }

    public Contact(String name, String id){
        this();

        this.name = name;
        this.id = id;
    }

    public void addPhoneNumber(String phoneNumber){
        this.phoneNumbers.add(phoneNumber);
    }

    public void addEmail(String email){
        this.emails.add(email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSObject toJSObject(){
        JSObject object = new JSObject();
        object.put("name", this.name);
        object.put("id", this.id);

        JSArray emails = new JSArray(this.emails);
        object.put("emails", emails);

        JSArray phoneNumbers = new JSArray(this.phoneNumbers);
        object.put("phoneNumbers", phoneNumbers);

        return object;
    }
}
