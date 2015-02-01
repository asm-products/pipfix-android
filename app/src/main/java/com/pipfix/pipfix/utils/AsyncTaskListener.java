package com.pipfix.pipfix.utils;

public interface AsyncTaskListener<Result>{
    public void onPostExecute(Result result);
}
