package com.cwoolum.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import com.cwoolum.contacts.models.Contact;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.util.ArrayList;
import java.util.Objects;

@NativePlugin(permissionRequestCode = 1, permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
public class ContactManager extends Plugin {
    private static final String TAG = ContactManager.class.getSimpleName();
    private static String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME;

    private static final String[] PROJECTION =
            {
                    /*
                     * The detail data row ID. To make a ListView work,
                     * this column is required.
                     */
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
                    // The primary display name
                    DISPLAY_NAME,
                    ContactsContract.Contacts.LOOKUP_KEY // A permanent link to the contact
            };

    @SuppressLint("InlinedApi")
    private static final String SELECTION =
            DISPLAY_NAME + " LIKE ?" +" AND " +
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";

    @PluginMethod()
    public void getContacts(PluginCall call) {

        String query = call.getString("query");

        call.save();
        if (!hasRequiredPermissions()) {
            Log.d(TAG, "Not permitted. Asking permission...");
            saveCall(call);
            pluginRequestAllPermissions();
        } else {
            ArrayList<Contact> contacts = fetchContacts(query);
            JSArray returnContacts = new JSArray();

            for(Contact contact: contacts){
                returnContacts.put(contact.toJSObject());
            }

            JSObject returnObject = new JSObject();
            returnObject.put("data", returnContacts);

            call.success(returnObject);
        }
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            return;
        }

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                savedCall.error("User denied location permission");
                return;
            }
        }

        if (savedCall.getMethodName().equals("getContacts")) {
            getContacts(savedCall);
        }
    }

    public ArrayList<Contact> fetchContacts(String query) {
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = getContext().getContentResolver();

        Cursor cursor;

        if(!Objects.equals(query, "")) {
            // Defines the array to hold values that replace the ?
            String[] selectionArgs = {query + "%"};

            cursor = contentResolver.query(CONTENT_URI, PROJECTION, SELECTION, selectionArgs, DISPLAY_NAME + " asc");
        } else {
            cursor = contentResolver.query(CONTENT_URI, PROJECTION, null, null, DISPLAY_NAME + " asc");
        }

        ArrayList<Contact> contacts = new ArrayList<>();

        if(cursor == null){
            return contacts;
        }

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                Contact contact = new Contact(name, contact_id);

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    if(phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            contact.addPhoneNumber(phoneNumber);
                        }

                        phoneCursor.close();
                    }

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                    if(emailCursor != null) {
                        while (emailCursor.moveToNext()) {
                            String email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            contact.addEmail(email);
                        }

                        emailCursor.close();
                    }
                }

                contacts.add(contact);
            }
        }

        cursor.close();

        return contacts;
    }
}
