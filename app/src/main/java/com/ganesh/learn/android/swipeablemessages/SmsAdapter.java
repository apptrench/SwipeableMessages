package com.ganesh.learn.android.swipeablemessages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class SmsAdapter extends ArrayAdapter<SmsHolder> implements UndoAdapter {
    private static DateFormat dateFormatter = new SimpleDateFormat("dd MMM");
    private static DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private final Context context;

    public SmsAdapter(Context context, List<SmsHolder> data) {
        super(data);
        this.context = context;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SmsHolder sms = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_row, parent, false);
            holder = new ViewHolder();
            holder.address = (TextView) convertView.findViewById(R.id.from);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.address.setText(sms.getAddress());
        holder.message.setText(sms.getMessage());
        holder.date.setText(getDate(sms));

        return convertView;
    }

    private String getDate(SmsHolder sms) {
        final Date date = sms.getDate();
        if (isToday(date)) {
            return timeFormatter.format(date);
        }
        return dateFormatter.format(date);
    }

    private boolean isToday(Date date) {
        Calendar calendar = getInstance();
        calendar.setTime(date);

        Calendar today = getInstance();
        today.setTime(new Date());

        return isSameDay(calendar, today) && isSameMonth(calendar, today) &&
                isSameYear(calendar, today);

    }

    private boolean isSameYear(Calendar calendar, Calendar today) {
        return today.get(YEAR) == calendar.get(YEAR);
    }

    private boolean isSameMonth(Calendar calendar, Calendar today) {
        return today.get(MONTH) == calendar.get(MONTH);
    }

    private boolean isSameDay(Calendar calendar, Calendar today) {
        return today.get(DAY_OF_MONTH) == calendar.get(DAY_OF_MONTH);
    }

    @NonNull
    @Override
    public View getUndoView(int i, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = getUndoView(parent);
        }
        return convertView;
    }

    private View getUndoView(ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.undo_layout, parent, false);
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undoButton);
    }

    static class ViewHolder {
        TextView address;
        TextView date;
        TextView message;
    }
}
