package com.example.kotlindemo.repository

import com.example.kotlindemo.model.Article
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.SQLException




@Repository
class BatchRepository {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun batchInsert(articles: List<Article>): IntArray {
        return jdbcTemplate.batchUpdate(
            "insert into article (title, content) values (?, ?)",
            object: BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setString(1, articles[i].title)
                    ps.setString(2, articles[i].content)
                }

                override fun getBatchSize() = articles.size
            })
    }

    fun batchUpdate(articles: List<Article>): IntArray {
        return jdbcTemplate.batchUpdate(
            "update article set title = ?, content = ? where id = ?",
            object : BatchPreparedStatementSetter {
                @Throws(SQLException::class)
                override fun setValues(ps: PreparedStatement, i: Int) {
                    val article: Article = articles[i]
                    ps.setString(1, article.title)
                    ps.setString(2, article.content)
                    ps.setLong(3, article.id)
                }

                override fun getBatchSize() = articles.size
            })
    }

    fun batchDelete(ids: List<Int>): IntArray {
        return jdbcTemplate.batchUpdate(
            "delete FROM article where id = ?",
            object : BatchPreparedStatementSetter {
                @Throws(SQLException::class)
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setLong(1, ids[i].toLong())
                }

                override fun getBatchSize() = ids.size
            })
    }
}