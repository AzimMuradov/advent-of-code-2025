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
    val problems = lines
        .transpose()
        .joinToString(separator = "\n")
        .split(Regex("""\n {${lines.size}}\n"""))
        .map(String::lines)
        .map(List<String>::transpose)
        .map { lines ->
            val (numLines, op) = lines.splitAt(lines.lastIndex)
            Problem(numLines, op = op.first().trim())
        }

    part1(problems).println()
    part2(problems).println()
}
