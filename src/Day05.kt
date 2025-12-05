import kotlin.math.max


fun main() {
    fun part1(freshIdRanges: List<LongRange>, ids: List<Long>): Int = ids.count { id ->
        freshIdRanges.any { id in it }
    }

    fun part2(freshIdRanges: List<LongRange>): Long {
        val sorted = freshIdRanges.sortedByDescending { it.first }.toMutableList()
        val merged = buildList {
            var merging = sorted.removeLast()
            while (sorted.isNotEmpty()) {
                val range = sorted.removeLast()
                if (merging.last >= range.first) {
                    merging = merging.first..max(merging.last, range.last)
                } else {
                    add(merging)
                    merging = range
                }
            }
            add(merging)
        }
        return merged.sumOf(LongRange::size)
    }


    fun parseInput(input: String): Pair<List<LongRange>, List<Long>> {
        val (freshIdRangesText, idsText) = input.split("\n\n")
        val freshIdRanges = freshIdRangesText.lines().map { it.toLongs("-").toPair().toRange() }
        val ids = idsText.toLongs(separator = "\n")
        return freshIdRanges to ids
    }

    val (freshIdRanges, ids) = parseInput(readInputText("day-05-input"))

    part1(freshIdRanges, ids).println()
    part2(freshIdRanges).println()
}
