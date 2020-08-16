package homework.api.application.service.integration

import homework.api.application.controller.ServiceTests
import homework.api.application.entities.Word
import homework.api.application.service.WordServiceImpl
import homework.api.application.utils.Category
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class WordServiceImplIntegrationTests : ServiceTests() {
    @Autowired
    private lateinit var wordService : WordServiceImpl

    @Test
    @Throws(Exception::class)
    fun testFindAll(){
        //Given
        val word = Word(word = "testFindAll", wordCategory = Category.VERB)
        // When
        val savedWord = wordService.save(word)
        // Then
        Assertions.assertTrue(savedWord.wordId != 0L)
        Assertions.assertEquals(word.word, savedWord.word)
        //Then
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateWordSuccessfully(){
        //Given
        var word = Word(word = "testFindAll", wordCategory = Category.VERB)
        word = wordService.save(word)
        word.wordCategory = Category.ADJECTIVE
        // When
        val updatedWord = wordService.update(word.wordId, word)
        // Then
        Assertions.assertTrue(updatedWord.wordCategory == Category.ADJECTIVE)
        Assertions.assertEquals(word.word, updatedWord.word)
    }

}