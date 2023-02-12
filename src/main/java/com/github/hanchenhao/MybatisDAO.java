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
        if (isNotInTable("isInContinued", link)) {
            try (SqlSession session = sqlSessionFactory.openSession(true)) {
                session.insert("com.github.hanchenhao.MybatisDAO.insertContinuedLink", new Link(title, link));
            }
        }
    }

    @Override
    public void insertCompletedLink(String title, String link) {
        if (isNotInTable("isInCompleted", link)) {
            try (SqlSession session = sqlSessionFactory.openSession(true)) {
                session.insert("com.github.hanchenhao.MybatisDAO.insertCompletedLink", new Link(title, link));
            }
        }
    }

    @Override
    public void insertArticleInformation(String url, String title, String source, String contents) {
        if (isNotInTable("isInNews", url)) {
            try (SqlSession session = sqlSessionFactory.openSession(true)) {
                session.insert("com.github.hanchenhao.MybatisDAO.insertNews", new News(url, title, source, contents));
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

    private boolean isNotInTable(String sel, String url) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            String s = "com.github.hanchenhao.MybatisDAO." + sel;
            int count = session.selectOne(s, url);
            return count <= 0;
        }
    }
}
