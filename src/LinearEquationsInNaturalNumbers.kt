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
        val dependentVariableIndices: List<Int>,
        val freeVariableIndices: List<Int>,
        val dependentVariableEquations: Map<Int, DependentVariableEquation>,
    ) : Solution()

    // TODO : Implement Solution.None case for future uses
    data object None : Solution()
}

// x = (k_0 * v_0 + ... + k_n * v_n + v) / d, x in Nat
data class DependentVariableEquation(
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
    val depVarIndices = mutableListOf<Int>()
    val freeVarIndices = mutableListOf<Int>()

    val equations = equations.mapTo(mutableListOf(), ::simplify)

    for (varI in 0..<equations.first().size - 1) {
        val equation = run {
            val index = equations.indexOfFirstOrNull { eq -> eq[varI] != 0L } ?: run {
                freeVarIndices += varI
                continue
            }
            equations.removeAt(index)
        }
        resultingEquations += equation

        subtractAndSimplify(equations, equation, varIndex = varI)
        depVarIndices += varI
    }

    for ((varI, eqI) in (depVarIndices zip resultingEquations.indices).reversed()) {
        subtractAndSimplify(
            equations = resultingEquations.subList(0, eqI),
            equation = resultingEquations[eqI],
            varIndex = varI,
        )
    }

    return if (freeVarIndices.isNotEmpty()) {
        Solution.Multiple(
            dependentVariableIndices = depVarIndices,
            freeVariableIndices = freeVarIndices,
            dependentVariableEquations = (depVarIndices zip resultingEquations).toMap()
                .mapValues { (varI, eq) ->
                    val (coefficients, value) = eq.splitAt(eq.lastIndex)
                    DependentVariableEquation(
                        coefficients = coefficients
                            .mapIndexed { i, k -> i to k }.toMap()
                            .filterKeys(freeVarIndices::contains),
                        value = -value.first(),
                        divisor = -eq[varI],
                    )
                },
        )
    } else {
        Solution.One(
            values = resultingEquations.mapIndexed { i, eq -> eq.last() / eq[i] },
        )
    }
}

private fun simplify(equation: List<Long>): List<Long> {
    val gcd = gcd(*equation.toLongArray())
    return if (gcd != 0L) equation.map { it / gcd } else equation
}

private fun subtractAndSimplify(equations: MutableList<List<Long>>, equation: List<Long>, varIndex: Int) {
    fun subtract(eqA: List<Long>, eqB: List<Long>, varI: Int): List<Long> {
        val multA = eqB[varI]
        val multB = eqA[varI]
        return eqA.zip(eqB) { kA, kB -> multA * kA - multB * kB }
    }
    for ((i, eq) in equations.withIndex()) {
        equations[i] = simplify(equation = subtract(eq, equation, varIndex))
    }
}
