package homework.api.application.entities

import com.fasterxml.jackson.annotation.JsonProperty
import homework.api.application.controller.WordController
import homework.api.application.utils.Category
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import javax.persistence.*

@Entity
@Table(name = "word")
data class Word (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "word_id", nullable = false, updatable = false)
        @JsonProperty("id")
        val wordId : Long = 0,

        @Column(name = "word", nullable = false, unique = true, updatable = true)
        var word : String = "",

        @Column(name = "category", nullable = false, updatable = true)
        @Enumerated(EnumType.STRING)
        var wordCategory : Category = Category.NOUN
) : RepresentationModel<Word>(){}

data class WordResource private constructor(val word : Word)
{
        companion object{
                fun of(word: Word) : WordResource
                {
                        return WordResource(word)
                }

                fun ofWordWithDefaultLink(word: Word) : WordResource
                {
                        val method = WordController::class.java.getMethod(WordController::getWordByText.name, String::class.java)
                        val link = WebMvcLinkBuilder.linkTo(method, word.word).withSelfRel()
                        word.add(link)
                        return of(word)
                }
        }
}

data class WordResourceList(val words : MutableList<WordResource>)
