package com.github.hanchenhao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.stream.Collectors;


public class NewsCrawler {
    //    private final JdbcDAO _DAO;
    public MybatisDAO _DAO;
//    public NewsCrawler(Connection _connection) {
//        this._DAO = new JdbcDAO(_connection);
//    }

    public NewsCrawler(MybatisDAO _DAO) {
        this._DAO = _DAO;
    }

    public void run() {
        String link = _DAO.getNextContinuedLink();
        while (link != null) {
            parseNewsContents(link);
            link = _DAO.getNextContinuedLink();
        }
    }

    public void insertAllNewsLinks(String page) {
        final String url = "https://www.dushu.com/news/" + page + ".html";
        try {
            Document doc = Jsoup.connect(url).get();
            doc.select("a").forEach(aTag -> {
                if (isAllowSaveNewsLink(aTag)) {
                    _DAO.insertContinuedLink(aTag.text(), ("https://www.dushu.com" + aTag.attr("href")));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void parseNewsContents(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.select("h1").text();
            String source = doc.select(".border-right").text();
            String contents = doc.select(".text p").stream().map(Element::text).collect(Collectors.joining("\n"));
            _DAO.insertArticleInformation(_DAO.insertCompletedLink(title, url), title, source, contents);
            _DAO.deleteTempLinkFromContinuedTable(url);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private boolean isAllowSaveNewsLink(Element link) {
        String linkHref = link.attr("href");
        String linkText = link.text();
        return (linkText.length() >= 5) && linkHref.contains("news") && !linkHref.contains("html");
    }


}

