package homework.api.application.controller

import homework.api.application.entities.Sentence
import homework.api.application.entities.SentenceResource
import homework.api.application.entities.SentenceResourceList
import homework.api.application.service.SentenceServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/sentences")
class SentenceController {
    @Autowired
    private lateinit var sentenceService : SentenceServiceImpl

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll (): ResponseEntity<SentenceResourceList> {
        val sentenceList = sentenceService.findAll()
        val sentences : MutableList<SentenceResource> = mutableListOf()
        for(s in sentenceList)
        {
            sentences.add(SentenceResource.ofSentenceWithDefaultLink(s))
        }
        return ResponseEntity.ok(SentenceResourceList(sentences))
    }

    @PostMapping("/generate")
    fun generate(): ResponseEntity<Void> {
        sentenceService.generateSentencesToNVA()
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    @GetMapping("/{id}",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSentenceById(@PathVariable(value = "id") sentenceId: Long): ResponseEntity<SentenceResource> {
        val sentence = sentenceService.findByIdAndIncreaseView(sentenceId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SentenceResource.ofSentenceWithDefaultLink(sentence))

    }

    @GetMapping("/{id}/yodaTalk",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun convertToYodaTalk(@PathVariable(value = "id") sentenceId: Long): ResponseEntity<SentenceResource> {
        val convertedStr = sentenceService.yodaTalk(sentenceId)
        return ResponseEntity.ok(SentenceResource.ofSentenceInYodaTalk(convertedStr, Sentence(sentenceId = sentenceId, text = convertedStr),sentenceId))
    }



}