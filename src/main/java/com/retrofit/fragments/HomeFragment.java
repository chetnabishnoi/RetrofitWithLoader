package com.retrofit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.AbstractActivity;
import com.retrofit.R;
import com.retrofit.domain.Item;
import com.retrofit.loaders.AbstractLoader;
import com.retrofit.loaders.GetItemLoader;
import com.retrofit.loaders.PostItemLoader;

/**
 * Created by Cbishnoi on 25-07-2014.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView id, title;
    private EditText mEditText;
    private Button mButton;
    private AbstractActivity mActivity;
    private LoaderManager mLoaderManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.retrofit.R.layout.fragment_home, container, false);
        mActivity = (AbstractActivity) getActivity();
        id = (TextView) view.findViewById(R.id.titleId);
        title = (TextView) view.findViewById(R.id.title);
        mEditText = (EditText) view.findViewById(R.id.editText);
        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoaderManager = getActivity().getSupportLoaderManager();
        if (mLoaderManager.getLoader(R.id.loader_items) == null)
            mLoaderManager.initLoader(R.id.loader_items, null, new GetItemCallback(mActivity, true));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            Item item = new Item();
            item.setId(1);
            item.setTitle(mEditText.getText().toString().trim());
            if (mLoaderManager.getLoader(R.id.loader_post_item) == null) {
                mLoaderManager.initLoader(R.id.loader_post_item, null, new PostItemCallback(mActivity, item));
            }
        }
    }

    /*Callback class to handle the response of ItemLoader*/
    private class GetItemCallback extends AbstractLoader.AbstractLoaderCallbacks<Item> {
        public GetItemCallback(ActionBarActivity activity, boolean showProgressDialog) {
            super(activity, showProgressDialog);
        }

        @Override
        public void onResponse(AbstractLoader<Item> loader, Item response) {
            id.setText(String.valueOf(response.getId()));
            title.setText(response.getTitle());
        }

        @Override
        public Loader<Item> onCreateLoader(int i, Bundle bundle) {
            return new GetItemLoader(mActivity);
        }
    }

    /*Callback class to handle the response of PostItemLoader*/
    private class PostItemCallback extends AbstractLoader.AbstractLoaderCallbacks<Item> {
        private Item mItem;

        public PostItemCallback(ActionBarActivity activity, Item item) {
            super(activity, true);
            this.mItem = item;
        }

        @Override
        public void onResponse(AbstractLoader<Item> loader, Item response) {
            Toast.makeText(mActivity, response.getTitle(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public Loader<Item> onCreateLoader(int i, Bundle bundle) {
            return new PostItemLoader(mActivity, mItem);
        }
    }
}