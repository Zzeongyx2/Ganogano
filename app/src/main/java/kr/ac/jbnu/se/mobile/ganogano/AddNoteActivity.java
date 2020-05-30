package kr.ac.jbnu.se.mobile.ganogano;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

//TODO: 노트 추가
public class AddNoteActivity extends AppCompatActivity {
    //공유
    SharedPreferences sharedPref = null;
    SharedPreferences.Editor editor = null;

    // Millisecond 형태의 하루(24 시간)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private int REQUEST_TEST = 1;

    // 현재 날짜를 알기 위해 사용
    private Calendar mCalendar;
    private String result = null;
    private CheckBox DcheckBox;
    private TextView resultTxt;
    private Button saveButton;
    final String dayformat = "%d 년 %d 월 %d일"; //날자형식
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    // DatePicker 에서 날짜 선택 시 호출
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) {
            // D-day 계산 결과 출력
            result = getDday(a_year, a_monthOfYear, a_dayOfMonth);
            resultTxt.setText(String.format(dayformat, a_year, a_monthOfYear, a_dayOfMonth));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_resvision_activity);
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        DcheckBox = findViewById(R.id.date_checkbox);
        saveButton = findViewById(R.id.saveBtn);
        resultTxt = findViewById(R.id.D_DAY_BOX);
        // 한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // 현재 날짜를 알기 위해 사용
        mCalendar = new GregorianCalendar();

        // Input date click 시 date picker 호출
        resultTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddNoteActivity
                        .this, mDateSetListener, year, month, day);
                dialog.show();
            }
        });

        //체크박스 클릭시
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
        //세이브 버튼 클릭시 startActivity사용하기
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : SAVE시 DB 저장
                //TODO : activity 변경
                Do_Notification();
                //finish()
            }
        });
    }
    /**
     * D-day 반환
     */
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
    }

    public void Do_Notification() {
        //알람 설정
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_box_black_24dp)) //BitMap 이미지 요구
                .setContentTitle("간호가노 상태창")
                .setContentText("추가되었습니다")
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

