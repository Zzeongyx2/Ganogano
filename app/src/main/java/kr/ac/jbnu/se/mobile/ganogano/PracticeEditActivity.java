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
    private String period, hospital;
    private CheckBox DcheckBox;

    private Calendar ACalendar, mCalendar;
    private String result = null;
    final String dayformat = "%d 년 %d 월 %d일"; //날자형태

    //datePicker가 두개

    // DatePicker 에서 날짜 선택 시 호출
    private DatePickerDialog.OnDateSetListener BDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) {
            // D-day 계산 결과 출력
            result = getDday(a_year, a_monthOfYear, a_dayOfMonth); // 디데이 계산 메인화면에 반영
            BPeriodEditText.setText(String.format(dayformat, a_year, a_monthOfYear+1, a_dayOfMonth)); //왜 날짜가 한달이 늦게 나오지?

        }
    };
    // DatePicker 에서 날짜 선택 시 호출
    private DatePickerDialog.OnDateSetListener ADateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) {
            APeriodEditText.setText(String.format(dayformat, a_year, a_monthOfYear+1, a_dayOfMonth));
        }
    };

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

        key = bundle.getString("key");
        period = bundle.getString("title");
        hospital = bundle.getString("content");

        // 한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // 현재 날짜를 알기 위해 사용
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
                if (DcheckBox.isChecked() == true) {
                    editor.putString("D_DAY", result);
                } else {
                    editor.putString("D_DAY", "날짜가 설정되지 않았습니다");
                }
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
                    renew();
                    return true;
                case R.id.action_save:
                    save();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    private void renew() {
        Do_Notification("수정되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("period", period);
        intent.putExtra("hospital", hospital);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void save() {
        ;
        Do_Notification("저장되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("period", APeriodEditText.getText().toString()+" ~ "+BPeriodEditText.getText().toString());
        intent.putExtra("hospital", mHospitalEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
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

    public void Do_Notification(String string) {
        //알람 설정
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_box_black_24dp)) //BitMap 이미지 요구
                .setContentTitle("가노간호 실습")
                .setContentText(string)
                // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //알림 바가 안뜨면 여기 수정해 보기
                .setAutoCancel(true);

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            //notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        } else
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴
    }
}

