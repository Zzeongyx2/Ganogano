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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button btnLogOut, btnNote, btnSetting, btnMemo;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences sf;
    private SoundPool soundPool;
    private int soundID;
    private int vol;//볼륨, 추후 수정
    TextView DDay;
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    BroadcastReceiver receiver;
    String array[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf = getSharedPreferences("settings", MODE_PRIVATE);
        DDay = findViewById(R.id.DDAY);

        //디데이 구하기
        DDay.setText(getDday(sf.getInt("year", 0), sf.getInt("month", 0), sf.getInt("date", 0)));

        sf = getSharedPreferences("settings", MODE_PRIVATE);
        vol = sf.getInt("effect", 1);
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(this, R.raw.button, 1);

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
                finish();

            }
        });
        btnMemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                soundPool.play(soundID, vol, vol, 0, 0, 0);
                Intent I = new Intent(MainActivity.this, MemoActivity.class);
                startActivity(I);
                finish();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(soundID, vol, vol, 0, 0, 0);
                Intent I = new Intent(MainActivity.this, Settings.class);
                startActivity(I);
                finish();
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundPool.play(soundID, vol, vol, 0, 0, 0);
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I);
                finish();
            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                    Toast.makeText(context, "충전기 연결!", Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
                    Toast.makeText(context, "배터리 부족! 충전기를 연결해 주세요", Toast.LENGTH_LONG).show();
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
        filter.addAction("example.test.broadcast");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private String getDday(int a_year, int a_monthOfYear, int a_dayOfMonth) {
        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(a_year, a_monthOfYear, a_dayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        // 출력 시 d-day 에 맞게 표시
        final String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "D-Day";
        } else {
            result *= -1;
            strFormat = "D+%d";
        }

        final String strCount = (String.format(strFormat, result));
        return strCount; //결과 값 반환
        //Todo: Shared
    }
}
