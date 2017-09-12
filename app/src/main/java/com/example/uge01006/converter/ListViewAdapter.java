package com.example.uge01006.converter;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.uge01006.converter.POJOs.VideoYoutube;
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

        VideoView video = convertView.findViewById(R.id.VVvideo);
        TextView title = convertView.findViewById(R.id.TVtitle);
        TextView length = convertView.findViewById(R.id.TVlength);
        //TextView views = convertView.findViewById(R.id.TVviews);
        //TextView likes = convertView.findViewById(R.id.TVlikes);


        title.setText(item.getTitle());
        length.setText(item.getViewCount());
        /** Meter el video en el VideoView a partir de la información del item
        video.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=JsKIAO11q1Y&list=FLtYx3YJq_0mlhJO_rpE1MTQ"));
      //  video.setVideoURI(Uri.parse("rtsp://v4.cache3.c.youtube.com/CjYLENy73wIaLQlW_ji2apr6AxMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYOr_86Xm06e5UAw=/0/0/0/video.3gp"));
        video.setMediaController(new MediaController(getContext())); //sets MediaController in the video view
        // MediaController containing controls for a MediaPlayer
        video.requestFocus();//give focus to a specific view
        video.start();//starts the video
         */
        return convertView;
    }

}
