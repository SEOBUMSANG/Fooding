package com.example.fooding;

import com.android.volley.RequestQueue;

public class AppHelper {
    public static RequestQueue requestQueue;

    //부스트코스 예시
    public static String host = "boostcourse-appapi.connect.or.kr";
    public static String readMovieList = "/movie/readMovieList";
    public static String readMovie = "/movie/readMovie";
    public static String readCommentList = "/movie/readCommentList";
    public static String createComment = "/movie/createComment";

    public static int port = 10000;
}
