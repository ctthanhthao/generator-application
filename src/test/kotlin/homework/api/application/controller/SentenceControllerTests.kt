package homework.api.application.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import homework.api.application.entities.SentenceResourceList
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class SentenceControllerTests : ServiceTests(){

    companion object {
        var logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
    private val mapper = jacksonObjectMapper()

    @Test
    @Throws(Exception::class)
    fun `call generate then get all sentences then do yodaTalk on a given sentence by id`()
    {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/sentences/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/sentences/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo {print(it)}
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.sentences").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sentences[*].sentence").isNotEmpty)

        val content: String = result.andReturn().response.contentAsString
        val sentences = mapper.readValue<SentenceResourceList>(content, SentenceResourceList::class.java)
        val id = sentences.sentences[0].sentence.sentenceId

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sentences/{id}/yodaTalk", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo {print(it)}
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.sentence.text").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sentence.id").doesNotExist())

    }
}