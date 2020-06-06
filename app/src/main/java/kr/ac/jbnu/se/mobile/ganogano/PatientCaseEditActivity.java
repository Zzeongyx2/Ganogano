package kr.ac.jbnu.se.mobile.ganogano;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class PatientCaseEditActivity extends AppCompatActivity {
    private EditText mSicknessEditText;
    private EditText mPrescriptionEditText;
    private EditText mPrecautionEditText;
    private EditText mEtcEditText;

    private String key = null;
    private String sickness, prescription, precaution, etc;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_case_activity);


        mSicknessEditText = (EditText) findViewById(R.id.Sickness);
        mPrescriptionEditText = (EditText) findViewById(R.id.Prescription);
        mPrecautionEditText = (EditText) findViewById(R.id.Precautions);
        mEtcEditText = (EditText) findViewById(R.id.Other);

        Bundle bundle = getIntent().getExtras();

        key = bundle.getString("key");
        sickness = bundle.getString("sickness");
        prescription = bundle.getString("prescription");
        precaution = bundle.getString("precaution");
        etc = bundle.getString("etc");

        if (key != null) {
            mSicknessEditText.setText(sickness);
            mPrescriptionEditText.setText(prescription);
            mPrecautionEditText.setText(precaution);
            mEtcEditText.setText(etc);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_memoedit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(key == null) {
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
        }else{
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
    private void cancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

    private void renew() {
        Do_Notification("수정되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("key",key);
        intent.putExtra("sickness", mSicknessEditText.getText().toString());
        intent.putExtra("prescription", mPrescriptionEditText.getText().toString());
        intent.putExtra("precaution", mPrecautionEditText.getText().toString());
        intent.putExtra("etc", mEtcEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void save(){
        Do_Notification("저장되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("sickness", mSicknessEditText.getText().toString());
        intent.putExtra("prescription", mPrescriptionEditText.getText().toString());
        intent.putExtra("precaution", mPrecautionEditText.getText().toString());
        intent.putExtra("etc", mEtcEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void Do_Notification(String string) {
        //알람 설정
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_box_black_24dp)) //BitMap 이미지 요구
                .setContentTitle("가노간호 케이스")
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
