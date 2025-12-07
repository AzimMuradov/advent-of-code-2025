fun main() {
    fun part1(diagram: List<String>): Int {
        var splitCount = 0
        var beams = setOf(diagram.rect().w / 2)
        for (line in diagram.drop(1)) {
            beams = buildSet {
                for (beam in beams) {
                    if (line[beam] == '.') {
                        add(beam)
                    } else {
                        addAll(listOf(beam - 1, beam + 1))
                        splitCount += 1
                    }
                }
            }
        }
        return splitCount
    }

    fun part2(diagram: List<String>): Long {
        fun MutableMap<Int, Long>.registerBeam(beam: Int, timelineCount: Long) {
            put(beam, timelineCount + getOrDefault(beam, 0))
        }

        var beams = mapOf(diagram.rect().w / 2 to 1L)
        for (line in diagram.drop(1)) {
            beams = buildMap {
                for ((beam, timelineCount) in beams) {
                    if (line[beam] == '.') {
                        registerBeam(beam, timelineCount)
                    } else {
                        registerBeam(beam - 1, timelineCount)
                        registerBeam(beam + 1, timelineCount)
                    }
                }
            }
        }
        return beams.values.sum()
    }


    val diagram = readInputLines("day-07-input")

    part1(diagram).println()
    part2(diagram).println()
}
