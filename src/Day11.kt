import java.lang.invoke.SwitchPoint
import java.util.*
import java.util.Collections.max
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.floor

fun main() {

    fun readOctopuses(lines: List<String>) : Array<IntArray> {
        return lines.map { line -> line.split("").filter{ it.isNotEmpty() }.map { it.toInt() }.toIntArray() }.toTypedArray()
    }

    fun Array<IntArray>.inc() : Array<IntArray> {
        return this.map { line -> line.map { it + 1 }.toIntArray() }.toTypedArray()
    }

    fun Array<IntArray>.getFlashLocations(flashed: Set<Pair<Int, Int>>) : List<Pair<Int, Int>> {
        return this.flatMapIndexed { x, line -> line.mapIndexed { y, i ->
            val curPosn = Pair(x, y)
            if (i > 9 && !flashed.contains(curPosn)) curPosn else Pair(-1, -1) }
        }.filter { it.first > -1 }
    }

    fun Array<IntArray>.flash(loc: Pair<Int, Int>, flashed: Set<Pair<Int, Int>>) : Array<IntArray> {
        this[loc.first][loc.second] = 0
        listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0), Pair(1, 1), Pair(1, -1), Pair(-1, 1), Pair(-1, -1)).forEach {
            val x = loc.first + it.first
            val y = loc.second + it.second
            if (0 <= x && x < this.size && 0 <= y && y < this[0].size && !flashed.contains(Pair(x, y))) {
                this[x][y] += 1
            }
        }
        return this
    }

    fun Array<IntArray>.step() : Pair<Array<IntArray>, Int> {
        var octopuses = this.inc()
        val flashed = HashSet<Pair<Int, Int>>()
        while (true) {
            val flashLocations = octopuses.getFlashLocations(flashed)

            if (flashLocations.isEmpty())
                break

            flashed.addAll(flashLocations)
            octopuses = flashLocations.fold(octopuses) { acc, loc -> acc.flash(loc, flashed) }
        }
        return Pair(octopuses, flashed.size)
    }

    fun Array<IntArray>.print() {
        this.forEach { line ->
            line.forEach { print(it) }
            println()
        }
        println()
    }

    fun Array<IntArray>.isSynchronized() : Boolean {
        val target = this[0][0]
        return this.map { line -> line.all { it == target } }.all { it }
    }

    fun part1(lines: List<String>): Int {
        val octopuses = readOctopuses(lines)
        octopuses.print()
        val flashedOctopuses = (0 until 100).fold(Pair(octopuses, 0)) { acc, _ ->
            val (newOctopuses, flashes) = acc.first.step()
            Pair(newOctopuses, acc.second + flashes)
        }

        flashedOctopuses.first.print()
        return flashedOctopuses.second
    }

    fun part2(lines: List<String>) : Int {
        var octopuses = readOctopuses(lines)
        octopuses.print()
        var i = 0
        while (!octopuses.isSynchronized()) {
            octopuses = octopuses.step().first
            i += 1

        }
        return i
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}