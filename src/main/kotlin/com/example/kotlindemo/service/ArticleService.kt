package com.example.kotlindemo.service

import com.example.kotlindemo.model.Article
import com.example.kotlindemo.model.ReportDto
import com.example.kotlindemo.repository.ArticleRepository
import com.example.kotlindemo.repository.BatchRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.stream.Stream


@Service
class ArticleService(
    var transactionTemplate: TransactionTemplate,
    var articleRepository: ArticleRepository,
    var batchRepository: BatchRepository,
    private val objectMapper: ObjectMapper,
) {

    fun getResult(): StreamingResponseBody {
        return StreamingResponseBody { outputStream: OutputStream ->
            transactionTemplate.execute(object : TransactionCallbackWithoutResult() {
                override fun doInTransactionWithoutResult(status: TransactionStatus) {
                    fillStream(outputStream)
                }
            })
        }
    }

    private fun fillStream(outputStream: OutputStream) {
        try {
            println("실행 시간(ms): " + Date(System.currentTimeMillis()))
            val test = articleRepository.find1000Article(1)
            println("실행 시간(ms) 2: " + Date(System.currentTimeMillis()))
            test.forEach { article: Article -> processOrder(outputStream, article) }

            println("실행 시간(ms) 3: " + Date(System.currentTimeMillis()))
        } catch (e: IOException) {
            throw RuntimeException("Error processing stream", e)
        }
    }

    private fun processOrder(outputStream: OutputStream, article: Article) {
        try {
            val json: String = objectMapper.writeValueAsString(mapToArticle(article))
            outputStream.write((json + "\n").toByteArray(StandardCharsets.UTF_8))
        } catch (e: IOException) {
            throw RuntimeException("Error writing order to output stream", e)
        }
    }

    @Transactional(readOnly = true)
    fun getArticles(): List<ReportDto> {
        val articles = ArrayList<ReportDto>()
        val firstId = articleRepository.findArticleByFirstId().id
        var lastCheck = articleRepository.findById(firstId)
        var lastId: Long = firstId
        var size = 0

        while (!lastCheck.isEmpty) {
            val articleStream: Stream<Article> = articleRepository.find1000Article(lastId)
            articleStream.forEach{ article: Article -> articles.add(mapToArticle(article))}
            if(size < articles.size) {
                lastId = articles.last().id
                lastCheck = articleRepository.findById(lastId+1)
                size = articles.size
            }
        }
        return articles
    }
    private fun mapToArticle(article: Article): ReportDto {
        return ReportDto(
            article.id,
            article.title,
            article.content
        )
    }

    fun batchInsert(articles: List<Article>) {
        //10000000
        val maxSize = 5000000
        val loopSize = (articles.size / maxSize)
        for (i in 0 ..loopSize) {
            val savaData = mutableListOf<Article>()
            val end = if (loopSize > 0) (i+1) * maxSize else articles.size
            savaData.addAll(articles.subList( i * maxSize, end))
            println(savaData.size)
            batchRepository.batchInsert(savaData)
        }
    }

}