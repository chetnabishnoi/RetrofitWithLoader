package com.retrofit.loaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.retrofit.service.RetrofitRestApi;
import com.retrofit.service.RetrofitService;

import retrofit.RetrofitError;
import retrofit.client.Response;


public abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {

    private RetrofitService mRetrofitService;
    private T mResult;
    private RetrofitError mError;

    AbstractLoader(Context context) {
        super(context);
        mRetrofitService = new RetrofitRestApi(context).getService();
    }

    protected abstract T doLoadInBackground();

    RetrofitService getService() {
        return mRetrofitService;
    }

    @Override
    protected void onStartLoading() {
        if (mResult == null) {
            forceLoad();
        } else {
            deliverResult(mResult);
        }
    }

    @Override
    public final T loadInBackground() {
        try {
            return mResult = doLoadInBackground();
        } catch (RetrofitError e) {
            mError = e;
            return null;
        }
    }

    @Override
    public void deliverResult(final T data) {
        mResult = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    RetrofitError getError() {
        return mError;
    }


    public abstract static class AbstractLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<T> {

        private final ActionBarActivity mActivity;
        /*Progress Dialog Instance*/
        private ProgressDialog mProgressDialog;

        public AbstractLoaderCallbacks(final ActionBarActivity activity, boolean showProgressDialog) {
            mActivity = activity;
            /*Creating Progress Dialog*/
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(mActivity);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setCancelable(false);

            }
            if (showProgressDialog)
                showProgressDialog();
        }

        /*Function To Display Progress Dialog */
        private void showProgressDialog() {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        /*Function To Hide Progress Dialog */
        private void hideProgressDialog() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        /*Function to handle the response from the apii*/
        @Override
        public void onLoadFinished(final Loader<T> loader, T response) {
            LoaderManager loaderManager = mActivity.getSupportLoaderManager();
            loaderManager.destroyLoader(loader.getId());
            hideProgressDialog();
            final AbstractLoader<T> abstractLoader = (AbstractLoader<T>) loader;
            RetrofitError error = abstractLoader.getError();
            if (error != null) {
                onError(abstractLoader, error);
            } else {
                onResponse(abstractLoader, response);
            }
        }

        @Override
        public void onLoaderReset(Loader<T> loader) {
        }

        public abstract void onResponse(final AbstractLoader<T> loader,
                                        T response);

        /*Common function to handle all types of error*/
        public boolean onError(final AbstractLoader<T> loader, RetrofitError error) {
            if (error.isNetworkError()) {
                Toast.makeText(mActivity, "Network Error", Toast.LENGTH_SHORT).show();
            } else {
                Response response = error.getResponse();
                Toast.makeText(mActivity, response.getReason(), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }
}
