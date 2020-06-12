package kr.ac.jbnu.se.mobile.ganogano;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class Settings extends Activity {
    SharedPreferences sharedPref = null;
    SharedPreferences.Editor editor = null;
    Switch effect, bgm;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("환경설정");
        bgm = findViewById(R.id.btn_bgm);
        effect = findViewById(R.id.btn_effect);

        //===============상태저장=================//
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //===============상태 반영================//
        if (sharedPref.getInt("effect", 1) == 0) effect.setChecked(false);
        bgm.setChecked(sharedPref.getBoolean("bgm", false));

        //==================배경음악==============//
        intent = new Intent(this, MusicService.class);
    }

    public void bgm(View o) {
        if (bgm.isChecked()) {
            startService(intent);
            editor.putBoolean("bgm", true);
        } else {
            stopService(intent);
            editor.putBoolean("bgm", false);
        }
        editor.commit();
    }

    public void effect(View o) {
        if (effect.isChecked()) {
            editor.putInt("effect", 1);
        } else {
            editor.putInt("effect", 0);
        }
        editor.commit();
    }
}