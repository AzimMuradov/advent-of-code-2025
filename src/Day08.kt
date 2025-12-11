import java.util.*


fun main() {
    fun dist(a: Triple<Long, Long, Long>, b: Triple<Long, Long, Long>): Long =
        (a.toList() zip b.toList()).sumOf { (a, b) -> (a - b) * (a - b) }

    fun getOrderedConnections(junctionBoxes: List<Triple<Long, Long, Long>>): Iterable<Pair<Int, Int>> =
        TreeMap<Long, Pair<Int, Int>>().apply {
            junctionBoxes.withIndex().toList().iterateOrderedCombinations { a, b ->
                this[dist(a.value, b.value)] = a.index to b.index
            }
        }.values

    fun part1(junctionBoxes: List<Triple<Long, Long, Long>>, orderedConnections: Iterable<Pair<Int, Int>>): Long {
        val ds = DisjointSets(junctionBoxes.size)

        for ((a, b) in orderedConnections.take(1000)) {
            ds.union(a, b)
        }
        return ds.setIds().groupingBy { it }.eachCount().values.sortedDescending().take(3).product().toLong()
    }

    fun part2(junctionBoxes: List<Triple<Long, Long, Long>>, orderedConnections: Iterable<Pair<Int, Int>>): Long {
        val ds = DisjointSets(junctionBoxes.size)

        for ((a, b) in orderedConnections) {
            ds.union(a, b)
            if (ds.setIds().toSet().size == 1) {
                return junctionBoxes[a].first * junctionBoxes[b].first
            }
        }
        error("unreachable")
    }


    val junctionBoxes = readInputLines("day-08-input").map { line ->
        line.toLongs(",").toTriple()
    }
    val orderedConnections = getOrderedConnections(junctionBoxes)

    part1(junctionBoxes, orderedConnections).println()
    part2(junctionBoxes, orderedConnections).println()
}
