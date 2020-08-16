package homework.api.application.dao

import homework.api.application.entities.Word
import homework.api.application.utils.Category
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WordDao : CrudRepository<Word, Long>{
    fun findWordByWord(word : String) : Word?
    fun findWordByWordCategory(wordCategory: Category) : List<Word>?
}
