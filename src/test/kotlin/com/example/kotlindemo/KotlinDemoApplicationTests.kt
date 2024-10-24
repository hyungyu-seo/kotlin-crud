package com.example.kotlindemo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import com.example.kotlindemo.model.Article
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.servlet.function.RequestPredicates

@SpringBootTest
@AutoConfigureMockMvc
class KotlinDemoApplicationTests @Autowired constructor(
	val mockMvc: MockMvc,
	val objectMapper: ObjectMapper
) {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `get users`() {

		mockMvc.get("/api/articles")
//			.andDo { print() }
			.andExpect {
				status { isOk() }
				content {
					RequestPredicates.contentType(MediaType.APPLICATION_JSON)
				}
			}
	}


	@Test
	fun `create user`() {

		for (i in 1..1) run {
			val article = Article(title = i.toString() , content = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest")

			mockMvc.post("/api/articles") {
				contentType = MediaType.APPLICATION_JSON
				content = objectMapper.writeValueAsString(article)
			}
				.andDo { print() }
				.andExpect {
					status { isOk() }
					content {
						contentType(MediaType.APPLICATION_JSON)
					}
					jsonPath("$.title") { value(article.title) }
				}
		}

	}

}
