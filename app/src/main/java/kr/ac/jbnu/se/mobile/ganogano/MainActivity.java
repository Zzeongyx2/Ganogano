package kr.ac.jbnu.se.mobile.ganogano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button btnLogOut, btnNote, btnSetting, btnMemo;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences sf; //이미 저장된 값 가져오기
    private SoundPool soundPool;
    private int soundID;
    private int vol;//볼륨, 추후 수정
    TextView DDay;
    //리시버 브로그캐스트 용
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf = getSharedPreferences("settings", MODE_PRIVATE);
        DDay = findViewById(R.id.DDAY);
        DDay.setText(sf.getString("D_DAY", "No D-DAY"));//디데이 설정
        //효과음
        sf = getSharedPreferences("settings",MODE_PRIVATE);     //왜 두개?
        vol = sf.getInt("effect",1);
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(this,R.raw.button,1);

        btnLogOut = (Button) findViewById(R.id.btn_logout);
        btnMemo = (Button) findViewById(R.id.btn_memo);
        btnNote = (Button) findViewById(R.id.btn_note);
        btnSetting = findViewById(R.id.btn_setting);

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(soundID, vol, vol, 0, 0, 0);
                Intent I = new Intent(MainActivity.this, PracticeActivity.class);
                startActivity(I);

            }
        });
        btnMemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                soundPool.play(soundID, vol, vol, 0, 0, 0);
                Intent I = new Intent(MainActivity.this, MemoActivity.class);
                startActivity(I);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(soundID, vol, vol, 0, 0, 0);
                Intent I = new Intent(MainActivity.this, Settings.class);
                startActivity(I);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundPool.play(soundID, vol, vol, 0, 0, 0);
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I);

            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
                    Toast.makeText(context, "충전기 연결!", Toast.LENGTH_LONG).show();
                }else if(intent.getAction().equals(Intent.ACTION_BATTERY_LOW)){
                    Toast.makeText(context, "배터리 부족!", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(receiver,filter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
