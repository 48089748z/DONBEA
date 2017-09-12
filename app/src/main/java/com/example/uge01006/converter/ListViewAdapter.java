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

import java.util.List;
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
        String[] parts = code.split("M");
        Integer hoursN = 0;
        Integer minutesN = Integer.valueOf(parts[0].substring(2, parts[0].length()));
        String seconds = parts[1].substring(0, parts[1].length()-1);
        while (Integer.valueOf(minutesN)>=60)
        {
            minutesN=minutesN-60;
            hoursN++;
        }
        String minutes = String.valueOf(minutesN);
        String hours = String.valueOf(hoursN);
        if (seconds.length()==1){seconds="0"+seconds;}
        if (minutes.length()==1){minutes="0"+minutes;}
        if (hoursN==0){return minutes+":"+seconds+" ";}
        else {return hours+":"+minutes+":"+seconds+" ";}
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
