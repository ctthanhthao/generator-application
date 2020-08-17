package homework.api.application.service

import homework.api.application.dao.WordDao
import homework.api.application.entities.Word
import homework.api.application.exception.handler.GeneralException
import homework.api.application.utils.Category
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.*
import java.util.*

class WordServiceImplUnitTests {
    @InjectMocks
    lateinit var wordService : WordServiceImpl
    @Mock
    lateinit var wordDao : WordDao

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
    }
    @AfterEach
    fun tearDown(){
        Mockito.validateMockitoUsage()
    }

    @Test
    fun `find all words`() {
        // Given
        val word1 = Word(wordId = 1L, word = "Test1", wordCategory = Category.NOUN)
        val word2 = Word(wordId = 2L, word = "Test2", wordCategory = Category.NOUN)
        val word3 = Word(wordId = 3L, word = "run", wordCategory = Category.VERB)
        val word4 = Word(wordId = 4L, word = "fine", wordCategory = Category.ADJECTIVE)
        val words = listOf(word1, word2, word3, word4)
        Mockito.`when`(wordDao.findAll()).thenReturn(words)
        // When
        val returnedList = wordService.findAll()
        // Then
        Assertions.assertEquals(4, returnedList.size)
        Assertions.assertTrue(returnedList.contains(word1))
    }

    @Test
    fun `find word by word without exception`() {
        // Given
        val word = Word(wordId = 1L, word = "Test", wordCategory = Category.NOUN)
        Mockito.doReturn(word).`when`(wordDao).findWordByWord("Test")
        // When
        val result = wordService.findWordByText("Test")
        // Then
        if (result != null) {
            Assertions.assertEquals("Test", result.word)
        }
        else
            throw Exception("The word should be not null")

        Assertions.assertEquals(Category.NOUN, result.wordCategory)
    }

    @Test
    fun `save successfully without exception`()
    {
        // Given
        val word = Word(wordId = 1L, word = "Test", wordCategory = Category.NOUN)

        Mockito.`when`(wordDao.save(word)).thenReturn(word)
        // When
        val result = wordService.save(word)
        // Then
        Assertions.assertEquals("Test", result.word)
        Assertions.assertEquals(Category.NOUN, result.wordCategory)
    }

    @Test
    fun `save failed and throw exception "The word must not be empty"`()
    {
        // Given
        val word = Word(wordId = 1L, word = "", wordCategory = Category.NOUN)
        // When Then
        val message = Assertions.assertThrows(GeneralException::class.java){
            wordService.save(word)
        }.message
        Assertions.assertTrue(message == "The word must not be empty")
    }

    @Test
    fun `save failed and throw exception "The word already exist"`()
    {
        // Given
        val wordDB = Word(wordId = 1L, word = "Test", wordCategory = Category.NOUN)
        val newWord = Word(word = "Test", wordCategory = Category.NOUN)
        Mockito.`when`(wordDao.findWordByWord(newWord.word)).thenReturn(wordDB)
        // When
        // When Then
        val message = Assertions.assertThrows(GeneralException::class.java){
            wordService.save(newWord)
        }.message
        Assertions.assertTrue(message == "The word already existed")
    }

    @Test
    fun `save failed and throw exception "The word is forbidden"`()
    {
        // Given
        val word = Word(wordId = 1L, word = "forbidden-word", wordCategory = Category.NOUN)
        // When Then
        val message = Assertions.assertThrows(GeneralException::class.java){
            wordService.save(word)
        }.message
        Assertions.assertTrue(message == "The word is forbidden")
    }

    @Test
    fun `update word successfully`()
    {
        // Given
        val word = Word(wordId = 1L, word = "Test", wordCategory = Category.NOUN)
        val updateWord = Word(word = "Test", wordCategory = Category.ADJECTIVE)
        Mockito.`when`(wordDao.findById(word.wordId)).thenReturn(Optional.of(word))
        // When
        wordService.update(word.wordId, updateWord)
        // Then
        Mockito.verify(wordDao, Mockito.times(1)).save(ArgumentMatchers.any(Word::class.java))
    }

    @Test
    fun `update word failed and throw exception "The word hasn't existed"`()
    {
        // Given
        val word = Word(wordId = 1L, word = "Test", wordCategory = Category.NOUN)
        val updateWord = Word(word = "Test", wordCategory = Category.ADJECTIVE)
        Mockito.`when`(wordDao.findById(word.wordId)).thenReturn(Optional.of(word))
        // When
        wordService.update(word.wordId, updateWord)
        // Then
        Mockito.verify(wordDao, Mockito.times(1)).save(ArgumentMatchers.any(Word::class.java))
    }

}