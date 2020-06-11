package kr.ac.jbnu.se.mobile.ganogano;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class PracticeEditActivity extends AppCompatActivity {

    private TextView APeriodEditText, BPeriodEditText;
    private EditText mHospitalEditText;

    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    SharedPreferences sharedPref = null;
    SharedPreferences.Editor editor = null;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference database;

    private String key;
    private String aperiod, bperiod, hospital;
    private CheckBox DcheckBox;
    private ArrayList<Integer> a_list, b_list, n_list, u_list;//a_list는 변경 값 , b_list는 D-Day로 설정된 마감날짜, n_list는 현재 페이지의 마감 날짜
    private Calendar mCalendar;
    private String result = null;
    final String dayformat = "%d 년 %d 월 %d일"; //날자형식

    private DatePickerDialog.OnDateSetListener BDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) {
            //값 넣기
            if(a_list!=null) a_list.clear();
            a_list.add(a_year);
            a_list.add(a_monthOfYear);
            a_list.add(a_dayOfMonth);
            n_list = a_list;
            BPeriodEditText.setText(String.format(dayformat, a_year, a_monthOfYear + 1, a_dayOfMonth));
        }
    };
    // DatePicker 에서 날짜 선택 시 호출
    private DatePickerDialog.OnDateSetListener ADateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) {
            APeriodEditText.setText(String.format(dayformat, a_year, a_monthOfYear + 1, a_dayOfMonth));
        }
    };

    public PracticeEditActivity() {
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practiceedit);

        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();

        BPeriodEditText = (TextView) findViewById(R.id.period_edit_end);
        APeriodEditText = (TextView) findViewById(R.id.period_edit_start);
        mHospitalEditText = (EditText) findViewById(R.id.hospital_edit);
        DcheckBox = findViewById(R.id.date_checkbox);

        Bundle bundle = getIntent().getExtras();
        //전에 저장되었던 값 저장

        key = bundle.getString("key");
        aperiod = bundle.getString("aperiod");
        bperiod = bundle.getString("bperiod");
        hospital = bundle.getString("hospital");
        n_list = bundle.getIntegerArrayList("day");

        b_list = new ArrayList<>();
        a_list = new ArrayList<>();
        b_list.clear();//숫자를 유지하기 위해
        b_list.add(sharedPref.getInt("year", 0));
        b_list.add(sharedPref.getInt("month", 0));
        b_list.add(sharedPref.getInt("date", 0));
        u_list = new ArrayList<>();//임시변수
        if (key != null) {
            BPeriodEditText.setText(bperiod);
            APeriodEditText.setText(aperiod);
            mHospitalEditText.setText(hospital);
        }

        Locale.setDefault(Locale.KOREAN);

        mCalendar = new GregorianCalendar();

        // Input date click 시 date picker 호출
        BPeriodEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PracticeEditActivity.this, BDateSetListener, year, month, day);

                dialog.show();
            }
        });

        // Input date click 시 date picker 호출
        APeriodEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PracticeEditActivity.this, ADateSetListener, year, month, day);
                dialog.show();
            }
        });

        DcheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_list = n_list;
                if (DcheckBox.isChecked() == true && a_list.size()!=0) {
                    u_list = a_list;
                } else if(DcheckBox.isChecked() == false){
                    u_list = b_list;
                }
                // 변경이 없을 경우 bundle 값 그대로. 클릭했으므로
                editor.putInt("year", u_list.get(0));
                editor.putInt("month", u_list.get(1));
                editor.putInt("date", u_list.get(2));
                editor.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_memoedit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (key == null) {
            switch (item.getItemId()) {
                case R.id.action_cancel:
                    cancel();
                    return true;
                case R.id.action_save:
                    save();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            switch (item.getItemId()) {
                case R.id.action_cancel:
                    cancel();
                    return true;
                case R.id.action_save:
                    renew();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    private void renew() {
        Do_Notification("수정되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("aperiod", APeriodEditText.getText().toString());
        intent.putExtra("bperiod", BPeriodEditText.getText().toString());
        intent.putExtra("hospital", mHospitalEditText.getText().toString());
        intent.putExtra("key", key);
        intent.putExtra("day", n_list);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void save() {

        Do_Notification("저장되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("aperiod", APeriodEditText.getText().toString());
        intent.putExtra("bperiod", BPeriodEditText.getText().toString());
        intent.putExtra("hospital", mHospitalEditText.getText().toString());
        intent.putExtra("day", a_list);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void Do_Notification(String string) {
        //알람 설정
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_box_black_24dp))
                .setContentTitle("가노간호 실습")
                .setContentText(string)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            CharSequence channelName = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        } else
            builder.setSmallIcon(R.mipmap.ic_launcher);

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build());
    }
}

