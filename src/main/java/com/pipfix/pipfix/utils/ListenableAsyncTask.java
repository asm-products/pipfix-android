package com.pipfix.pipfix.utils;

import android.os.AsyncTask;
import com.pipfix.pipfix.utils.AsyncTaskListener;

public abstract class ListenableAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{
 
  @Override
  protected final void onPostExecute(Result result) {
    notifyListenerOnPostExecute(result);
  }
 
  private AsyncTaskListener<Result> mListener;

  
  
  public void listenWith(AsyncTaskListener<Result> l){
    mListener = l;
  }
  
  private void notifyListenerOnPostExecute(Result result){
    if(mListener != null)
      mListener.onPostExecute(result);
  }
 
}