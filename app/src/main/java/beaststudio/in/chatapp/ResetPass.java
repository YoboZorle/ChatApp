package beaststudio.in.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    Button resetbtn;
    EditText resetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        resetEmail = (EditText)findViewById(R.id.reset_password);

        firebaseAuth = FirebaseAuth.getInstance();
        resetbtn=(Button)findViewById(R.id.reset_button);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passreset();
            }
        });

    }

    private void passreset() {
        String email = resetEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(ResetPass.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){


                    finish();
                    Toast.makeText(ResetPass.this, "Reset link is send to your Email", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ResetPass.this, "Try Again", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

}
