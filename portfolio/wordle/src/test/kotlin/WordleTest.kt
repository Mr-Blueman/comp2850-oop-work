import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

@Suppress("unused")
class WordleTest : StringSpec({

    // --- 1. isValid Function Block ---
    "isValid function: Length and Character Validation" should {

        "should return true for a valid 5-letter word" {
            withClue("Testing 'CRANE'") {
                isValid("CRANE").shouldBeTrue()
            }
            withClue("Testing 'AUDIO'") {
                isValid("AUDIO").shouldBeTrue()
            }
        }

        "should return false for words with incorrect length" {
            // Test Case 2: Length boundary - too short
            withClue("The word 'FOUR' is only 4 letters long") {
                isValid("FOUR").shouldBeFalse()
            }
            // Test Case 3: Length boundary - too long (Fix for the syntax error)
            withClue("The word 'SEVENLT' is 7 letters long") {
                isValid("SEVENLT").shouldBeFalse()
            }
        }
    }

    // ------------------------------------------------------------------------
    // Test Block for the 'readWordList' function
    // ------------------------------------------------------------------------
    "readWordList function: File Reading and Error Handling" should {

        "should successfully read a non-empty word list from data/words.txt" {
            withClue("Testing successful read from path data/words.txt") {
                readWordList("data/words.txt").shouldNotBeEmpty()
            }
        }

        "should return an empty list or fallback when FileNotFoundException occurs" {
            val nonExistentFilename = "missing_file.txt"
            withClue("Testing path '$nonExistentFilename' which does not exist") {
                readWordList(nonExistentFilename).shouldBeEmpty()
            }
        }
    }

    // ------------------------------------------------------------------------
    // Test Block for the 'pickRandomWord' function
    // ------------------------------------------------------------------------
    "pickRandomWord function: Selection and List Mutation" should {
        "should return a valid word and remove it from the list" {
            var words = readWordList("data/words.txt")
            val initialSize = words.size
            var word = pickRandomWord(words)

            withClue("returned word is valid") {
                isValid(word).shouldBeTrue()
            }
            withClue("returned word not in word list") {
                (word in words) shouldBe false
            }
            withClue("list size is reduced by one") {
                words.size shouldBe initialSize - 1
            }
        }
    }

    // ------------------------------------------------------------------------
    // Test Block for the 'evaluateGuess' function (Fix for the second syntax error)
    // ------------------------------------------------------------------------
    "evaluateGuess function: Full Wordle Scoring (2, 1, 0)" should {

        "should return all 2s for a perfect match (Green)" {
            val guess = "CRANE"
            val target = "CRANE"
            withClue("$guess vs $target") {
                // Expect all 2s (Green)
                evaluateGuess(guess, target) shouldBe listOf(2, 2, 2, 2, 2)
            }
        }

        "should return all 0s for a complete mismatch (Gray)" {
            val guess = "PLUTO"
            val target = "CRANE"
            withClue("$guess vs $target") {
                // Expect all 0s (Gray)
                evaluateGuess(guess, target) shouldBe listOf(0, 0, 0, 0, 0)
            }
        }

        "should score correct position as 2 and misplacement as 1 (Yellow)" {
            val guess = "STARE"
            val target = "AUDIO"
            // S: 0 (No)
            // T: 0 (No)
            // A: 1 (Exists, wrong spot)
            // R: 0 (No)
            // E: 0 (No)
            withClue("$guess vs $target") {
                evaluateGuess(guess, target) shouldBe listOf(0, 0, 1, 0, 0)
            }
        }

        "should correctly score a mix of 2s, 1s, and 0s" {
            val guess = "ALERT"
            val target = "CREAM"
            // A: 0 (No)
            // L: 0 (No)
            // E: 2 (Match in position)
            // R: 1 (Exists, wrong spot)
            // T: 0 (No)
            withClue("$guess vs $target") {
                evaluateGuess(guess, target) shouldBe listOf(1, 0, 2, 1, 0)
            }
        }
    }
})
