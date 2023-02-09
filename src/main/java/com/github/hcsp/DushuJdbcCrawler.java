package com.github.hcsp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;


public class DushuJdbcCrawler {
    public Connection _connection;

    public DushuJdbcCrawler(Connection _connection) {
        this._connection = _connection;
    }

    public void run() {
        String link = getNextLinkAndInsertIntoCompletedLinks("select * from CONTINUED_LINKS limit 1");
        while (link != null) {
            link = getNextLinkAndInsertIntoCompletedLinks("select * from CONTINUED_LINKS limit 1");
        }
    }

    public void insertAllOriginalNewsLinks(String page) {
        final String url = "https://www.dushu.com/news/" + page + ".html";
        try {
            Document doc = Jsoup.connect(url).get();
            doc.select("a").forEach(aTag -> {
                if (isAllowSaveNewsLink(aTag)) {
                    insertContinuedLink(aTag.text(), ("https://www.dushu.com" + aTag.attr("href")));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void insertContinuedLink(String title, String link) {
        if ((!isSameLinkInDatabase("select * from CONTINUED_LINKS where LINK=?", link))) {
            System.out.println("插入缓存表： " + title);
            insertNewsLink("insert into CONTINUED_LINKS(title, link)values ( ?,? )", title, link);
            parseNewsContents(link);
        }
    }

    private String insertCompletedLink(String title, String link) {
        if ((!isSameLinkInDatabase("select * from completed_links where LINK=?", link))) {
            System.out.println("插入完成表： " + title);
            insertNewsLink("insert into completed_links(title, link)values ( ?,? )", title, link);
            deleteTempLinkFromContinuedTable(link);
            return link;
        }
        return null;
    }

    private void insertNewsLink(String sql, String title, String link) {
        try (PreparedStatement statement = _connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, link);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void insertArticleIntoDatabase(String url, String title, String source, String contents) {
        if (!isSameLinkInDatabase("select * from new where LINK=?", url)) {
            try (PreparedStatement statement = _connection.prepareStatement(
                    "insert into new( title, link, source_media, content, UPDATED_AT, CREATED_AT) " +
                            "values ( ?, ?,?,?,now(),now())")) {
                statement.setString(1, title);
                statement.setString(2, url);
                statement.setString(3, source);
                statement.setString(4, contents);
                statement.executeUpdate();
                System.out.println("插入明细表： " + title);
                insertCompletedLink(title, url);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private String getNextLinkAndInsertIntoCompletedLinks(String sql) {
        try (PreparedStatement statement = _connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String link = resultSet.getString("LINK");
                String title = resultSet.getString("TITLE");
                return insertCompletedLink(title, link);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private void parseNewsContents(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.select("h1").text();
            String source = doc.select(".border-right").text();
            String contents = doc.select(".text p").stream().map(Element::text).collect(Collectors.joining("\n"));
            insertArticleIntoDatabase(insertCompletedLink(title, url), title, source, contents);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private void deleteTempLinkFromContinuedTable(String link) {
        try (PreparedStatement statement = _connection.prepareStatement("delete from CONTINUED_LINKS where LINK=?")) {
            statement.setString(1, link);
            statement.executeUpdate();
            System.out.println("删除缓存表： " + link);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private boolean isAllowSaveNewsLink(Element link) {
        String linkHref = link.attr("href");
        String linkText = link.text();
        return (linkText.length() >= 5) && linkHref.contains("news") && !linkHref.contains("html");
    }

    private boolean isSameLinkInDatabase(String sql, String link) {
        try (PreparedStatement statement = _connection.prepareStatement(sql)) {
            System.out.println("正在查询链接是否重复 link= " + link);
            statement.setString(1, link);
            return statement.executeQuery().next() || link == null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

