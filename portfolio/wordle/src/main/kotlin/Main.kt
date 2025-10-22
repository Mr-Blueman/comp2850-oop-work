const val MAX_GUESSES = 6

fun main() {
    var correctGuess = false
    var guesses = MAX_GUESSES // Standard Wordle limit is 6, set to 6
    val maxGuesses = MAX_GUESSES

    // --- 1. Initialization and Error Handling ---
    val wordList = readWordList("data/words.txt") // Check the filename: was "data/wordlist.txt"

    if (wordList.isEmpty()) {
        println("Cannot start game: No words available in the list.")
        return // Exit the main function
    }

    try {
        // Pick the word, and remove it from the list if desired
        val word = pickRandomWord(wordList)
        // For testing/debugging, uncomment the next line:
        println("DEBUG: Target Word is $word")

        // --- 2. Game Loop ---
        while((!correctGuess) && guesses > 0) {
            println("\nGuesses remaining: $guesses")

            // 10 - guesses logic simplified to (maxGuesses - guesses + 1) to get current guess number
            val guessNumber = maxGuesses - guesses + 1

            val guess = obtainGuess(guessNumber)

            val guessBitMap = evaluateGuess(guess, word)
            displayGuess(guess, guessBitMap)

            // Check for perfect match (sum of bitmap is equal to word length)
            if(guessBitMap.all { it == 2 }) {
                correctGuess = true
            }

            guesses--
        }

        // --- 3. Final Result ---
        println("\n--- GAME ENDED ---")
        if (correctGuess) {
            println("Congratulations! You guessed the word '$word' and win!")
        } else {
            println("Ran out of guesses. Game Over. The word was '$word'.")
        }
    } catch (e: IllegalStateException) {
        println("FATAL ERROR: ${e.message}")
    }
}

