fun main() {
    data class Problem(val numLines: List<String>, val op: String) {
        fun eval(): Long {
            val nums = numLines.map { it.trim().toLong() }
            return if (op == "+") nums.sum() else nums.product()
        }
    }

    fun part1(problems: List<Problem>): Long = problems
        .sumOf { it.eval() }

    fun part2(problems: List<Problem>): Long = problems
        .map { it.copy(numLines = it.numLines.transpose()) }
        .sumOf { it.eval() }


    val lines = run {
        val lines = readInputLines("day-06-input")
        val lineLength = lines.maxOf { it.length }
        lines.map { it.padEnd(lineLength) }
    }
    val problems = buildList {
        add(mutableListOf())
        for (lineT in lines.transpose()) {
            if (lineT.isNotBlank()) {
                last().add(lineT)
            } else {
                add(mutableListOf())
            }
        }
    }.map { linesT ->
        linesT.transpose()
    }.map { lines ->
        Problem(
            numLines = lines.dropLast(n = 1),
            op = lines.last().trim(),
        )
    }

    part1(problems).println()
    part2(problems).println()
}
