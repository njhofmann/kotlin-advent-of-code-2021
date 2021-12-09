class Board(rawBoard: List<List<Int>>) {

    private var numSets: List<List<Pair<Int, Boolean>>>

    init {
        val rows: MutableList<List<Pair<Int, Boolean>>> = rawBoard.map { row -> row.map { Pair(it, false) } }.toMutableList()
        (0 until rawBoard[0].size).forEach { rows.add(rawBoard.map { row -> Pair(row[it], false) } ) }
        numSets = rows;
    }

    fun mark(num: Int) {
        numSets = numSets.map { numSet -> numSet.map { pair -> if (pair.first == num) Pair(pair.first, true) else pair } }
    }

    fun filled() : Boolean {
        return numSets.any { numSet -> numSet.all { pair -> pair.second } }
    }

    fun unmarkedSum() : Double {
        return numSets.map { numSet -> numSet.filter { !it.second }.map { it.first } }.flatten().sum() / 2.0
    }
}


fun main() {
    fun readNumsAndBoards(input: List<String>) : Pair<List<Int>, List<Board>> {
        val rawBoards = ArrayList<List<String>>()
        var curBoard: MutableList<String> = ArrayList();
        val nums = input[0].split(",").map { it.toInt() }
        input.subList(1, input.size).forEach {
            if (it.isEmpty()) {
                if (curBoard.isNotEmpty())
                    rawBoards.add(curBoard)
                curBoard = ArrayList()
            }
            else
                curBoard.add(it)
        }

        val boards = rawBoards.map { board -> board.map { row -> row.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } } }.map { Board(it) }
        return Pair(nums, boards)
    }

    fun part1(input: List<String>): Int {
        val (nums, boards) = readNumsAndBoards(input)
        nums.forEach { num ->
            boards.forEach { board ->
                board.mark(num)
                if (board.filled()) {
                    return (num * board.unmarkedSum()).toInt()
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val (nums, boards) = readNumsAndBoards(input)
        var curBoards = boards
        nums.forEach { num ->
            curBoards.forEach { it.mark(num) }

            if (curBoards.size == 1 && curBoards[0].filled()) {
                return (num * curBoards[0].unmarkedSum()).toInt()
            }

            curBoards = curBoards.filter { !it.filled() }

            if (curBoards.size == 1 && curBoards[0].filled()) {
                return (num * curBoards[0].unmarkedSum()).toInt()
            }
        }
        return 0
    }

    val testInput = readInput("Day04_test")
    println(part1(testInput))
//    check(part1(testInput) == 4512)
//    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}