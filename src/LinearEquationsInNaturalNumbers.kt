sealed class Solution {

    data class One(val values: List<Long>) : Solution()

    data class Multiple(
        val depIds: List<Int>,
        val freeIds: List<Int>,
        val depEqs: Map<Int, DependentEquation>,
    ) : Solution()

    data object None : Solution()
}

// x = (k_0 * a_0 + ... + k_n * a_n + v) / div >= 0
data class DependentEquation(
    val ks: Map<Int, Long>,
    val v: Long,
    val div: Long,
) {

    fun solveOrNull(vals: Map<Int, Long>): Long? {
        val sum = ks.keys.sumOf { k -> ks[k]!! * vals[k]!! } + v

        return if (sum % div == 0L && sum / div >= 0) sum / div else null
    }
}

private fun simplify(eq: List<Long>): List<Long> {
    fun gcd(a: Long, b: Long): Long {
        var a = if (a > 0) a else -a
        var b = if (b > 0) b else -b

        while (b != 0L) {
            val temp = b
            b = a % b
            a = temp
        }

        return a
    }

    val notNull = eq.firstOrNull { it != 0L } ?: return eq

    val gcd = eq.fold(notNull) { acc, x -> gcd(acc, x) }

    return eq.map { it / gcd }
}

fun solveSystemOfLinearEquations(matrix: Matrix): Solution {
    val equations = matrix.lines

    val fIds = mutableListOf<Int>()
    val nfIds = mutableListOf<Int>()

    var solutionMatrix = mutableListOf<List<Long>>()

    var todo = equations.map(::simplify).toMutableList()

    var rowI = 0
    var colI = 0
    val rowN = equations.size
    val colN = equations.first().size - 1

    while (rowI < rowN && colI < colN) {
        val next = todo.indexOfFirstOrNull { row -> row[colI] != 0L } ?: run {
            nfIds += colI++
            continue
        }

        val currEq = todo.removeAt(next)
        solutionMatrix.add(currEq)

        for ((i, eq) in todo.withIndex()) {
            val multForEq = currEq[colI]
            val multForCurrEq = eq[colI]
            todo[i] = (eq zip currEq).map { (e, ce) -> multForEq * e - multForCurrEq * ce }
        }
        todo = todo.map { simplify(it) }.toMutableList()

        rowI++
        fIds += colI++
    }
    if (colI < colN) {
        nfIds += colI..<colN
    }

    for ((i, j) in (fIds zip solutionMatrix.indices).reversed()) {
        val eq = solutionMatrix[j]
        for ((k, eqq) in solutionMatrix.subList(0, j).withIndex()) {
            val multForA = eq[i]
            val multForB = eqq[i]
            solutionMatrix[k] = (eqq zip eq).map { (a, b) -> multForA * a - multForB * b }
        }
        solutionMatrix = solutionMatrix.map { simplify(it) }.toMutableList()
    }

    check(fIds.size == solutionMatrix.size)

    return if (nfIds.isNotEmpty()) {
        Solution.Multiple(
            depIds = fIds,
            freeIds = nfIds,
            depEqs = (fIds zip solutionMatrix).associate { (i, v) ->
                i to DependentEquation(
                    ks = v
                        .dropLast(1).withIndex()
                        .filter { it.index in nfIds }
                        .associate { (j, u) -> j to u },
                    v = -v.last(),
                    div = -v[i],
                )
            },
        )
    } else {
        Solution.One(
            values = solutionMatrix.withIndex().map { (i, v) ->
                v.last() / v[i]
            },
        )
    }
}

data class Matrix(val lines: List<List<Long>>) {

    override fun toString(): String = lines.joinToString("\n") {
        val it = it.map { it.toString().padStart(3) }
        it.dropLast(1).joinToString(" ") + " | " + it.last()
    }
}
