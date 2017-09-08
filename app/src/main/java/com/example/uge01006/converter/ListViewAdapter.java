package com.example.uge01006.converter;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.VideoView;
import java.util.List;
public class ListViewAdapter extends ArrayAdapter<Result>
{

    public ListViewAdapter(Context context, int resource, List<Result> objects)
    {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Result item = getItem(position);
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_layout, parent, false);
        }
        VideoView video = convertView.findViewById(R.id.VVvideo);

        /** Meter el video en el VideoView a partir de la información del item **/



        video.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=JsKIAO11q1Y&list=FLtYx3YJq_0mlhJO_rpE1MTQ"));
      //  video.setVideoURI(Uri.parse("rtsp://v4.cache3.c.youtube.com/CjYLENy73wIaLQlW_ji2apr6AxMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYOr_86Xm06e5UAw=/0/0/0/video.3gp"));
        video.setMediaController(new MediaController(getContext())); //sets MediaController in the video view
        // MediaController containing controls for a MediaPlayer
        video.requestFocus();//give focus to a specific view
        video.start();//starts the video

        return convertView;
    }
}
