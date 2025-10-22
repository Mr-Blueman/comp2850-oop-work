import java.io.File
import java.io.FileNotFoundException

const val WORD_LENGTH = 5
const val GREY = "\u001B[1;90m"
const val GREEN = "\u001B[32m"
const val ORANGE = "\u001B[1;33m"
const val RESET = "\u001b[0m"

fun isValid(word: String): Boolean {
    if (word.length != WORD_LENGTH) {
        return false
    }
    return word.all { it.isLetter() }
}

fun readWordList(filename: String): MutableList<String> = try {
    File(filename).useLines { it.toMutableList() }
} catch(e: FileNotFoundException) {
    println("ERROR: Failed to read word list file '$filename'. Reason: ${e.message}")
    mutableListOf()
}

fun pickRandomWord(words: MutableList<String> = mutableListOf("hello")): String {
    val word = words.random()
    words.removeAt(words.indexOf(word))
    return word
}

fun obtainGuess(attempt: Int): String {
    println("Attempt $attempt, guess a word: ")
    while (true) {
        val input = readln()
        if (isValid(input)) {
            return input.uppercase()
        } else {
            println("Invalid input")
        }
    }
}

fun evaluateGuess(guess: String, target: String): List<Int> = guess.mapIndexed { index, char ->
    when (char) {
        target[index] -> 2
        in target -> 1
        else -> 0
    }
}

fun displayGuess(guess: String, matches: List<Int>) {
    val revealedString = buildString {
        guess.toList().zip(matches).forEach { (char, flag) ->
            when (flag) {
                2 -> {
                    append("$GREEN$char $RESET")
                }
                1 -> {
                    append("$ORANGE$char $RESET")
                }
                else -> {
                    append("$GREY$char $RESET")
                }
            }
        }
    }
    println(revealedString)
}
