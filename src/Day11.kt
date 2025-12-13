fun main() {
    fun part1(dag: DAG<String>): Long = dag.countPaths(path = listOf("you", "out"))

    fun part2(dag: DAG<String>): Long {
        val paths = listOf(
            listOf("svr", "dac", "fft", "out"),
            listOf("svr", "fft", "dac", "out"),
        )
        return paths.sumOf(dag::countPaths)
    }


    val adjacencyMap = readInputLines("day-11-input").associate { line ->
        val (v, usString) = line.split(":")
        val us = usString.split(" ")
        v to us
    }
    val dag = DAG(adjacencyMap)

    part1(dag).println()
    part2(dag).println()
}
