import java.util.*


private fun Int.repeat(n: Int): Long = toString().repeat(n).toLong()

private fun Long.repeat(n: Int): Long = toString().repeat(n).toLong()

private fun generateInvalidIds(repeats: List<Int>): SortedSet<Long> {
    val idLengths = 1..10

    return TreeSet<Long>().apply {
        for (r in repeats) {
            val patternLengths = idLengths.filter { l -> l % r == 0 }.map { l -> l / r }
            for (pL in patternLengths) {
                val beg = ("1" + "0".repeat(pL - 1)).toInt()
                val end = ("9".repeat(pL)).toInt()
                val invalidIds = (beg..end).asSequence().map { num ->
                    num.repeat(r)
                }
                addAll(invalidIds)
            }
        }
    }
}


fun main() {
    fun common(idRanges: List<Pair<Long, Long>>, repeats: List<Int>): Long {
        val invalidIds = generateInvalidIds(repeats)
        return idRanges.sumOf { (beg, end) ->
            invalidIds.subSet(beg, end + 1).sum()
        }
    }

    fun part1(idRanges: List<Pair<Long, Long>>): Long = common(idRanges, repeats = listOf(2))

    fun part2(idRanges: List<Pair<Long, Long>>): Long = common(idRanges, repeats = (2..10).toList())


    val idRanges = readInputText("day-02-input")
        .split(",")
        .map { it.toLongs("-").toPair() }

    part1(idRanges).println()
    part2(idRanges).println()
}
