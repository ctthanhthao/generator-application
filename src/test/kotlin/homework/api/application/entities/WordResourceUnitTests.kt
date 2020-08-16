package homework.api.application.entities

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import homework.api.application.utils.Category
import net.javacrumbs.jsonunit.JsonAssert
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals
import net.javacrumbs.jsonunit.core.ConfigurationWhen.path
import net.javacrumbs.jsonunit.core.ConfigurationWhen.then
import net.javacrumbs.jsonunit.core.Option.IGNORING_VALUES
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class WordResourceUnitTests {
    private val mapper = jacksonObjectMapper()

    @AfterEach
    fun reset() {
        JsonAssert.resetOptions()
    }

    @Test
    fun `convert word obj to json`() {
        val word = Word (wordId = 1, word = "Anna", wordCategory = Category.NOUN)
        val serializedWord = mapper.writeValueAsString(word)
        assertThat(serializedWord, jsonEquals(wordJson));
    }

    @Test
    fun `wordId should be 1`()
    {
        val word = Word (wordId = 1, word = "Anna", wordCategory = Category.NOUN)
        val serializedWord = mapper.writeValueAsString(word)
        assertThat(serializedWord, jsonPartEquals("id", 1));
    }

    @Test
    fun `convert word resource to json`() {
        val word = Word (wordId = 5, word = "nice", wordCategory = Category.ADJECTIVE)
        val serializedWord = mapper.writeValueAsString(WordResource.ofWordWithDefaultLink(word))
        println(serializedWord)
        assertThat(serializedWord, jsonEquals<Any>(wordResourceJson).`when`(IGNORING_VALUES));
        assertThat(serializedWord, jsonEquals<Any>(wordResourceJson).`when`(path("word.links[0].href"), then(IGNORING_VALUES)))
    }

    @Test
    fun `convert word resource list to json`(){
        val word1 = Word (wordId = 1, word = "Anna", wordCategory = Category.NOUN)
        val word2 = Word (wordId = 2, word = "David", wordCategory = Category.NOUN)
        val word3 = Word (wordId = 3, word = "Bird", wordCategory = Category.NOUN)
        val word4 = Word (wordId = 4, word = "sing", wordCategory = Category.VERB)
        val wordList = WordResourceList(mutableListOf(WordResource.ofWordWithDefaultLink(word1),
                                                    WordResource.ofWordWithDefaultLink(word2),
                                                    WordResource.ofWordWithDefaultLink(word3),
                                                    WordResource.ofWordWithDefaultLink(word4)))

        val serializeWordList = mapper.writeValueAsString(wordList)
        println(serializeWordList)
        assertThat(serializeWordList, jsonEquals<Any>(wordResourceWithLinkJson).`when`(IGNORING_VALUES))
    }

    @Test
    fun `deserialized word from json`()
    {
        val word : Word = mapper.readValue(wordJson, Word::class.java)
        Assertions.assertEquals(1, word.wordId)
        Assertions.assertEquals("Anna", word.word)
        Assertions.assertEquals(Category.NOUN, word.wordCategory)
    }

    private val wordJson = """{
                "id": 1,
                "word": "Anna",
                "wordCategory": "NOUN",
                "links":[]
                }""".trimIndent()

    private val wordResourceJson = """{
        "word": {
                "id": 5,
                "word": "nice",
                "wordCategory": "ADJECTIVE",
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/words/Anna"
                    }
                ]
            }
                }""".trimIndent()

    private val wordResourceWithLinkJson = """{
    "words": [
        {
            "word": {
                "id":1,
                "word":"Anna",
                "wordCategory": "NOUN",
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/words/Anna"
                    }
                ]
            }
        },
        {
            "word": {
                "id": 2,
                "word": "David",
                "wordCategory": "NOUN",
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/words/David"
                    }
                ]
            }
        },
        {
            "word": {
                "id": 3,
                "word": "Bird",
                "wordCategory": "NOUN",
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/words/Bird"
                    }
                ]
            }
        },
        {
            "word": {
                "id": 4,
                "word": "sing",
                "wordCategory": "VERB",
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/words/Cat"
                    }
                ]
            }
        }
]}""".trimIndent()
}