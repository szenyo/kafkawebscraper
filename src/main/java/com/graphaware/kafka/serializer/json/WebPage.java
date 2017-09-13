package com.graphaware.kafka.serializer.json;

import java.util.List;

/**
 * Created by Janos Szendi-Varga on 2017. 09. 13.
 */
public class WebPage {

  private String sourceUrl;
  private String title;
  private String text;
  private List<String> tags;

  public WebPage(){

  }

  public WebPage(String sourceUrl, String title, String text, List<String> tags) {
    this.sourceUrl = sourceUrl;
    this.title = title;
    this.text = text;
    this.tags = tags;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    return "WebPage{" +
        "url=" + sourceUrl +
        ", title='" + title + "\'" +
        ", text='" + text + "\'" +
        ", tags='" + tags.toString() + "\'" +
        '}';
  }


}