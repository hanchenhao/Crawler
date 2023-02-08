package com.github.hcsp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


public class Main {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> contents = crawlerNewsLinks("85_2");
        for (String title : contents.keySet()) {
            System.out.println("title = " + title);
            System.out.println("url = " + contents.get(title));
        }
    }

    public static ConcurrentHashMap<String, String> crawlerNewsLinks(String page) {
        ConcurrentHashMap<String, String> linksMap = new ConcurrentHashMap<>();
        String url = "https://www.dushu.com/news/" + page + ".html";
        try {
            Document doc = Jsoup.connect(url).get();
            doc.select("a").forEach(aTag -> {
                if (isAllowSaveNewsLink(aTag, linksMap)) {
                    linksMap.put(aTag.text(), "https://www.dushu.com" + aTag.attr("href"));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return linksMap;
    }

    private static boolean isAllowSaveNewsLink(Element link, ConcurrentHashMap<String, String> linksMap) {
        String linkHref = link.attr("href");
        String linkText = link.text();
        return (linkText.length() >= 5) && (!linksMap.containsKey(linkText) && linkHref.contains("news") && !linkHref.contains("html"));
    }
}
