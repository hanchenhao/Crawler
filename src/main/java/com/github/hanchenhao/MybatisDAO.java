package com.github.hanchenhao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MybatisDAO implements CrawlerDAO {
    public SqlSessionFactory sqlSessionFactory;

    public MybatisDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void insertContinuedLink(String title, String link) {
        if (!isInContinuedTable(link)) {
            System.out.println("插入缓存表： " + title);
            try (SqlSession session = sqlSessionFactory.openSession(true)) {
                session.insert("com.github.hanchenhao.MybatisDAO.insertContinuedLink", new Link(title, link));
            }
        }
    }

    @Override
    public void insertCompletedLink(String title, String link) {
        if (!isInCompletedTable(link)) {
            System.out.println("插入缓存表： " + title);
            try (SqlSession session = sqlSessionFactory.openSession(true)) {
                session.insert("com.github.hanchenhao.MybatisDAO.insertCompletedLink", new Link(title, link));
            }
        }
    }

    @Override
    public void insertArticleInformation(String url, String title, String source, String contents) {
        if (!isInNewsTable(url)) {
            try (SqlSession session = sqlSessionFactory.openSession(true)) {
                session.insert("com.github.hanchenhao.MybatisDAO.insertNews", new News(url, title, source, contents));
                System.out.println("插入明细表： " + title);
            }
        }
    }

    @Override
    public String getNextContinuedLink() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne("com.github.hanchenhao.MybatisDAO.getNextContinuedLink");
        }
    }

    @Override
    public void deleteTempLinkFromContinuedTable(String link) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            session.delete("com.github.hanchenhao.MybatisDAO.deleteContinueLink", link);
        }
    }

    private boolean isInContinuedTable(String url) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int count = session.selectOne("com.github.hanchenhao.MybatisDAO.isInContinued", url);
            return count > 0;
        }
    }

    private boolean isInCompletedTable(String url) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int count = session.selectOne("com.github.hanchenhao.MybatisDAO.isInCompleted", url);
            return count > 0;
        }
    }

    private boolean isInNewsTable(String url) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int count = session.selectOne("com.github.hanchenhao.MybatisDAO.isInNews", url);
            return count > 0;
        }
    }
}
