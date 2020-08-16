package homework.api.application.controller

import org.junit.jupiter.api.AfterAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT
                ,properties = ["spring.flyway.enabled=true"])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
open class ServiceTests {

    @Autowired
    lateinit var mockMvc: MockMvc
}