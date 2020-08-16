package homework.api.application.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import homework.api.application.controller.SentenceController
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import java.lang.reflect.Method
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "sentence")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Sentence (

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "sentence_id", nullable = false, updatable = false)
        @JsonProperty("id")
        var sentenceId : Long? = 0,

        @Column(name = "text", nullable = false, updatable = true)
        var text : String = "",

        @Column(name = "generated_date", nullable = false, updatable = true)
        @JsonIgnore
        var generatedDate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "show_display_count", nullable = false, updatable = true)
        var showDisplayCount : Long? = 0,

        @Column(name="by_rule", nullable = false, updatable = true)
        @JsonIgnore
        var byRule : String = ""
) : RepresentationModel<Sentence>(){}

data class SentenceResource private constructor(val sentence : Sentence)
{
        companion object{
                fun of(sentence: Sentence) : SentenceResource{
                        return SentenceResource(sentence)
                }

                fun ofSentenceWithDefaultLink(sentence: Sentence) : SentenceResource
                {
                        val method = SentenceController::class.java.getMethod(SentenceController::getSentenceById.name, Long::class.java)
                        val link = WebMvcLinkBuilder.linkTo(method, sentence.sentenceId).withSelfRel()
                        sentence.add(link)
                        return of(sentence)
                }

                fun ofSentenceWithCustomLink(sentence: Sentence, method : Method, vararg params : Any) : SentenceResource{
                        val link = WebMvcLinkBuilder.linkTo(method, params).withSelfRel()
                        sentence.add(link)
                        return of(sentence)
                }

                fun ofSentenceInYodaTalk(convertedStr : String, sentence: Sentence, vararg params : Any) : SentenceResource{
                        val method = SentenceController::class.java.getMethod(SentenceController::convertToYodaTalk.name, Long::class.java)
                        var yodaSentence = ofSentenceWithCustomLink(sentence, method, params).sentence
                        yodaSentence.text = convertedStr
                        yodaSentence.sentenceId = null
                        yodaSentence.showDisplayCount = null
                        return of(yodaSentence)
                }
        }
}

data class SentenceResourceList(val sentences : MutableList<SentenceResource>)
