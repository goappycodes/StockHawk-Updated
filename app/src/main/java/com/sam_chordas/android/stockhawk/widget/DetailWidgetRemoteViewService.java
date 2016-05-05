package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDatabase;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.concurrent.ExecutionException;

/**
 * Created by Swati Agarwal on 04-05-2016.
 */
public class DetailWidgetRemoteViewService extends RemoteViewsService {

    public final String LOG_TAG = DetailWidgetRemoteViewService.class.getSimpleName();
    private static final String[] FORECAST_COLUMNS = {
            QuoteDatabase.QUOTES + "." + QuoteColumns._ID,
            QuoteColumns.SYMBOL,
            QuoteColumns.BIDPRICE,
            QuoteColumns.PERCENT_CHANGE,
            QuoteColumns.CHANGE,
            QuoteColumns.ISUP
    };

    // these indices must match the projection
    static final int INDEX_STOCK_ID = 0;
    static final int INDEX_SYMBOL= 1;
    static final int INDEX_BIDPRICE = 2;
    static final int INDEX_PERCENT_CHANGE = 3;
    static final int INDEX_CHANGE = 4;
    static final int INDEX_ISUP = 5;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                        FORECAST_COLUMNS,
                        QuoteColumns.ISCURRENT + " = ?",
                        new String[]{"1"},
                        null);

                Log.v("Widget-data",data+"");
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);
                int weatherId = data.getInt(INDEX_STOCK_ID);
                String symbol = data.getString(INDEX_SYMBOL);
                long bid_price = data.getLong(INDEX_BIDPRICE);
                double change = data.getDouble(INDEX_CHANGE);
                Log.v("widget_symbol",symbol);
                Log.v("widgt_bidprice",bid_price+"");
                Log.v("widget_change",String.valueOf(change));

                views.setTextViewText(R.id.widget_stock, symbol);
                views.setTextViewText(R.id.widget_bid_price, bid_price+"");
                views.setTextViewText(R.id.widget_change, String.valueOf(change));

                final Intent fillInIntent = new Intent();
                fillInIntent.setData(QuoteProvider.Quotes.withSymbol(symbol));
                views.setOnClickFillInIntent(R.id.widget_list, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getInt(INDEX_STOCK_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
