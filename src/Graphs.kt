fun <T : Any> topologicalSort(vertices: Set<T>, adjList: Map<T, List<T>>): List<T> {
    val incomingCount = vertices
        .associateWithTo(mutableMapOf()) { 0L }
        .apply {
            for (u in adjList.asSequence().flatMap { it.value }) {
                inc(u)
            }
        }

    return buildList {
        val q = ArrayDeque<T>().apply {
            for ((k, v) in incomingCount) {
                if (v == 0L) add(k)
            }
        }
        while (!q.isEmpty()) {
            val v = q.removeFirst()
            add(v)

            for (u in adjList[v] ?: continue) {
                incomingCount.dec(u)
                if (incomingCount[u] == 0L) {
                    q.addLast(u)
                }
            }
        }
    }
}

fun <T : Any> countPaths(
    sortedVertices: List<T>, adjList: Map<T, List<T>>,
    start: T, end: T
): Long {
    val pathsCount = mutableMapOf(start to 1L).withDefault { 0L }

    for (v in sortedVertices) {
        for (u in adjList[v] ?: continue) {
            pathsCount.plusTo(u, pathsCount.getValue(v))
        }
    }

    return pathsCount.getValue(end)
}

fun <T : Any> countPaths(
    sortedVertices: List<T>, adjList: Map<T, List<T>>,
    path: List<T>,
): Long = path.zipWithNext().map { (start, end) ->
    countPaths(sortedVertices, adjList, start, end)
}.sorted().product()
