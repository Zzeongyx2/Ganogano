package kr.ac.jbnu.se.mobile.ganogano;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {

    EditText memoEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoEdit = (EditText)findViewById(R.id.memo_edit);
    }
    public void onClick(View v){
        String memoData = memoEdit.getText().toString();
        memoEdit.setText("");

        Toast.makeText(this, "저장 완료", Toast.LENGTH_LONG).show();
    }
}
