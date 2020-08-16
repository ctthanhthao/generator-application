package homework.api.application.service

import homework.api.application.dao.SentenceDao
import homework.api.application.dao.WordDao
import homework.api.application.entities.Sentence
import homework.api.application.entities.Word
import homework.api.application.exception.handler.GeneralException
import homework.api.application.utils.Category
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.*
import java.util.*


class SentenceServiceImplUnitTests {

    @InjectMocks
    private lateinit var sentenceService : SentenceServiceImpl
    @Mock
    private lateinit var sentenceDao :  SentenceDao
    @Mock
    private lateinit var wordDao : WordDao

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @AfterEach
    fun tearDown(){
        Mockito.validateMockitoUsage()
    }

    @Test
    fun testFindAll() {
        // Given
        val sentence1 = Sentence(sentenceId = 1L, text = "Test1 run ok", byRule = "NOUN VERB ADJECTIVE")
        val sentence2 = Sentence(sentenceId = 2L, text = "Test2 run ok", byRule = "NOUN VERB ADJECTIVE")
        val sentences = listOf(sentence1, sentence2)
        Mockito.`when`(sentenceDao.findAll()).thenReturn(sentences)
        // When
        val returnedList = sentenceService.findAll()
        // Then
        Assertions.assertEquals(2, returnedList.size)
        Assertions.assertTrue(returnedList.contains(sentence1))
    }

    @Test
    fun testFindByIdAndIncreaseView() {
        // Given
        val sentenceOpt = Optional.of(Sentence(sentenceId = 1L, text = "Test run ok", byRule = "NOUN VERB ADJECTIVE"))
        Mockito.`when`(sentenceDao.findById(1L)).thenReturn(sentenceOpt)
        // When
        val sentence = sentenceService.findByIdAndIncreaseView(1L)
        // Then
        if (sentence != null) {
            Assertions.assertEquals("Test run ok", sentence.text)
        }
        else
            throw Exception("The text of sentence should be not null")

        Assertions.assertEquals("NOUN VERB ADJECTIVE", sentence.byRule)
        Assertions.assertEquals(1, sentence.showDisplayCount)
    }

    @Test
    fun testGenerateSentencesToNVASuccessful()
    {
        // Given
        val listNoun = listOf<Word>(Word(word = "N1"), Word(word = "N2"))
        val listVerb = listOf<Word>(Word(word = "V1"), Word(word = "V2"))
        val listAdj = listOf<Word>(Word(word = "Adj"))
        Mockito.`when`(wordDao.findWordByWordCategory(Category.NOUN)).thenReturn(listNoun)
        Mockito.`when`(wordDao.findWordByWordCategory(Category.VERB)).thenReturn(listVerb)
        Mockito.`when`(wordDao.findWordByWordCategory(Category.ADJECTIVE)).thenReturn(listAdj)
        // When
        sentenceService.generateSentencesToNVA()
        // Then
        Mockito.verify(sentenceDao, Mockito.times(1)).deleteAll()
        Mockito.verify(sentenceDao, Mockito.times(4)).save(ArgumentMatchers.any(Sentence::class.java))
    }

    @Test
    fun testGenerateSentencesToNVAFailedAndThrowException()
    {
        // Given
        val listNoun = listOf<Word>(Word(word = "N1"), Word(word = "N2"))
        val listVerb = listOf<Word>(Word(word = "V1"), Word(word = "V2"))
        val listAdj = listOf<Word>()

        Mockito.`when`(wordDao.findWordByWordCategory(Category.NOUN)).thenReturn(listNoun)
        Mockito.`when`(wordDao.findWordByWordCategory(Category.VERB)).thenReturn(listVerb)
        Mockito.`when`(wordDao.findWordByWordCategory(Category.ADJECTIVE)).thenReturn(listAdj)
        // When Then
        Assertions.assertThrows(GeneralException::class.java) {
            sentenceService.generateSentencesToNVA()
        }
    }

    @Test
    fun testYodaTalkSuccessful()
    {
        // Given
        val sentence = Sentence(1L, text = "Sentence is inverse", byRule = "NOUN VERB ADJECTIVE")
        Mockito.`when`(sentenceDao.findById(1L)).thenReturn(Optional.of(sentence))
        // When
        val result = sentenceService.yodaTalk(1L)
        // Then
        Assertions.assertTrue(result == "inverse Sentence is")
    }

    @Test
    fun testYodaTalkFailedAndThrowException()
    {
        // Given
        Mockito.`when`(sentenceDao.findById(1L)).thenReturn(Optional.empty())
        // When Then
        Assertions.assertThrows(GeneralException::class.java){
            sentenceService.yodaTalk(1L)
        }
    }
}