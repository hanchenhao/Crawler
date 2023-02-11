package com.github.hanchenhao;

public interface CrawlerDAO {

    void insertContinuedLink(String title, String link);

    void insertCompletedLink(String title, String link);

    void insertArticleInformation(String url, String title, String source, String contents);

    String getNextContinuedLink();

    void deleteTempLinkFromContinuedTable(String link);

}
