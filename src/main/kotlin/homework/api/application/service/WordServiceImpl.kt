package homework.api.application.service

import homework.api.application.dao.WordDao
import homework.api.application.entities.Word
import homework.api.application.exception.handler.GeneralException
import homework.api.application.utils.FileReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.properties.Delegates

@Service("wordService")
class WordServiceImpl {

    @Autowired
    private lateinit var wordDao: WordDao

    var forbiddenWords : Set<String> by Delegates.notNull()
    init {
        val fileReader = FileReader()
        forbiddenWords = fileReader.readFileUsingGetResource("/forbidden.word").split(",", ignoreCase = true).toSet()
    }

    fun findAll(): List<Word> {
        return wordDao.findAll().map { w -> w }
    }

    fun findWordByText(text : String): Word? {
        return wordDao.findWordByWord(text)
    }

    fun save(obj: Word): Word {
        if (obj.word.isBlank())
        {
            throw GeneralException("The word must not be empty", null, HttpStatus.BAD_REQUEST)
        }
        if (obj.wordId == 0L) {
            val existWord = wordDao.findWordByWord(obj.word)
            if (existWord != null) {
                throw GeneralException("The word already existed", null, HttpStatus.BAD_REQUEST)
            }
        }
        if (forbiddenWords.contains(obj.word))
        {
            throw GeneralException("The word is forbidden", null, HttpStatus.BAD_REQUEST)
        }
        return wordDao.save(obj)
    }

    fun update(wordId :Long, obj : Word) : Word {
        val existingWord = wordDao.findById(wordId)
        if (!existingWord.isPresent)
        {
            throw GeneralException("The word hasn't existed", null, HttpStatus.NOT_FOUND)
        }
        return existingWord.map { w ->
            val updateWord = w.copy(wordCategory = obj.wordCategory)
            wordDao.save(updateWord)
        }.orElse(Word())
    }
}