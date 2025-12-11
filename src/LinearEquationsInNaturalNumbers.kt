import kotlin.math.sign


data class SystemOfEquations(val equations: List<List<Long>>) {

    override fun toString(): String {
        val padLength = equations.maxOf { equation ->
            equation.map(Long::toString).maxOf(String::length)
        }
        return equations.withIndex().joinToString("\n") { (i, equation) ->
            val (leftBorder, rightBorder) = when (i) {
                0 -> "/" to "\\"
                equations.lastIndex -> "\\" to "/"
                else -> "|" to "|"
            }
            val (coefficients, value) = equation
                .map(Long::toString)
                .map { it.padStart(padLength) }
                .splitAt(equation.lastIndex)

            val lineParts = listOf(leftBorder) + coefficients + listOf("|") + value + listOf(rightBorder)

            lineParts.joinToString(" ")
        }
    }
}


sealed class Solution {

    data class One(val values: List<Long>) : Solution()

    data class Multiple(
        val dependentIndices: List<Int>,
        val freeIndices: List<Int>,
        val dependentEquations: Map<Int, DependentEquation>,
    ) : Solution()

    // TODO : Implement Solution.None case for future uses
    data object None : Solution()
}

// x = (k_0 * v_0 + ... + k_n * v_n + v) / d, x in Nat
data class DependentEquation(
    val coefficients: Map<Int, Long>,
    val value: Long,
    val divisor: Long,
) {

    fun solveOrNull(values: Map<Int, Long>): Long? {
        val sum = coefficients.map { (i, k) -> k * values.getValue(i) }.sum() + value

        return if (sum % divisor == 0L && sum.sign * divisor.sign >= 0) {
            sum / divisor
        } else {
            null
        }
    }
}


fun SystemOfEquations.solve(): Solution {
    val resultingEquations = mutableListOf<List<Long>>()
    val dependentIndices = mutableListOf<Int>()
    val freeIndices = mutableListOf<Int>()

    run {
        val equations = simplify(equations)

        var rowI = 0
        var colI = 0
        val rowN = equations.size
        val colN = equations.first().size - 1

        while (rowI < rowN && colI < colN) {
            val equation = run {
                val index = equations.indexOfFirstOrNull { row -> row[colI] != 0L } ?: run {
                    freeIndices += colI++
                    continue
                }
                equations.removeAt(index)
            }

            resultingEquations += equation

            subtract(equations, equation, index = colI)

            rowI++
            dependentIndices += colI++
        }

        if (colI < colN) {
            freeIndices += colI..<colN
        }
    }

    for ((i, j) in (dependentIndices zip resultingEquations.indices).reversed()) {
        subtract(
            equations = resultingEquations.subList(0, j),
            equation = resultingEquations[j],
            index = i,
        )
    }

    return if (freeIndices.isNotEmpty()) {
        Solution.Multiple(
            dependentIndices,
            freeIndices,
            dependentEquations = (dependentIndices zip resultingEquations).toMap().mapValues { (i, equation) ->
                val (coefficients, value) = equation.splitAt(equation.lastIndex)
                DependentEquation(
                    coefficients = (coefficients.indices zip coefficients).toMap().filterKeys { i -> i in freeIndices },
                    value = -value.first(),
                    divisor = -equation[i],
                )
            },
        )
    } else {
        Solution.One(
            values = resultingEquations.mapIndexed { i, eq -> eq.last() / eq[i] },
        )
    }
}

@JvmName("simplifyEquations")
private fun simplify(equations: List<List<Long>>): MutableList<List<Long>> {
    return equations.mapTo(mutableListOf(), ::simplify)
}

@JvmName("simplifyEquation")
private fun simplify(equation: List<Long>): List<Long> {
    val gcd = gcd(*equation.toTypedArray().toLongArray())
    return if (gcd != 0L) equation.map { it / gcd } else equation
}

private fun subtract(equations: MutableList<List<Long>>, equation: List<Long>, index: Int) {
    fun subtract(eqA: List<Long>, eqB: List<Long>, index: Int): List<Long> {
        val kA = eqB[index]
        val kB = eqA[index]
        return (eqA zip eqB).map { (a, b) -> kA * a - kB * b }
    }
    for ((i, eq) in equations.withIndex()) {
        equations[i] = simplify(equation = subtract(eq, equation, index))
    }
}
