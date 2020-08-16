package homework.api.application.controller

import homework.api.application.entities.Word
import homework.api.application.entities.WordResource
import homework.api.application.entities.WordResourceList
import homework.api.application.exception.handler.GeneralException
import homework.api.application.service.WordServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/words")
class WordController {
    @Autowired
    private lateinit var wordService: WordServiceImpl

    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll (): ResponseEntity<WordResourceList> {
        val wordList = wordService.findAll()
        val words : MutableList<WordResource> = mutableListOf()
        for(w in wordList)
        {
            words.add(WordResource.ofWordWithDefaultLink(w))
        }
        return ResponseEntity.ok(WordResourceList(words))
    }

    @GetMapping("/{word}",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getWordByText(@NonNull @PathVariable(value = "word") text: String): ResponseEntity<WordResource> {
        val w = wordService.findWordByText(text) ?: throw GeneralException("The word hasn't existed", null, HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(WordResource.ofWordWithDefaultLink(w))

    }

    @PostMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createWord(@NonNull @RequestBody word : Word) : ResponseEntity<Any>
    {
        val createdWord = wordService.save(word)
        return ResponseEntity.status(HttpStatus.CREATED).body(WordResource.ofWordWithDefaultLink(createdWord))

    }

    @PutMapping("/{word}",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateWord(@PathVariable(value = "word") text : String, @NonNull @RequestBody word : Word): ResponseEntity<Any> {
        val newWord = wordService.findWordByText(text)
        if (newWord != null) {
            return ResponseEntity.ok(WordResource.ofWordWithDefaultLink(wordService.update(newWord.wordId, word)))
        }
        throw GeneralException("The word hasn't existed", null, HttpStatus.NOT_FOUND)
    }

}