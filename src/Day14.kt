import java.util.Collections.max
import java.util.Collections.min

typealias Rule = Pair<Pair<Char, Char>, Char>

typealias Template = MutableMap<Pair<Char, Char>, Long>

fun main() {

    fun <T> MutableMap<T, Long>.inc(key: T, incVal: Long = 1.toLong()) {
        if (!this.containsKey(key))
            this[key] = 0.toLong()
        this[key] = this[key]!! + incVal
    }

    fun <T> MutableMap<T, Long>.dec(key: T, decVal: Long = 1.toLong()) {
        this[key] = this[key]!! - decVal
        if (this[key] == 0.toLong())
            this.remove(key)
    }

    fun step(template: Template, charCounts: Map<Char, Long>, rules: List<Rule>) : Pair<Template, Map<Char, Long>> {
        val temp = template.toMutableMap()
        val tempCounter = charCounts.toMutableMap()
        for (rule in rules) {
            val (key, value) = rule
            if (template.containsKey(rule.first)) {
                val count = template[key]
                temp.dec(key, count!!)
                temp.inc(Pair(key.first, value), count)
                temp.inc(Pair(value, key.second), count)
                tempCounter.inc(value, count)
            }
        }
        return Pair(temp, tempCounter)
    }

    fun readRawTemplate(raw: String) : Pair<Template, Map<Char, Long>> {
        val template = HashMap<Pair<Char, Char>, Long>()
        val counter = HashMap<Char, Long>()
        raw.forEachIndexed { idx, char ->
            counter.inc(char)
            if (idx > 0)
                template.inc(Pair(raw[idx - 1], char))
        }
        return Pair(template, counter)
    }

    fun readRawRules(rawRules: List<String>) : List<Rule> {
        return rawRules.map { Pair(Pair(it[0], it[1]), it[6]) }
    }

    fun readTemplateAndRules(lines: List<String>) : Triple<Template, Map<Char, Long>, List<Rule>> {
        val (template, counter) = readRawTemplate(lines[0])
        val rules = readRawRules(lines.subList(2, lines.size))
        return Triple(template, counter, rules)
    }

    fun maxMinDiff(lines: List<String>, steps: Int) : Long {
        val (template, counter, rules) = readTemplateAndRules(lines)
        val (_, finalCounter) = (0 until steps).fold(Pair(template, counter)) { acc, i ->
            step(acc.first, acc.second, rules)
        }
        val counts = finalCounter.values
        return max(counts) - min(counts)
    }

    fun part1(lines: List<String>): Long {
        return maxMinDiff(lines, 10)
    }

    fun part2(lines: List<String>) : Long {
        return maxMinDiff(lines, 40)
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588.toLong())
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}