package com.example.kotlindemo

import com.example.kotlindemo.model.Article
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets


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

		val inputStream: InputStream = URL("http://localhost:8080/api/articles").openStream()

		val buffer = ByteArray(1024)
		var length: Int = 0

		while ((inputStream.read(buffer).also { length = it }) != -1) {
			println(String(buffer, 0, length, StandardCharsets.UTF_8))
		}


//		mockMvc.get("/api/articles")
//			.andExpect {
//				request {
//					asyncStarted()
//					asyncResult("body")
//				}
//				status { isOk() }
//				content {
//					RequestPredicates.contentType(MediaType.APPLICATION_JSON)
//				}
//			}
//			.andReturn()

	}


	@Test
	fun `create user`() {

		for (i in 1..1) run {
			val article = Article(title = i.toString() , content = "testtesttesttesttesttesttesttesttttesttesttest")

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
