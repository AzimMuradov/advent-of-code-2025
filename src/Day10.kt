fun main() {
    data class MachineManual(
        val size: Int,
        val indicatorLightDiagram: List<Boolean>,
        val buttonWiringSchematics: List<List<Int>>,
        val joltageRequirements: List<Long>,
    )

    fun part1(machineManuals: List<MachineManual>): Long = machineManuals.sumOf { (size, correctLights, buttons) ->
        val memory = mutableSetOf(List(size) { false })
        val q = ArrayDeque<Pair<List<Boolean>, Int>>().apply {
            add(List(size) { false } to 0)
        }
        searchLoop@ while (correctLights !in memory) {
            val (lights, count) = q.removeFirst()
            for (button in buttons) {
                val newLights = lights.workAsMut {
                    for (i in button) {
                        this[i] = !this[i]
                    }
                }
                if (newLights !in memory) {
                    memory += newLights
                    q.addLast(newLights to count + 1)
                }
                if (newLights == correctLights) {
                    break@searchLoop
                }
            }
        }
        val (_, minButtonPressesCount) = q.last()
        minButtonPressesCount
    }.toLong()

    fun part2(machineManuals: List<MachineManual>): Long = machineManuals.sumOf { (size, _, buttons, correctJoltage) ->
        val equations = run {
            val coefficients = buttons.map { button ->
                val button = button.toSet()
                List(size) { i ->
                    if (i in button) 1L else 0L
                }
            }.transpose()

            SystemOfEquations(equations = (coefficients zip correctJoltage).map { (ks, v) -> ks + v })
        }

        when (val solution = equations.solve()) {
            is Solution.One -> solution.values.sum()

            is Solution.Multiple -> {
                val (_, freeIndices, depEqs) = solution

                val iterationRanges = freeIndices.map { freeI ->
                    0..buttons[freeI].minOf(correctJoltage::get)
                }

                val sums = sequence {
                    iterateOverRanges(iterationRanges) { freeVals ->
                        val proposedSolution = (freeIndices zip freeVals).toMap()
                        val depVals = depEqs.values.map { depEq -> depEq.solveOrNull(proposedSolution) }
                        if (depVals.none { it == null }) {
                            yield(depVals.sumOf { it!! } + freeVals.sum())
                        }
                    }
                }
                sums.min()
            }

            Solution.None -> error("unreachable")
        }
    }


    val machineManuals = readInputLines("day-10-input").map { line ->
        val (ild, bws, jr) = run {
            val spaceSplit = line.split(" ").map { it.removeSurrounding(length = 1) }
            spaceSplit.splitAt(1, spaceSplit.lastIndex)
        }

        val indicatorLightDiagram = ild.first().map { it == '#' }
        val buttonWiringSchematics = bws.map { schematic ->
            schematic.toInts(separator = ",")
        }
        val joltageRequirements = jr.first().toLongs(separator = ",")

        MachineManual(
            size = indicatorLightDiagram.size,
            indicatorLightDiagram,
            buttonWiringSchematics,
            joltageRequirements,
        )
    }

    part1(machineManuals).println()
    part2(machineManuals).println()
}
