package com.voicebot.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.koushik.bot_voice.R;
import com.voicebot.ui.fragments.SampleFragment;

public class SampleActivity extends BaseSingleFragmentActivity {

    protected Fragment createFragment(){
            return new SampleFragment();
    }
}
