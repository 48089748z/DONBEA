package com.oriolcunado.pro.donbea.DAOs;
/**
 * Created by 48089748z on 25/04/16.
 */
import com.oriolcunado.pro.donbea.POJOs.VideoYoutube;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class API
{
    private static String DEVELOPER = X.DEVELOPER;
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final JsonFactory JSON_FACTORY = new JacksonFactory();
    public final long MAX_ITEMS_RETURNED = 15;
    private YouTube youtube;
    public API()
    {
        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, httpRequest -> {}).setApplicationName("youtube-cmdline-search_48_white-sample").build();
    }

    public ArrayList<VideoYoutube> searchVideos (String query)
    {
        String ids = null;
        try {ids = getSearchId(query);} catch (IOException e) {e.printStackTrace();}
        ArrayList<VideoYoutube> youtubeVideos = new ArrayList<>();
        try
        {
            YouTube.Videos.List videos = youtube.videos().list("id,snippet,statistics,contentDetails");
            videos.setKey(DEVELOPER);
            videos.setId(ids);
            videos.setMaxResults(MAX_ITEMS_RETURNED);
            videos.setFields("items(id,snippet/publishedAt,snippet/title,snippet/description," +
                    "snippet/thumbnails/default/url,snippet/thumbnails/medium/url,snippet/thumbnails/high/url," +
                    "snippet/channelId,snippet/channelTitle,statistics/viewCount,statistics/likeCount,statistics/dislikeCount," +
                    "statistics/favoriteCount,statistics/commentCount,contentDetails/duration)");
            VideoListResponse videoResponse = videos.execute();
            List<Video> videoResultList = videoResponse.getItems();
            if (videoResultList != null)
            {
                Iterator<Video> iteratorSearchResults = videoResultList.iterator();

                if (!iteratorSearchResults.hasNext()) {return null;}
                else
                {
                    //Instance of the Arraylist to return and the object toAdd that is used to fill it

                    VideoYoutube toAdd;
                    while (iteratorSearchResults.hasNext())
                    {
                        Video singleVideo = iteratorSearchResults.next();
                        toAdd = new VideoYoutube();
                        toAdd.setId(singleVideo.getId());
                        toAdd.setPublishedAt(singleVideo.getSnippet().getPublishedAt().toString());
                        toAdd.setTitle(singleVideo.getSnippet().getTitle());
                        toAdd.setDescripcion(singleVideo.getSnippet().getDescription());
                        toAdd.setThumbnailDefault(((Thumbnail) singleVideo.getSnippet().getThumbnails().get("default")).getUrl());
                        toAdd.setThumbnailmedium(((Thumbnail) singleVideo.getSnippet().getThumbnails().get("medium")).getUrl());
                        toAdd.setThumbnailhigh(((Thumbnail) singleVideo.getSnippet().getThumbnails().get("high")).getUrl());
                        toAdd.setChannelId(singleVideo.getSnippet().getChannelId());
                        toAdd.setChannelTitle(singleVideo.getSnippet().getChannelTitle());
                        toAdd.setViewCount(singleVideo.getStatistics().getViewCount().toString());
                        toAdd.setDuration(singleVideo.getContentDetails().getDuration().toString());
                        try
                        {
                            toAdd.setLikeCount(singleVideo.getStatistics().getLikeCount().toString());
                            toAdd.setDislikeCount(singleVideo.getStatistics().getDislikeCount().toString());
                        }
                        catch (Exception e)
                        {
                            toAdd.setLikeCount("Private");
                            toAdd.setDislikeCount("Private");
                        }
                        youtubeVideos.add(toAdd);
                    }
                    return youtubeVideos;
                }
            }
        }
        catch (Throwable e) {e.printStackTrace();}
        return youtubeVideos;
    }

    public  ArrayList<VideoYoutube> getMusicChannelVideos()
    {
        List<SearchResult> searchResultList = null;
        try
        {
            String MUSIC_CHANNEL_ID = "UC-9-kyTW8ZkZNDHQJ6FgpwQ";
            YouTube.Search.List search = youtube.search().list("id");
            search.setKey(DEVELOPER);
            search.setChannelId(MUSIC_CHANNEL_ID);
            search.setType("video");
            search.setFields("items(id)");
            search.setMaxResults(MAX_ITEMS_RETURNED);
            SearchListResponse searchResponse = search.execute();
            searchResultList = searchResponse.getItems();
        }
        catch (IOException e){e.printStackTrace();}
        return searchVideos(getMusicChannelVideosIDs(searchResultList));
    }

    private String getMusicChannelVideosIDs(List<SearchResult> searchResultList)
    {
        if (searchResultList != null)
        {
            Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
            if (!iteratorSearchResults.hasNext()) {return null;}
            else
            {
                //String used to get all the ids found from the result comma separated
                String ids = "";

                //Selecting the type to get the id with different methods depending on what we got from result
                while (iteratorSearchResults.hasNext())
                {
                    SearchResult singleVideo = iteratorSearchResults.next();
                    ids = ids + singleVideo.getId().getVideoId() + ",";
                }
                ids = ids.substring(0, ids.length() - 1);
                return ids;
            }
        }
        return null;
    }

    public String getSearchId (String query) throws IOException
    {
        YouTube.Search.List search = youtube.search().list("id");
        search.setKey(DEVELOPER);
        search.setQ(query);
        search.setType("video");
        search.setFields("items(id)");
        search.setMaxResults(MAX_ITEMS_RETURNED);
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        if (searchResultList != null)
        {
            Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
            if (!iteratorSearchResults.hasNext()) {return null;}
            else
            {
                //String used to get all the ids found from the result comma separated
                String ids = "";

                while (iteratorSearchResults.hasNext())
                {
                    SearchResult singleVideo = iteratorSearchResults.next();
                    ids = ids+singleVideo.getId().getVideoId() + ",";
                }
                ids = ids.substring(0, ids.length() - 1);
                return ids;
            }
        }
        return null;
    }
}
