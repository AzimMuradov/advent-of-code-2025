inline fun List<LongRange>.iterate(f: (List<Long>) -> Unit) {
    if (any { it.isEmpty() }) return

    val ranges = this
    val state = map { r -> r.first }.toMutableList()
    multiloop@ while (true) {
        for (i in state.indices.reversed()) {
            if (state[i] !in ranges[i]) {
                if (i != 0) {
                    state[i] = ranges[i].first
                    state[i - 1] += 1
                } else {
                    break@multiloop
                }
            }
        }
        f(state.toList())
        state[state.lastIndex] += 1
    }
}


fun main() {
    data class MachineManual(
        val size: Int,
        val indicatorLightDiagram: List<Boolean>,
        val buttonWiringSchematics: List<List<Int>>,
        val joltageRequirements: List<Long>,
    )

    fun part1(machineManuals: List<MachineManual>): Long {
        return machineManuals.sumOf { (size, diagram, wiring) ->
            val memory = mutableMapOf(List(size) { false } to 0)
            var iter = mapOf(List(size) { false } to 0)
            while (diagram !in memory) {
                iter = buildMap {
                    for ((lights, cnt) in iter) {
                        for (w in wiring) {
                            val newLights = lights.workAsMut {
                                for (i in w) {
                                    this[i] = !this[i]
                                }
                            }
                            val mem = memory[newLights]
                            if (mem == null || mem > cnt + 1) {
                                memory[newLights] = cnt + 1
                                put(newLights, cnt + 1)
                            }
                        }
                    }
                }
            }
            memory.getValue(diagram)
        }.toLong()
    }

    fun part2(machineManuals: List<MachineManual>): Long {
        return machineManuals.sumOf { (size, _, schs, reqs) ->
            val equations = run {
                val left = schs.map { w ->
                    val w = w.toSet()
                    List(size) { if (it in w) 1L else 0L }
                }.transpose()

                val lines = (left zip reqs).map { (ks, v) -> ks + v }

                Matrix(lines)
            }

            when (val solution = solveSystemOfLinearEquations(equations)) {
                is Solution.One -> solution.values.sum()

                is Solution.Multiple -> {
                    val (_, freeIds, depEqs) = solution

                    val iterationRanges = freeIds.map {
                        0..schs[it].minOf(reqs::get)
                    }

                    val sums = sequence {
                        iterationRanges.iterate { freeVals ->
                            val proposedSolution = (freeIds zip freeVals).toMap()
                            val depVals = depEqs.map { (_, depEq) -> depEq.solveOrNull(proposedSolution) }
                            if (depVals.all { it != null }) {
                                yield(depVals.filterNotNull().sum() + freeVals.sum())
                            }
                        }
                    }
                    sums.min()
                }

                Solution.None -> error("unreachable")
            }
        }
    }


    val machineManuals = readInputLines("day-10-input").map { line ->
        val split = line.split(" ")
        val indicatorLightDiagram = split
            .first()
            .drop(1).dropLast(1)
            .map { it == '#' }
        val buttonWiringSchematics = split
            .drop(1).dropLast(1)
            .map { buttonWiringSchematic ->
                buttonWiringSchematic
                    .drop(1).dropLast(1)
                    .toInts(separator = ",")
            }
        val joltageRequirements = split
            .last()
            .drop(1).dropLast(1)
            .toLongs(separator = ",")

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
