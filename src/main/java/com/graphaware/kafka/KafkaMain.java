package com.graphaware.kafka;

import com.graphaware.kafka.client.producer.WebscraperProducer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by Janos Szendi-Varga on 2017. 09. 14.
 */
public class KafkaMain {


  public static void main(String[] args) throws IOException {
    ArrayList<String> feedUrlList = new ArrayList<String>();
    String fileName = "feedurl.txt";

    Scanner s = new Scanner(new File(fileName));
    while (s.hasNext()) {
      feedUrlList.add(s.next());
    }
    s.close();

    WebscraperProducer producerThread = new WebscraperProducer("test", feedUrlList);

    producerThread.run();
    producerThread.close();

  }
}
