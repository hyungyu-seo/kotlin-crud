package com.example.kotlindemo.repository

import com.example.kotlindemo.model.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.stream.Stream

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {
    fun findAllBy() : Stream<Article>
    @Query(value = "SELECT * FROM article WHERE id > :id LIMIT 0, 2000000", nativeQuery = true)
    fun find1000Article(id: Long) : Stream<Article>

    @Query(value = "SELECT * FROM article LIMIT 1", nativeQuery = true)
    fun findArticleByFirstId() : Article
}