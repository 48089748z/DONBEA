package com.example.uge01006.converter;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
class ListViewAdapter extends ArrayAdapter<VideoYoutube>
{
    private ImageView IVimage;
    private TextView TVtitle;
    private TextView TVviews;
    private TextView TVlikes;
    private TextView TVdislikes;
    private TextView TVduration;
    private LinearLayout LLlistview;
    private TextView TVsplitbar7;

    private SharedPreferences settings;
    ListViewAdapter(Context context, int resource, List<VideoYoutube> objects) {super(context, resource, objects);}
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        VideoYoutube item = getItem(position);
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_layout, parent, false);
        }
        IVimage = convertView.findViewById(R.id.IVimage);
        TVtitle = convertView.findViewById(R.id.TVtitle);
        TVviews = convertView.findViewById(R.id.TVviews);
        TVlikes = convertView.findViewById(R.id.TVlikes);
        TVdislikes = convertView.findViewById(R.id.TVdislikes);
        TVduration = convertView.findViewById(R.id.TVduration);
        LLlistview = convertView.findViewById(R.id.LLlistview);
        TVsplitbar7 = convertView.findViewById(R.id.TVsplitbar7);
        checkTheme();

        Picasso.with(getContext()).load(item.getThumbnailmedium()).fit().into(IVimage);
        TVtitle.setText(item.getTitle());
        TVviews.setText(getKorM(item.getViewCount())+" views");
        TVlikes.setText(getKorM(item.getLikeCount())+" likes");
        TVdislikes.setText(getKorM(item.getDislikeCount())+" dislikes");
        TVduration.setText(translateDuration(item.getDuration()));
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
    public void checkTheme()
    {
        if (settings.getBoolean("dark", true)) {setDarkTheme();}
        else {setLightTheme();}
    }
    private void setDarkTheme()
    {
        LLlistview.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar7.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVtitle.setTextColor(getContext().getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVviews.setTextColor(getContext().getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVlikes.setTextColor(getContext().getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVdislikes.setTextColor(getContext().getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));


    }
    private void setLightTheme()
    {
        LLlistview.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar7.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVtitle.setTextColor(getContext().getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVviews.setTextColor(getContext().getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVlikes.setTextColor(getContext().getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVdislikes.setTextColor(getContext().getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
    }
}
