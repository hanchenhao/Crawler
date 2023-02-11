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
        if (!isInContinued(link)) {
            System.out.println("title = " + title);
        }
    }

    @Override
    public String insertCompletedLink(String title, String link) {
        return null;
    }

    @Override
    public void insertArticleInformation(String url, String title, String source, String contents) {

    }

    @Override
    public String getNextContinuedLink() {
        return null;
    }

    @Override
    public void deleteTempLinkFromContinuedTable(String link) {

    }

    private boolean isInContinued(String url) {
//        System.out.println("isInContinued = " + sqlSessionFactory.openSession().selectOne(url));
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int count = session.selectOne("NewsMapper.isInContinued", url);
            return count > 0;
        }
    }
}
