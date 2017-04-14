package beaststudio.in.chatapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class signUp extends Fragment implements View.OnClickListener{


    public signUp() {
        // Required empty public constructor
    }

    private AutoCompleteTextView signup_email;
    private EditText signup_password;
    private Button signup_button;

    private ProgressDialog progressdialog;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signup_email = (AutoCompleteTextView)view.findViewById(R.id.signup_email);
        signup_password = (EditText)view.findViewById(R.id.signup_password);
        signup_button = (Button)view.findViewById(R.id.signup_button);

        progressdialog = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();

        signup_button.setOnClickListener(signUp.this);


        return view;
    }
    @Override
    public void onClick(View v) {
        if (v==signup_button)
        {
            registerUser();
        }
    }

    private void registerUser() {
        String email = signup_email.getText().toString().trim();
        String pass = signup_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            signup_email.setError("Email field is required");
            return;

        } else if (TextUtils.isEmpty(pass)) {
            signup_password.setError("Password field is required");
            return;

        }

        progressdialog.setMessage("Please wait...");
        progressdialog.show();
        final Intent intent = new Intent(getActivity(),homepage.class);
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            startActivity(intent);
                            getActivity().finish();
                            progressdialog.dismiss();
                        }
                        else {
                            Toast.makeText(getActivity(), "Could not register", Toast.LENGTH_SHORT).show();
                            progressdialog.dismiss();
                        }
                    }
                });
    }

}
