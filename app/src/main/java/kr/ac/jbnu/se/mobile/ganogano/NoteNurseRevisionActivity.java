package kr.ac.jbnu.se.mobile.ganogano;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class NoteNurseRevisionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_nurse_revision_activity);
    }
    //save 버튼 클릭 이벤트
    public void save(View o){
        //TODO : DB 수정 , snackbar 나중에 제거하기

        Snackbar.make(o, "저장되었습니다.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //finish()
    }

}
