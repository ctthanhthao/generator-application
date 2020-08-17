package homework.api.application.service.integration

import homework.api.application.controller.ServiceTests
import homework.api.application.entities.Sentence
import homework.api.application.exception.handler.GeneralException
import homework.api.application.service.SentenceServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class SentenceServiceImplIntegrationTests : ServiceTests() {

    @Autowired
    private lateinit var sentenceService : SentenceServiceImpl

    @Test
    @Throws(Exception::class)
    fun testSaveSentenceSuccessfully()
    {
        // Given
        val sentence = Sentence(text = "Test run ok", byRule = "NOUN VERB ADJECTIVE")
        // When
        val savedSentence = sentenceService.save(sentence)
        // Then
        Assertions.assertTrue(savedSentence.text === sentence.text)
        Assertions.assertTrue(savedSentence.sentenceId !== null)
    }

    @Test
    @Throws(Exception::class)
    fun testSaveDuplicatedSentenceThenFailed()
    {
        // Given
        val sentence = Sentence(text = "Test run ok", byRule = "NOUN VERB ADJECTIVE")
        sentenceService.save(sentence)
        // When then
        val message = Assertions.assertThrows(GeneralException::class.java){
            sentenceService.save(sentence)
        }.message
        Assertions.assertTrue(message == "The sentence already existed")
    }

    @Test
    @Throws(Exception::class)
    fun testGenerateSentences(){
        // Given
        var sentences = sentenceService.findAll()
        Assertions.assertTrue(sentences.isEmpty())
        // When
        sentenceService.generateSentencesToNVA()
        // Then
        sentences = sentenceService.findAll()
        Assertions.assertTrue(sentences.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testYodaTalk(){
        // Given
        var sentence = Sentence(text = "Test run ok", byRule = "NOUN VERB ADJECTIVE")
        sentence = sentenceService.save(sentence)
        // When
        val convertedString = sentenceService.yodaTalk(sentence.sentenceId!!)
        // Then
        Assertions.assertEquals("ok Test run", convertedString)
    }
}