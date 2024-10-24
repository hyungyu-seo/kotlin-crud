package com.example.kotlindemo.repository

import com.example.kotlindemo.model.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {
    @Query(value = "SELECT id FROM article WHERE id > :id LIMIT 100", nativeQuery = true)
    fun find100Article(id: Long) : List<Article>
}