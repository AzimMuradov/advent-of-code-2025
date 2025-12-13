data class DAG<T : Any>(
    val adjacencyMap: Map<T, List<T>>,
) {

    val topologicallySortedVertices: List<T> by lazy(::calculateTopologicallySortedVertices)

    private fun calculateTopologicallySortedVertices(): List<T> {
        val incomingCount = adjacencyMap
            .values.asSequence().flatten()
            .groupingBy { it }.eachCountTo(mutableMapOf())

        val q = ArrayDeque(elements = adjacencyMap.keys - incomingCount.keys)

        return buildList {
            while (q.isNotEmpty()) {
                val v = q.removeFirst()
                add(v)

                for (u in adjacencyMap[v] ?: continue) {
                    incomingCount.dec(u)
                    if (incomingCount[u] == 0) {
                        q.addLast(u)
                    }
                }
            }
        }
    }
}


fun <T : Any> DAG<T>.countPaths(start: T, end: T): Long {
    val pathsCount = mutableMapOf(start to 1L).withDefault { 0L }

    for (v in topologicallySortedVertices) {
        for (u in adjacencyMap[v] ?: continue) {
            pathsCount.plusTo(u, pathsCount.getValue(v))
        }
    }

    return pathsCount.getValue(end)
}

fun <T : Any> DAG<T>.countPaths(path: List<T>): Long = path.zipWithNext().map { (start, end) ->
    countPaths(start, end)
}.sorted().product()
