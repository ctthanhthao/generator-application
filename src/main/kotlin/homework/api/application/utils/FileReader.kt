package homework.api.application.utils

class FileReader {
    fun readFileUsingGetResource(fileName: String) = this::class.java.getResource(fileName).readText(Charsets.UTF_8)
}