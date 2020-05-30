package kr.ac.jbnu.se.mobile.ganogano;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

//TODO: 수정페이지
//일단 D-Day 계산 넣음
// 여기서 수정도 이뤄져야 한다.
public class NoteRevisionActivity extends AppCompatActivity {
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

    // DatePicker 에서 날짜 선택 시 호출
    private OnDateSetListener mDateSetListener = new OnDateSetListener() {
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
        saveButton = findViewById(R.id.backBtn);
        resultTxt = findViewById(R.id.D_DAY_BOX);
        // 한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // 현재 날짜를 알기 위해 사용
        mCalendar = new GregorianCalendar();

        // Input date click 시 date picker 호출
        resultTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NoteRevisionActivity.this, mDateSetListener, year, month, day);
                dialog.show();
            }
        });

        //체크박스 클릭시
        DcheckBox.setOnClickListener(new OnClickListener() {
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
    //save 버튼 클릭 이벤트
    public void save(View o){
        //TODO : SAVE시 DB 저장 , 세이브 버튼 클릭시 startActivity사용하기, snackbar 나중에 제거하기
        //DB 수정
        Snackbar.make(o, "저장되었습니다.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //finish()
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
        //Todo: Shared
    }
}

