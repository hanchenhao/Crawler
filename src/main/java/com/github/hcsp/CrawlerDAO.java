package com.github.hcsp;

public interface CrawlerDAO {

    void insertContinuedLink(String title, String link);

    String insertCompletedLink(String title, String link);

    void insertArticleInformation(String url, String title, String source, String contents);

    String getNextContinuedLink();

    void deleteTempLinkFromContinuedTable(String link);

}
