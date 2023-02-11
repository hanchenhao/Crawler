package com.github.hanchenhao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcDAO implements CrawlerDAO {
    public Connection _connection;

    public JdbcDAO(Connection _connection) {
        this._connection = _connection;
    }

    public void insertContinuedLink(String title, String link) {
        if ((!isSameLinkInDatabase("select * from CONTINUED_LINKS where LINK=?", link))) {
            System.out.println("插入缓存表： " + title);
            insertNewsLink("insert into CONTINUED_LINKS(title, link)values ( ?,? )", title, link);
        }
    }

    public String insertCompletedLink(String title, String link) {
        if ((!isSameLinkInDatabase("select * from completed_links where LINK=?", link))) {
            System.out.println("插入完成表： " + title);
            insertNewsLink("insert into completed_links(title, link)values ( ?,? )", title, link);
            return link;
        }
        return null;
    }

    public void insertNewsLink(String sql, String title, String link) {
        try (PreparedStatement statement = _connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, link);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void insertArticleInformation(String url, String title, String source, String contents) {
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
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public String getNextContinuedLink() {
        try (PreparedStatement statement = _connection.prepareStatement("select * from CONTINUED_LINKS limit 1")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("LINK");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteTempLinkFromContinuedTable(String link) {
        try (PreparedStatement statement = _connection.prepareStatement("delete from CONTINUED_LINKS where LINK=?")) {
            statement.setString(1, link);
            statement.executeUpdate();
            System.out.println("删除缓存表： " + link);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
