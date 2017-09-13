package com.example.uge01006.converter;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
public class ListViewAdapter extends ArrayAdapter<VideoYoutube>
{
    public ListViewAdapter(Context context, int resource, List<VideoYoutube> objects)
    {super(context, resource, objects);}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        VideoYoutube item = getItem(position);
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_layout, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.IVimage);
        TextView title = convertView.findViewById(R.id.TVtitle);
        TextView views = convertView.findViewById(R.id.TVviews);
        TextView likes = convertView.findViewById(R.id.TVlikes);
        TextView dislikes = convertView.findViewById(R.id.TVdislikes);
        TextView duration = convertView.findViewById(R.id.TVduration);

        Picasso.with(getContext()).load(item.getThumbnailmedium()).fit().into(image);
        title.setText(item.getTitle());
        views.setText(getKorM(item.getViewCount())+" views");
        likes.setText(getKorM(item.getLikeCount())+" likes");
        dislikes.setText(getKorM(item.getDislikeCount())+" dislikes");
        duration.setText(translateDuration(item.getDuration()));
        return convertView;
    }

    private String translateDuration(String code)
    {
        DateFormat dateFormat;
        Calendar calendar = new GregorianCalendar();
        if (code.contains("H")) {dateFormat = new SimpleDateFormat("'PT'hh'H'mm'M'ss'S'");}
        else if (code.contains("M")){dateFormat = new SimpleDateFormat("'PT'mm'M'ss'S'");}
        else {dateFormat = new SimpleDateFormat("'PT'ss'S'");}
        try
        {
            Date date = dateFormat.parse(code);
            calendar.setTime(date);
        }
        catch (ParseException e) {e.printStackTrace();}

        String hours = String.valueOf(calendar.get(Calendar.HOUR));
        String minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        String seconds = String.valueOf(calendar.get(Calendar.SECOND));
        if (seconds.length() == 1) {seconds = "0"+seconds;}
        if (minutes.length() == 1) {minutes = "0"+minutes;}
        if (hours.length() == 1) {hours = "0"+hours;}

        if (hours.equals("00")){return " "+minutes+":"+seconds+" ";}
        else{return " "+hours+":"+minutes+":"+seconds+" ";}
    }

    private String getKorM (String number)
    {
        String result;
        if (number.length()==5) {result = number.substring(0,2)+"."+number.substring(2,3)+"K";}
        else if (number.length()==6) {result = number.substring(0,3)+"."+number.substring(3,4)+"K";}
        else if (number.length()==7) {result = number.substring(0,1)+"."+number.substring(2,4)+"M";}
        else if (number.length()==8) {result = number.substring(0,2)+"."+number.substring(2,3)+"M";}
        else if (number.length()>8) {result = number.substring(0,number.length()-6)+"M";}
        else {return number;}
        return result;
    }
}
