package com.example.kotlindemo.service

import com.example.kotlindemo.model.Article
import com.example.kotlindemo.repository.ArticleRepository
import org.springframework.stereotype.Service


@Service
class ArticleService(
    var articleRepository: ArticleRepository
) {
    
    fun getArticles(id: Long): List<Article> {
        var articles = mutableListOf<Article>()
        val endId = articleRepository.findAll().last().id
        var lastId: Long = id

        while (endId != lastId) {
            val list: List<Article> = articleRepository.find100Article(lastId)
            if(list.isNotEmpty()) {
                lastId = list.last().id
                articles.addAll(list)
            }
        }
        return articles
    }

}