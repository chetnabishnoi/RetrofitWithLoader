package com.retrofit.loaders;

import android.content.Context;

import com.retrofit.domain.Item;

/**
 * Created by Cbishnoi on 05-08-2014.
 */
public class GetItemLoader extends AbstractLoader<Item> {
    public GetItemLoader(Context context) {
        super(context);
    }

    @Override
    protected Item doLoadInBackground() {
        return getService().getItems();
    }
}
