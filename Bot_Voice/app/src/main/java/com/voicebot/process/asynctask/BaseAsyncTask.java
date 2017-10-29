package com.voicebot.process.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.voicebot.services.VoiceBotService;
import com.voicebot.utils.VoiceBotConst;

import java.util.HashMap;


public class BaseAsyncTask extends AsyncTask<Object, Integer, Message> {

    protected Context mContext;
    protected Handler mHandler;
    protected HashMap<String, String> mValues;
    protected Message msg = new Message();
    protected VoiceBotService.JsonRequestSender sender;
    protected VoiceBotService.JsonResponseReader reader;

    @Override
    protected Message doInBackground(Object[] params) {
        return null;
    }

    protected Message createMessage(){
        msg.arg1 = 1;
        return msg;
    }

    protected Message createMessage(int status){
        msg.arg1 = status;
        return msg;
    }

    protected Message createMessage(String title, String message){
        msg.arg1 = 0;
        Bundle bundle = new Bundle();
        bundle.putString(VoiceBotConst.ASYNCTASK_TITLE, title);
        bundle.putString(VoiceBotConst.ASYNCTASK_MESSAGE, message);
        msg.setData(bundle);
        return msg;
    }
}
