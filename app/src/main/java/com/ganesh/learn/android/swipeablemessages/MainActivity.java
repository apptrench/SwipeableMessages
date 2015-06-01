package com.ganesh.learn.android.swipeablemessages;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.TimedUndoAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private ListView smsList;
    private List<SmsHolder> data;
    private SmsAdapter adapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new ArrayList<>();
        adapter = new SmsAdapter(this, data);
        setUpSmsList();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void setUpSmsList() {
        smsList = (ListView) findViewById(R.id.messages);

//        SimpleSwipeUndoAdapter undoAdapter = new SimpleSwipeUndoAdapter(adapter, this, new SmsDeleteCallback());
        TimedUndoAdapter undoAdapter = new TimedUndoAdapter(adapter,this,new SmsDeleteCallback());

        undoAdapter.setAbsListView(smsList);
        smsList.setAdapter(undoAdapter);

        TextView emptyTextView = new TextView(this);
        emptyTextView.setText(R.string.noMessages);
        smsList.setEmptyView(emptyTextView);

        new LoadAllSms().execute();
    }

    private class SmsDeleteCallback implements OnDismissCallback {

        @Override
        public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints) {
            for (int position : ints) {
               adapter.remove(position);
            }
        }
    }

    private class LoadAllSms extends AsyncTask<Void, Void, List<SmsHolder>> {

        @Override
        protected List<SmsHolder> doInBackground(Void... params) {
            final String inboxURI = "content://sms/inbox";
            Cursor smsCursor = getContentResolver().query(Uri.parse(inboxURI), null, null, null, null);

            List<SmsHolder> data = new ArrayList<>();
            while (smsCursor.moveToNext()) {
                final String address = smsCursor.getString(smsCursor.getColumnIndex("address"));
                final String body = smsCursor.getString(smsCursor.getColumnIndex("body"));
                final Date date = new Date(smsCursor.getLong(smsCursor.getColumnIndex("date")));
                data.add(new SmsHolder(address, body, date));
            }
            smsCursor.close();
            return data;
        }

        @Override
        protected void onPostExecute(List<SmsHolder> smsHolderList) {
            data.clear();
            data.addAll(smsHolderList);
            adapter.notifyDataSetChanged();

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
