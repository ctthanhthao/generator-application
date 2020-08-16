package homework.api.application.entities

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.javacrumbs.jsonunit.JsonAssert
import net.javacrumbs.jsonunit.JsonMatchers.*
import net.javacrumbs.jsonunit.core.ConfigurationWhen.path
import net.javacrumbs.jsonunit.core.ConfigurationWhen.then
import net.javacrumbs.jsonunit.core.internal.JsonUtils.jsonSource
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import net.javacrumbs.jsonunit.core.Option.IGNORING_VALUES
import org.junit.jupiter.api.Assertions


class SentenceResourceUnitTests {
    private val mapper = jacksonObjectMapper()

    @AfterEach
    fun reset() {
        JsonAssert.resetOptions()
    }

    @Test
    fun `convert sentence obj to json`() {
        val sentence = Sentence (sentenceId = 1, text = "Foo is amazing", showDisplayCount = 3)
        val serializedSentence = mapper.writeValueAsString(sentence)
        assertThat(serializedSentence, jsonEquals(sentenceJson));
    }

    @Test
    fun `convert sentence obj to json without generatedDate and byRule`()
    {
        val sentence = Sentence (sentenceId = 1, text = "Foo is amazing", showDisplayCount = 3)
        val serializedSentence = mapper.writeValueAsString(sentence)
        assertThat(jsonSource(serializedSentence, "$"), jsonNodeAbsent("generatedDate"))
        assertThat(jsonSource(serializedSentence, "$"), jsonNodeAbsent("byRule"))
    }

    @Test
    fun `show display count should be 3`()
    {
        val sentence = Sentence (sentenceId = 1, text = "Foo is amazing", showDisplayCount = 3)
        val serializedWord = mapper.writeValueAsString(sentence)
        assertThat(serializedWord, jsonPartEquals("showDisplayCount", 3));
    }

    @Test
    fun `convert sentence resource to json`() {
        val sentence = Sentence (sentenceId = 1, text = "Foo is amazing", showDisplayCount = 3)
        val serializedWord = mapper.writeValueAsString(SentenceResource.ofSentenceWithDefaultLink(sentence))
        assertThat(serializedWord, jsonEquals<Any>(sentenceResourceJson).`when`(IGNORING_VALUES))
        assertThat(serializedWord, jsonEquals<Any>(sentenceResourceJson).`when`(path("sentence.links[0].href"), then(IGNORING_VALUES)))
    }

    @Test
    fun `convert sentence resource list to json`(){
        val sentence1 = Sentence (sentenceId = 1, text = "Foo is amazing", showDisplayCount = 3)
        val sentence2 = Sentence (sentenceId = 2, text = "Movie is attractive", showDisplayCount = 2)
        val sentence3 = Sentence (sentenceId = 3, text = "David sing well", showDisplayCount = 0)
        val sentenceList = SentenceResourceList(mutableListOf(SentenceResource.ofSentenceWithDefaultLink(sentence1),
                SentenceResource.ofSentenceWithDefaultLink(sentence2),
                SentenceResource.ofSentenceWithDefaultLink(sentence3)))

        val serializeWordList = mapper.writeValueAsString(sentenceList)
        assertThat(serializeWordList, jsonEquals<Any>(sentenceResourceListJson).`when`(IGNORING_VALUES))
    }

    @Test
    fun `deserialized sentence from json`()
    {
        val sentence : Sentence = mapper.readValue(sentenceJson, Sentence::class.java)
        Assertions.assertEquals(1, sentence.sentenceId)
        Assertions.assertEquals("Foo is amazing", sentence.text)
        Assertions.assertEquals(3, sentence.showDisplayCount)
    }

    private val sentenceJson = """{
                "id": 1,
                "text": "Foo is amazing",
                "showDisplayCount": 3,
                "links": []
                }""".trimIndent()

    private val sentenceResourceJson = """{
        "sentence": {
                "id": 1,
                "text": "Foo is amazing",
                "showDisplayCount": 3,
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/sentences/1"
                    }
                ]
            }
        }""".trimIndent()

    private val sentenceResourceListJson = """{
    "sentences": [{
            "sentence": {
                "id": 1,
                "text": "Foo is amazing",
                "showDisplayCount": 3,
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/sentences/1"
                    }
                ]
            }
        },
        {
            "sentence": {
                "id": 2,
                "text": "Movie is attractive",
                "showDisplayCount": 2,
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/sentences/2"
                    }
                ]
            }
        },
        {
            "sentence": {
                "id": 3,
                "text": "David sing well",
                "showDisplayCount": 0,
                "links": [
                    {
                        "rel": "self",
                        "href": "http://localhost:8181/api/sentences/3"
                    }
                ]
            }
        }
        ]}""".trimIndent()
}