package com.retrofit.loaders;

import android.content.Context;

import com.retrofit.domain.Item;

/**
 * Created by Cbishnoi on 05-08-2014.
 */
public class PostItemLoader extends AbstractLoader<Item> {
    private Item mItem;
    public PostItemLoader(Context context, Item item) {
        super(context);
        this.mItem=item;
    }

    @Override
    protected Item doLoadInBackground() {
        return getService().postItem(mItem);
    }
}
