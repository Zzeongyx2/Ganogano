package kr.ac.jbnu.se.mobile.ganogano;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button btnLogOut,btnNote,btnSetting;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences sf; //이미 저장된 값 가져오기
    TextView DDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf = getSharedPreferences("settings", MODE_PRIVATE);
        DDay = findViewById(R.id.DDAY);
        DDay.setText(sf.getString("D_DAY","No D-DAY"));//디데이 설정

        btnLogOut = (Button) findViewById(R.id.btn_logout);
        btnNote = (Button) findViewById(R.id.btn_note);
        btnSetting = findViewById(R.id.btn_setting);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I);

            }
        });
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(I);

            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(MainActivity.this, Settings.class);
                startActivity(I);
            }
        });
    }
}
