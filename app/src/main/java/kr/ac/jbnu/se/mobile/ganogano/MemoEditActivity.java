package kr.ac.jbnu.se.mobile.ganogano;


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
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MemoEditActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mContentEditText;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference database;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    private String key = null;
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memoedit);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();

        mTitleEditText = (EditText) findViewById(R.id.title_edit);
        mContentEditText = (EditText) findViewById(R.id.content_edit);

        Bundle bundle = getIntent().getExtras();

        key = bundle.getString("key");
        title = bundle.getString("title");
        content = bundle.getString("content");

        if (key != null) {

            mTitleEditText.setText(title);
            mContentEditText.setText(content);
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

    private void renew() {
        Do_Notification("수정되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("key",key);
        intent.putExtra("title", mTitleEditText.getText().toString());
        intent.putExtra("content", mContentEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }


    private void cancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

    private void save(){ ;
        Do_Notification("저장되었습니다.");
        Intent intent = new Intent();
        intent.putExtra("title", mTitleEditText.getText().toString());
        intent.putExtra("content", mContentEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
    public void Do_Notification(String string) {
        //알람 설정
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_box_black_24dp))
                .setContentTitle("가노간호 메모")
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
