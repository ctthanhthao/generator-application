package homework.api.application.service

import homework.api.application.controller.SentenceController
import homework.api.application.dao.SentenceDao
import homework.api.application.dao.WordDao
import homework.api.application.entities.Sentence
import homework.api.application.entities.SentenceWithCount
import homework.api.application.entities.Word
import homework.api.application.exception.handler.GeneralException
import homework.api.application.utils.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

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

    fun findById(id: Long) : Sentence? {
        val opt = sentenceDao.findById(id)
        return if (opt.isPresent){
            opt.get()
        }
        else {
            null
        }
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
        try {
            return sentenceDao.save(obj)
        }catch (ex : DataIntegrityViolationException)
        {
            throw GeneralException("The sentence already existed", ex, HttpStatus.BAD_REQUEST)
        }
    }

    fun generateSentencesToNVA(): Unit {
        generate(arrayOf(Category.NOUN,Category.VERB, Category.ADJECTIVE))
    }

    fun yodaTalk(sentenceId: Long) : String
    {
        return convertSentence(sentenceId, arrayOf(Category.ADJECTIVE.name, Category.NOUN.name, Category.VERB.name))
    }

    fun showDisplayCountOf(sentence : Sentence) : SentenceWithCount
    {
        val count = sentence.showDisplayCount!!
        sentence.showDisplayCount = null
        val sentenceWithCount = SentenceWithCount(count, sentence.removeLinks())
        val method = SentenceController::class.java.getMethod(SentenceController::showDisplayCountOfSentence.name, Long::class.java)
        val link = WebMvcLinkBuilder.linkTo(method, sentence.sentenceId).withSelfRel()
        return sentenceWithCount.add(link)
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

        val mapWord = mutableMapOf<Category,List<Word>>()
        rule.forEach { r ->
            val listOfWord = wordDao.findWordByWordCategory(r)
            if (listOfWord == null || listOfWord.isEmpty())
            {
                    throw GeneralException("The system hasn't had all necessary types of words yet i.e NOUN, VERB, ADJECTIVE", null, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            mapWord.put(r, listOfWord)
        }
        // Start to generate
        val indexes = arrayOf(0,0,0)
        var numOfCombines : Int = 1
        for (e in mapWord)
        {
            numOfCombines *= e.value.size
        }

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

            val text = sentenceArr.joinToString(" ")
            val existSentence = sentenceDao.findSentenceByText(text)
            if (existSentence != null && existSentence.sentenceId!! > 0L) {
                continue
            }
            sentenceDao.save(Sentence(text = text, byRule = rule.joinToString(" "){
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