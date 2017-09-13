package com.graphaware.kafka.client.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphaware.kafka.serializer.json.WebPage;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;

/**
 * Created by Janos Szendi-Varga on 2017. 09. 13.
 */
public class WebscraperProducer implements Runnable {

  final static int TIMEOUT = 5000;
  public static final String USERAGENT = "Mozilla";

  private final String topic;

  private final org.apache.kafka.clients.producer.Producer producer;

  private ArrayList<String> feedUrlList;


  public WebscraperProducer(String topic, ArrayList<String> feedUrlList) {
    this.topic = topic;
    this.feedUrlList = feedUrlList;

    Properties configProperties = new Properties();
    configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.ByteArraySerializer");
    configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.connect.json.JsonSerializer");

    producer = new KafkaProducer(configProperties);


  }

  public void run() {

    int messageNo = 1;
    int i = 0;

    while (i < feedUrlList.size() && !Thread.interrupted()) {

      try {
        URL feedUrl = new URL(feedUrlList.get(i));

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl));

        for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {

          String url = entry.getLink();

          Document doc = Jsoup.connect(url).userAgent(USERAGENT).ignoreHttpErrors(true)
              .timeout(TIMEOUT).get();

          StringBuilder plainText = new StringBuilder();

          HtmlToPlainText formatter = new HtmlToPlainText();
          plainText.append(formatter.getPlainText(doc));

          ObjectMapper objectMapper = new ObjectMapper();

          WebPage webpage = new WebPage(url, entry.getTitle(), plainText.toString(), Collections.singletonList("news"));

          JsonNode jsonNode = objectMapper.valueToTree(webpage);
          ProducerRecord<String, JsonNode> rec = new ProducerRecord<String, JsonNode>(topic,
              jsonNode);
          producer.send(rec);
          messageNo++;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      i++;

    }

  }

  public void close() {
    producer.close();
  }
}