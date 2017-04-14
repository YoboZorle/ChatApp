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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class signIn extends Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener  {


    public signIn() {
        // Required empty public constructor
    }
    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;
    AutoCompleteTextView memail;
    EditText mpassword;
    TextView mTextForgot;
    Button signinBtn;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressdialog;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        memail = (AutoCompleteTextView) view.findViewById(R.id.email);
        mpassword = (EditText) view.findViewById(R.id.password);
        mTextForgot = (TextView) view.findViewById(R.id.TextForgot);
        signinBtn = (Button) view.findViewById(R.id.email_sign_in_button);
        progressdialog = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        signInButton = (SignInButton)view.findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        mTextForgot.setOnClickListener(signIn.this);
        signinBtn.setOnClickListener(signIn.this);
        signInButton.setOnClickListener(signIn.this);

        //-------Google SignIn----------------
        // Start Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();




        //-------End Google SignIn----------------

        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.email_sign_in_button:
                userLogin();
                break;

            case R.id.TextForgot:
                resetpass();
                break;

            case R.id.signInButton:
                gsignin();
                break;
        }
    }

    private void gsignin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progressdialog.setMessage("Please Wait...");
        progressdialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        progressdialog.setMessage("Please Wait...");
        progressdialog.show();

        final Intent intent = new Intent(getActivity(), homepage.class);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressdialog.dismiss();
                    }
                });
    }

    private void resetpass() {
        Intent intent = new Intent(getActivity(),ResetPass.class);
        startActivity(intent);

    }

    private void userLogin() {

        String email = memail.getText().toString().trim();
        String pass = mpassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            memail.setError("Email field is required");
            return;

        } else if (TextUtils.isEmpty(pass)) {
            mpassword.setError("Password field is required");
            return;

        }else if (!isEmailValid(email))
        {
            memail.setFocusable(true);
            memail.setError("This email address is invalid");

            return;
        }

        progressdialog.setMessage("Please Wait...");
        progressdialog.show();
        final Intent intent = new Intent(getActivity(), homepage.class);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(intent);
                            progressdialog.dismiss();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                            progressdialog.dismiss();
                        }
                    }
                });

    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

}
