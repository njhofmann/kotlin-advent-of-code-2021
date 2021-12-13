import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.floor

fun main() {

    fun getIllegalCharVal(char: Char) : Int {
        return when (char) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }

    fun matchingBrackets(left: Char, right: Char) : Boolean {
        return (left == '(' && right == ')') ||
                (left == '[' && right == ']') ||
                (left == '{' && right == '}') ||
                (left == '<' && right == '>')
    }

    fun computeSyntaxErrorVal(line: String) : Int {
        val deque = ArrayDeque<Char>()
        line.forEach {
            if (setOf('[', '{', '<', '(').contains(it))
                deque.add(it)
            else if (matchingBrackets(deque.last(), it))
                deque.removeLast()
            else
                return getIllegalCharVal(it)
        }
        return 0
    }

    fun computeOpenPairs(line: String) : ArrayDeque<Char> {
        val deque = ArrayDeque<Char>()
        line.forEach {
            if (setOf('[', '{', '<', '(').contains(it))
                deque.add(it)
            else if (matchingBrackets(deque.last(), it))
                deque.removeLast()
            else
                return ArrayDeque()
        }
        return deque
    }

    fun scoreOpenPair(bracket: Char) : Int {
        return when (bracket) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            else -> 4 // <
        }
    }

    fun scoreOpenPairs(pairs: ArrayDeque<Char>) : Long {
        return pairs.foldRight(0.toLong()) { c, acc -> (acc * 5) + scoreOpenPair(c) }
    }

    fun part1(lines: List<String>): Int {
        return lines.sumOf { computeSyntaxErrorVal(it) }
    }

    fun part2(lines: List<String>): Long {
        val openPairScores = lines.map { computeOpenPairs(it) }.filter { it.isNotEmpty() }.map { scoreOpenPairs(it) }
        return openPairScores.sorted()[floor(openPairScores.size / 2.0).toInt()]
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957.toLong())

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}