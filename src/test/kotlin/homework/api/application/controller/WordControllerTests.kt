package homework.api.application.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class WordControllerTests : ServiceTests(){

    @Test
    @Throws(Exception::class)
    fun `test create new word successfully`() {
        this.mockMvc.perform(post("/api/words/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"word\":\"Test\","
                    + "\"wordCategory\":\"ADJECTIVE\"}")).andExpect(status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.word.id").exists())

        this.mockMvc.perform(get("/api/words/Test")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun `get all words`() {
        mockMvc.perform(get("/api/words/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo {print(it)}
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.words").exists())
                .andExpect(jsonPath("$.words[*].word").isNotEmpty)
    }

    @Test
    @Throws(Exception::class)
    fun `get word by text`() {
        mockMvc.perform(get("/api/words/Anna")
                .accept(MediaType.APPLICATION_JSON))
                .andDo{ print(it)}
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.word.id").exists())
    }

    @Test
    @Throws(Exception::class)
    fun `save failed and throw exception "The word must not be empty"`() {
        val result = this.mockMvc.perform(post("/api/words/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{"
                        + "\"wordCategory\":\"ADJECTIVE\"}")).andExpect(status().isBadRequest)
        val errorMesg = result.andReturn().response.errorMessage
        Assertions.assertEquals("The word must not be empty", errorMesg)
    }

}