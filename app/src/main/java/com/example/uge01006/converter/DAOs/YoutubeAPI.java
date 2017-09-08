package com.example.uge01006.converter.DAOs;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
public class YoutubeAPI
{
    /**
     * Global instance properties filename.
     */
    private static final String PROPERTIES_FILENAME = "youtub.properties";
    /**
     * Global instance of the API_KEY.
     */
    private static String API_KEY;
    /**
     * Global instance of the HTTP transport.
     */
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    /**
     * Global instance of the JSON factory.
     */
    private final JsonFactory JSON_FACTORY = new JacksonFactory();
    /**
     * Global instance of the max number of videos we want returned (50 = upper limit per page).
     */
    private static final long RETURN_NUMBER = 20;
    /**
     * Global instance of Youtube object to make all API requests.
     */
    private YouTube youtube;
    /*
    When a new YoutubeAPI instance is created it automatically gets the property file and finds the api key
    then it init the youtube service
     */
    public YoutubeAPI()
    {
        //Create a new property
        Properties properties = new Properties();
        try
        {
            //reader for property from its remote path
            InputStream in = YoutubeAPI.class.getResourceAsStream("/DAOs/" + PROPERTIES_FILENAME);
            properties.load(in);
        }
        catch (IOException e)
        {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }
        //giving api_key its value from the property reader
        API_KEY = properties.getProperty("youtube.apikey");
        //Init the youtube service
        initYoutube();
    }
    /**
     * @param channelId the channel id got from the channel object. Also accepts multiple ids introducing it comma separated
     * @return
     * @throws IOException
     */
    public String getChannelVideosIds(String channelId) throws IOException
    {
        YouTube.Search.List search = youtube.search().list("id");
        //Define the search key
        search.setKey(API_KEY);
        search.setChannelId(channelId);
        //Give the type (video, playlist or channel)
        search.setType("video");
        //Giving the params we want to be returned comma separated(for example if we had choosed the snipped before, we could choose snipped fields like snipped/publishedAt)
        search.setFields("items(id)");
        //set the max return number
        search.setMaxResults(RETURN_NUMBER);
        //Execute the search query
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        //check if the result is null
        return retrieveIds(searchResultList);
    }

    public String getChannelPlaylistsIds(String channelId) throws IOException
    {
        YouTube.Search.List search = youtube.search().list("id");
        //Define the search key
        search.setKey(API_KEY);
        search.setChannelId(channelId);
        //Give the type (video, playlist or channel)
        search.setType("playlist");
        //Giving the params we want to be returned comma separated(for example if we had choosed the snipped before, we could choose snipped fields like snipped/publishedAt)
        search.setFields("items(id)");
        //set the max return number
        search.setMaxResults(RETURN_NUMBER);
        //Execute the search query
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        //check if the result is null
        return retrieveIds(searchResultList);
    }

    public String getChannelChannelsIds(String channelId) throws IOException
    {
        YouTube.Search.List search = youtube.search().list("id");
        //Define the search key
        search.setKey(API_KEY);
        search.setChannelId(channelId);
        //Give the type (video, playlist or channel)
        search.setType("channel");
        //Giving the params we want to be returned comma separated(for example if we had choosed the snipped before, we could choose snipped fields like snipped/publishedAt)
        search.setFields("items(id)");
        //set the max return number
        search.setMaxResults(RETURN_NUMBER);
        //Execute the search query
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        //check if the result is null
        return retrieveIds(searchResultList);
    }

    private String retrieveIds(List<SearchResult> searchResultList)
    {
        //check if the result is null
        if (searchResultList != null)
        {
            Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();

            if (!iteratorSearchResults.hasNext())
            {
                return null;
            }
            else {
                //String used to get all the ids found from the result comma separated
                String ids = "";

                //Selecting the type to get the id with different methods depending on what we got from result
                while (iteratorSearchResults.hasNext()) {
                    SearchResult singleVideo = iteratorSearchResults.next();
                    ids = ids + singleVideo.getId().getVideoId() + ",";
                }
                ids = ids.substring(0, ids.length() - 1);
                return ids;
            }
        }
        return null;
    }

    /**
     * @param queryTerm the search param query
     * @param type content type (video, playlist or channel)
     * @return the found videos/channels/playlists youtube unique id's
     * @throws IOException
     */
    public String getSearchId(String queryTerm, String type) throws IOException {
        //creating a new search list with the param we want to be returned (it can be id and snipped but we just want id in this case)
        YouTube.Search.List search = youtube.search().list("id");
        //Define the search key
        search.setKey(API_KEY);
        //Define de query
        search.setQ(queryTerm);
        //Give the type (video, playlist or channel)
        search.setType(type);
        //Giving the params we want to be returned comma separated(for example if we had choosed the snipped before, we could choose snipped fields like snipped/publishedAt)
        search.setFields("items(id)");
        //set the max return number
        search.setMaxResults(RETURN_NUMBER);
        //Execute the search query
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        //check if the result is null
        if (searchResultList != null)
        {
            Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();

            if (!iteratorSearchResults.hasNext())
            {
                return null;
            }
            else
            {
                //String used to get all the ids found from the result comma separated
                String ids = "";

                //Selecting the type to get the id with different methods depending on what we got from result
                switch (type)
                {
                    case "video":
                    {
                        while (iteratorSearchResults.hasNext())
                        {
                            SearchResult singleVideo = iteratorSearchResults.next();
                            ids = ids+singleVideo.getId().getVideoId() + ",";
                        }
                        ids = ids.substring(0, ids.length() - 1);
                        return ids;
                    }
                    case "channel":
                    {
                        while (iteratorSearchResults.hasNext())
                        {
                            SearchResult singleVideo = iteratorSearchResults.next();
                            ids = ids+singleVideo.getId().getChannelId() + ",";
                        }
                        ids = ids.substring(0, ids.length() - 1);
                        return ids;
                    }
                    case "playlist":
                    {
                        while (iteratorSearchResults.hasNext())
                        {
                            SearchResult singleVideo = iteratorSearchResults.next();
                            ids = ids+singleVideo.getId().getPlaylistId() + ",";
                        }
                        ids = ids.substring(0, ids.length() - 1);
                        return ids;
                    }
                }
            }
        }
        return null;
    }
    /**
     * @param ids the ids from the videos you want in a single String comma separated (see method getSearchId or getChannelVideosIds)
     * @return an ArrayList<VideoYoutube> that contains VideoYoutube objects got from the call
     * @throws IOException
     */
    public ArrayList<VideoYoutube> getVideos (String ids) throws IOException
    {
        ArrayList<VideoYoutube> youtubeVideos = new ArrayList<>();
        try
        {
            YouTube.Videos.List videos = youtube.videos().list("id,snippet,statistics");

            videos.setKey(API_KEY);
            //Setting all the video ids we want to get back
            videos.setId(ids);
            videos.setMaxResults(RETURN_NUMBER);
            //Setting all the fields we want to be called from the api. Its such an
            videos.setFields("items(id,snippet/publishedAt,snippet/title,snippet/description," +
                    "snippet/thumbnails/default/url,snippet/thumbnails/medium/url,snippet/thumbnails/high/url," +
                    "snippet/channelId,snippet/channelTitle,statistics/viewCount,statistics/likeCount,statistics/dislikeCount," +
                    "statistics/favoriteCount,statistics/commentCount)");
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
                        try
                        {
                            toAdd.setLikeCount(singleVideo.getStatistics().getLikeCount().toString());
                            toAdd.setDislikeCount(singleVideo.getStatistics().getDislikeCount().toString());
                        }
                        catch (Exception e){
                            toAdd.setLikeCount("Private");
                            toAdd.setDislikeCount("Private");
                        }
                        toAdd.setFavoriteCount(singleVideo.getStatistics().getFavoriteCount().toString());
                        toAdd.setCommentCount(singleVideo.getStatistics().getCommentCount().toString());

                        youtubeVideos.add(toAdd);
                    }
                    return youtubeVideos;
                }
            }
        }
        catch (Throwable e) {}
        return youtubeVideos;
    }

    /**
     * @param queryTerm
     * @return arraylist where first element is an arraylist of videos, second an arraylist of playlists and third an arraylist of channels
     * @throws IOException
     */
    public ArrayList<ArrayList> getAll (String queryTerm) throws IOException
    {
        ArrayList<ArrayList> allContent = new ArrayList<>();
        allContent.add(getVideos(queryTerm));

        /**allContent.add(getPlaylists(queryTerm));
        allContent.add(getChannels(queryTerm));*/
        return allContent;
    }
    /**
     * Initialize the youtube object
     */
    private void initYoutube()
    {
        /*The YouTube object is used to make all API requests. The last argument is required, but
       * because we don't need anything initialized when the HttpRequest is initialized, we override
       * the interface and provide a no-op function.*/
        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, httpRequest -> {}).setApplicationName("youtube-cmdline-search-sample").build();
    }

    public ArrayList<VideoYoutube> getYoutubePopularVideos() throws IOException
    {
        return getVideos(getChannelVideosIds("UC-9-kyTW8ZkZNDHQJ6FgpwQ"));
    }
}
