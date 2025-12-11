fun main() {
    fun part1(sortedVertices: List<String>, adjList: Map<String, List<String>>): Long =
        countPaths(sortedVertices, adjList, path = listOf("you", "out"))

    fun part2(sortedVertices: List<String>, adjList: Map<String, List<String>>): Long {
        val paths = listOf(
            listOf("svr", "dac", "fft", "out"),
            listOf("svr", "fft", "dac", "out"),
        )
        return paths.sumOf { path -> countPaths(sortedVertices, adjList, path) }
    }


    val adjList = readInputLines("day-11-input").associate { line ->
        val (v, usString) = line.split(":")
        val us = usString.split(" ")
        v to us
    }
    val sortedVertices = run {
        val vertices = adjList.flatMapTo(mutableSetOf()) { (v, us) -> us + v }
        topologicalSort(vertices, adjList)
    }

    part1(sortedVertices, adjList).println()
    part2(sortedVertices, adjList).println()
}
