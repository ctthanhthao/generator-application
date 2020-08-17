package homework.api.application.dao

import homework.api.application.entities.Sentence
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SentenceDao : CrudRepository<Sentence, Long> {
    fun findSentenceByText(text : String) : Sentence?
}