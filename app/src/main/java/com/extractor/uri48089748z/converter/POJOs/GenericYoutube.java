package com.extractor.uri48089748z.converter.POJOs;
import java.io.Serializable;

public abstract class GenericYoutube implements Serializable
{
    private String id;
    private String publishedAt;
    private String title;
    private String descripcion;
    private String thumbnailDefault;
    private String thumbnailmedium;
    private String thumbnailhigh;

    /**
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getPublishedAt() {
        return publishedAt;
    }

    /**
     * @param publishedAt
     */
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return
     */
    public String getThumbnailDefault() {
        return thumbnailDefault;
    }

    /**
     * @param thumbnailDefault
     */
    public void setThumbnailDefault(String thumbnailDefault) {
        this.thumbnailDefault = thumbnailDefault;
    }

    /**
     * @return
     */
    public String getThumbnailmedium() {
        return thumbnailmedium;
    }

    /**
     * @param thumbnailmedium
     */
    public void setThumbnailmedium(String thumbnailmedium) {
        this.thumbnailmedium = thumbnailmedium;
    }

    /**
     * @return
     */
    public String getThumbnailhigh() {
        return thumbnailhigh;
    }

    /**
     * @param thumbnailhigh
     */
    public void setThumbnailhigh(String thumbnailhigh) {
        this.thumbnailhigh = thumbnailhigh;
    }
}
