package com.softfinite.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SignUpRes {

    @SerializedName("result")
    @Expose
    public List<Result> result = new ArrayList<Result>();


    public class Result {

        @SerializedName("res")
        @Expose
        public String res;
        @SerializedName("accno")
        @Expose
        public int accno;

    }

}
