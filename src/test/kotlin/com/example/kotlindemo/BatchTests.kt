package com.example.kotlindemo

import com.example.kotlindemo.model.Article
import com.example.kotlindemo.repository.BatchRepository
import com.example.kotlindemo.service.ArticleService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(value = [SpringExtension::class])
@SpringBootTest
class BatchTests @Autowired constructor(
    val batchRepository: BatchRepository,
    val articleService: ArticleService
) {

    @Test
    fun `batch insert`() {
        val articles: MutableList<Article> = mutableListOf()
        val title = "titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle"
        val content = "testetsetslkdjflkadnfaksdjflskdftestetsetslkdjflkadnfaksdjflskdftestetsetslkdjflkadnfaksdjflskdf"
        for (i in 1..10000000) {
            articles.add(Article(title = title, content = content))
        }

        articleService.batchInsert(articles)

    }

    @Test
    fun `batch update`() {
        val articles: MutableList<Article> = mutableListOf()
        val content = "updateupdateupdateupdateupdateupdateupdate"
        for (i in 1..10) {
            articles.add(Article(id = i.toLong(), title = i.toString(), content = content))
        }
        println(articles)
        batchRepository.batchUpdate(articles)
    }

    @Test
    fun `batch delete`() {
        val ids: MutableList<Int> = mutableListOf()
        for (i in 1..10) {
            ids.add(i)
        }
        batchRepository.batchDelete(ids)
    }

}