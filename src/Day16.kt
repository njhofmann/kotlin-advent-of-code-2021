import kotlin.math.pow

fun main() {

    fun hexToBinary(hex: Char) : String {
        return when (hex) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            else -> "1111"  // F
        }
    }

    fun binaryToInt(raw: String) : Int {
        return raw.reversed().mapIndexed { idx, it -> if (it == '0') 0 else 2.0.pow(idx).toInt() }.sum()
    }

    fun decodeLiteralVal(raw: String) : Int {
        var i = 0
        val binaryChars = StringBuilder()
        while (raw[i] != '0' && raw.length - i >= 5) {
            binaryChars.append(raw.substring(i + 1, i + 4))
            i += 5
        }
        return binaryToInt(binaryChars.toString())
    }

    fun decodeMsg(rawMsg: String) : Int {
        val binaryMsg = rawMsg.map { hexToBinary(it) }.joinToString { "" }
        val packetVersion = binaryToInt(binaryMsg.substring(0, 3))
        val typeId = binaryToInt(binaryMsg.substring(3, 6))
        var msg = binaryMsg.substring(6, binaryMsg.length)

        if (typeId == 4) {
            val decodedMsg = decodeLiteralVal(msg)
            return decodedMsg
        }

        val lenTypeId = msg[0]
        msg = msg.substring(1)
        if (lenTypeId == '0') {

        }
        else {

        }
    }

    fun part1(lines: List<String>): Int {
        val decodedMsg = decodeMsg(lines[0])
    }

//    fun part2(lines: List<String>): Int {
//
//    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 40)
    //check(part2(testInput) == 315)

    val input = readInput("Day16")
    println(part1(input))
    //println(part2(input))
}