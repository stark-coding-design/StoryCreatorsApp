package com.stark.satos.storycreatorversion3.users;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.stark.satos.storycreatorversion3.MainActivity;
import com.stark.satos.storycreatorversion3.R;

public class HomeLoginOrNewUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signInButton;
    private Button newUserButton;
    private EditText userEmail;
    private EditText userPassword;
    private TextView websiteLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_user_sign_in_or_new_user);


        mAuth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.existing_user_sign_in_button);
        newUserButton = findViewById(R.id.new_member_sign_up_button);
        userEmail = findViewById(R.id.existing_user_name);
        userPassword = findViewById(R.id.user_sign_in_password);
        websiteLink = findViewById(R.id.link_to_eula);

        String linkText = "By continuing, you agree to our End-User License Agreement and Privacy Policy <a href='https://storycreators.godaddysites.com/'>read here</a>.";
        websiteLink.setText(Html.fromHtml(linkText));
        websiteLink.setMovementMethod(LinkMovementMethod.getInstance());

        /* Goes to signInUser() method to sign in if you are an existing user */
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                signInUser();
            }
        });

        /* New users go here to fill out new profile information */
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                openUserProfileActivity();
            }
        });
    }

    private void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }


    public void signInUser() {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if(!email.equals("")){
            if(!password.equals("")){
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    openMainActivity();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(HomeLoginOrNewUser.this, "Sign in Failed. Check Username and Password.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
            else {
                Toast.makeText(HomeLoginOrNewUser.this, "Sign in Failed. Check Username and Password.",
                        Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(HomeLoginOrNewUser.this, "Sign in Failed. Check Username and Password.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}