package com.github.hanchenhao;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    @SuppressFBWarnings({"DMI_CONSTANT_DB_PASSWORD", "ODR_OPEN_DATABASE_RESOURCE"})
    public static void main(String[] args) {
//        final String user = "root";
//        final String password = "123456";
//        String jdbcUrl = "jdbc:h2:file:/Users/hanchenhao/Desktop/Java学习/Crawler/target";
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);) {
//            NewsCrawler crawler = new NewsCrawler(connection);
//            for (int i = 1; i < 100; i++) {
//                crawler.insertAllNewsLinks("85_" + i);
//            }
//            crawler.run();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        String resource = "db.mybatis/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        MybatisDAO mybatis = new MybatisDAO(sqlSessionFactory);

        NewsCrawler crawler = new NewsCrawler(mybatis);
        crawler.insertAllNewsLinks("85_1");
    }
}
