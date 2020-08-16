package homework.api.application.service

import homework.api.application.dao.SentenceDao
import homework.api.application.dao.WordDao
import homework.api.application.entities.Sentence
import homework.api.application.entities.Word
import homework.api.application.exception.handler.GeneralException
import homework.api.application.utils.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.Exception
import kotlin.collections.HashSet

@Service("sentenceService")
class SentenceServiceImpl {
    @Autowired
    private lateinit var sentenceDao : SentenceDao

    @Autowired
    private lateinit var wordDao: WordDao

    fun findAll() : List<Sentence>
    {
        return sentenceDao.findAll().map { s -> s }
    }

    fun findByIdAndIncreaseView(id: Long): Sentence? {
        val optionalObj = this.sentenceDao.findById(id)
        if (optionalObj.isPresent)
        {
            var sentence = optionalObj.get()
            var count = sentence.showDisplayCount?.plus(1)
            sentence.showDisplayCount = count
            sentenceDao.save(sentence)
            return sentence
        }
        return null
    }

    fun save(obj: Sentence): Sentence {
        return sentenceDao.save(obj)
    }

    @Synchronized
    fun generateSentencesToNVA(): Unit {
        generate(arrayOf(Category.NOUN,Category.VERB, Category.ADJECTIVE))
    }

    fun yodaTalk(sentenceId: Long) : String
    {
        return convertSentence(sentenceId, arrayOf(Category.ADJECTIVE.name, Category.NOUN.name, Category.VERB.name))
    }

    private fun convertSentence(sentenceId : Long, toRule : Array<String>) : String
    {
        val optSentence = sentenceDao.findById(sentenceId)
        val sentence : Sentence
        if (optSentence.isPresent)
        {
            sentence = optSentence.get()
        }
        else
        {
            throw GeneralException("The sentence hasn't existed.", null, HttpStatus.NOT_FOUND)
        }
        val regex = """\W+""".toRegex()
        val words = sentence.text.split(regex).toTypedArray()
        val fromRule = sentence.byRule.split(regex).toTypedArray()
        val newSentence = arrayOf<String>("","","")
        for (j in toRule.indices)
        {
            newSentence[j] = words[fromRule.indexOf(toRule[j])]
        }

        return newSentence.joinToString(" ")
    }

    private fun generate(rule : Array<Category>)
    {
        if (rule.size < 3 || HashSet(listOf(*rule)).size < 3)
            throw GeneralException("The rule should include 3 types : NOUN, VERB and ADJECTIVE", null, HttpStatus.INTERNAL_SERVER_ERROR)

        var listOfNoun : List<Word> = emptyList()
        var listOFVerb : List<Word> = emptyList()
        var listOfAdj : List<Word> = emptyList()
        rule.forEach { r ->
            when (r) {
                Category.NOUN -> listOfNoun = wordDao.findWordByWordCategory(r)?.toList() ?: emptyList()
                Category.VERB -> listOFVerb = wordDao.findWordByWordCategory(r)?.toList() ?: emptyList()
                Category.ADJECTIVE -> listOfAdj = wordDao.findWordByWordCategory(r)?.toList() ?: emptyList()
            }
        }

        if (listOfNoun.isEmpty() || listOFVerb.isEmpty() || listOfAdj.isEmpty())
        {
            throw GeneralException("The system hasn't had all necessary types of words yet i.e NOUN, VERB, ADJECTIVE", null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
        // remove all existing sentences
        sentenceDao.deleteAll()

        // Start to generate
        val indexes = arrayOf(0,0,0)
        val numOfCombines = (listOfNoun.size) * (listOFVerb.size) * (listOfAdj.size)
        val mapWord = mapOf(Category.NOUN to listOfNoun, Category.VERB to listOFVerb, Category.ADJECTIVE to listOfAdj)
        val thresholds = arrayOf(mapWord.getValue(rule[1]).size * mapWord.getValue(rule[2]).size,
                mapWord.getValue(rule[2]).size,
                1)
        val categoryListInOrder = mutableListOf(mapWord.getValue(rule[0]),
                mapWord.getValue(rule[1]),
                mapWord.getValue(rule[2]))

        var sentenceArr : Array<String>
        for (i in 0 until numOfCombines)
        {
            sentenceArr = arrayOf(categoryListInOrder[0][indexes[0]].word,
                                     categoryListInOrder[1][indexes[1]].word,
                                     categoryListInOrder[2][indexes[2]].word)

            sentenceDao.save(Sentence(text = sentenceArr.joinToString(" "), byRule = rule.joinToString(" "){
                it -> "${it.name}"
            }))
            if ((i + 1) % thresholds[0] == 0)
            {
                indexes[0] = indexes[0] + 1
                indexes[1] = 0
                indexes[2] = 0
                continue
            }
            if ((i + 1) % thresholds[1] == 0)
            {
                indexes[1] = indexes[1] + 1
                indexes[2] = 0
                continue
            }
            if((i + 1) % thresholds[2] == 0)
            {
                indexes[2] = indexes[2] + 1
                continue
            }
        }
    }
}