import kotlin.math.max


fun main() {
    fun part1(freshIdRanges: List<LongRange>, ids: List<Long>): Int = ids.count { id ->
        freshIdRanges.any { freshIdRange -> id in freshIdRange }
    }

    fun part2(freshIdRanges: List<LongRange>): Long {
        var freshIdCount = 0L
        var lastFreshId = -1L
        for (freshIdRange in freshIdRanges.sortedBy { it.first }) {
            if (lastFreshId >= freshIdRange.first) {
                val newLastFreshId = max(lastFreshId, freshIdRange.last)
                freshIdCount += newLastFreshId - lastFreshId
                lastFreshId = newLastFreshId
            } else {
                freshIdCount += freshIdRange.size
                lastFreshId = freshIdRange.last
            }
        }
        return freshIdCount
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
