 package com.stark.satos.storycreatorversion3.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.stark.satos.storycreatorversion3.MainActivity;
import com.stark.satos.storycreatorversion3.R;
import com.stark.satos.storycreatorversion3.firestorefunctionality.AddDataToFirestore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.stark.satos.storycreatorversion3.MainActivity.globalUserVar;

 public class UserProfile extends AppCompatActivity {

    private EditText profileUserName;
    private EditText userEmail;
    private EditText userPasswordFirstCheck;
    private EditText userPasswordSecondCheck;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference storyIdCollectionRef;
    private DocumentReference storyIdDocumentReference;
    private Button saveButton;
    private AddDataToFirestore saveNewUser = new AddDataToFirestore();

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_user_profile_page);
         profileUserName = findViewById(R.id.enter_user_name);
         userEmail = findViewById(R.id.editTextTextEmailAddress);
         saveButton = findViewById(R.id.save_user_info);
         userPasswordFirstCheck = findViewById(R.id.editTextTextPassword);
         userPasswordSecondCheck = findViewById(R.id.editTextTextPassword2);

        // If button is clicked then the text fields will be checked to see if everything is filled out and correct.
        // If correct then a new user is created.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View root)
            {
                boolean checkForNull = checkForNull();
                if(checkForNull == false){
                    Toast.makeText(UserProfile.this,"Must Fill In All Text Fields", Toast.LENGTH_LONG).show();
                }
                else{
                    getEditText();
                    if(checkUserEmail() == true){
                        checkUserPassword();
                        if(checkUserPassword() == true){
                            checkUniqueUserName(globalUserVar.getProfileUserName());
                            }
                            else {
                                Toast.makeText(UserProfile.this,"User Profile not created...", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        });
    }


     private boolean checkForNull() {

        if(profileUserName.getText().toString().trim().length() < 1){
            return false;
        }
        else if(userEmail.getText().toString().trim().length() < 1){
            return false;
        }
        else if(userPasswordFirstCheck.getText().toString().trim().length() < 1){
            return false;
        }
        else if(userPasswordSecondCheck.getText().toString().trim().length() < 1){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean checkUserPassword() {

        final int passwordLengthMax = 21;
        final int passwordLengthMin = 7;
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$";
        String userPassword1;
        String userPassword2;
        int pwLength;
        Pattern passwordPattern;
        Matcher doesPasswordMatch;
        boolean passwordCheckTrue;
        final String TAG = "UserProfileFragment()";

        userPassword1 = userPasswordFirstCheck.getText().toString();
        userPassword2 = userPasswordSecondCheck.getText().toString();

        pwLength = userPassword1.length();

        if(pwLength < passwordLengthMax  && pwLength > passwordLengthMin ){
            passwordPattern = Pattern.compile(regex);
            doesPasswordMatch = passwordPattern.matcher(userPassword1);
            passwordCheckTrue = doesPasswordMatch.matches();

            if(passwordCheckTrue){
                if(userPasswordFirstCheck.getText().toString().contentEquals(userPasswordSecondCheck.getText().toString())){
                    globalUserVar.setUserPassword(userPasswordFirstCheck.getText().toString());
                }
                else{
                    Toast.makeText(UserProfile.this,"Passwords DO NOT match...", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else {
                Toast.makeText(UserProfile.this,"Password needs to contain at least one special character, "
                        + "one upper case letter, one lower case letter, and one number.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
            else {
                Toast.makeText(UserProfile.this, "Password needs to be between 8 and 20 characters long.", Toast.LENGTH_LONG).show();
                return false;
            }
        Toast.makeText(UserProfile.this, "Password is good.", Toast.LENGTH_LONG).show();
            return true;
    }

    private void getEditText() {
        globalUserVar.setUserEmail(userEmail.getText().toString());
        globalUserVar.setProfileUserName(profileUserName.getText().toString());
        globalUserVar.setUserPassword(userPasswordFirstCheck.getText().toString());
    }


    private void checkUniqueUserName(String profileUserName){

            storyIdCollectionRef = db.collection("userNames");
            storyIdDocumentReference = storyIdCollectionRef.document("listOfUserNames");

       Query query = storyIdCollectionRef.whereArrayContains("userName", profileUserName);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.size() == 0){
                        storyIdDocumentReference.update("userName", FieldValue.arrayUnion(profileUserName));
                        addUserToFirebase(globalUserVar.getUserEmail(), globalUserVar.getUserPassword());
                    }
                    else {
                        Toast.makeText(UserProfile.this,"User Name Already Exists!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


     private boolean  checkUserEmail() {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        boolean emailMatches;
        emailMatches = emailPattern.matcher(globalUserVar.getUserEmail()).matches();

        if(emailMatches == true){
             return true;
        }
        else{
             Toast.makeText(UserProfile.this,"Not and email address.  ", Toast.LENGTH_LONG).show();
             return false;
        }
     }

     public void addUserToFirebase(String email, String password){

         FirebaseAuth mAuth = FirebaseAuth.getInstance();
         mAuth.createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             // Sign in success, update UI with the signed-in user's information
                             FirebaseUser user = mAuth.getCurrentUser();
                             AddDataToFirestore saveNewUser = new AddDataToFirestore(globalUserVar.getProfileUserName(), globalUserVar.getUserEmail(), globalUserVar.getUserPassword(), globalUserVar.getProfilePicUrl());
                             openMainActivity();
                         }
                         else {
                             // If sign in fails, display a message to the user.
                             Toast.makeText(UserProfile.this, "New sign in failed." +
                                             " Do you already have an account with us?", Toast.LENGTH_LONG).show();
                         }
                     }
                 });
     }


     private void openMainActivity() {
         Intent intent = new Intent(this, MainActivity.class);
         startActivity(intent);
     }
 }