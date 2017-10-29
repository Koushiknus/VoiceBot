package com.voicebot.process.json;

import com.google.gson.annotations.Expose;

import org.apache.http.Header;

import java.util.HashMap;

/**
 * Created by koushik on 13/5/2017.
 */

public abstract class JsonRequestObject {
    @Expose
    protected Header header;

    abstract void buildData(HashMap<String, String> values);
}
