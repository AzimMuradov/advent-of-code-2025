fun main() {
    fun part1(tachyonManifoldDiagram: List<String>): Int {
        var splitCount = 0
        var beams = setOf(tachyonManifoldDiagram.rect().w / 2)
        for (line in tachyonManifoldDiagram.drop(1)) {
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

    fun part2(tachyonManifoldDiagram: List<String>): Long {
        var beams = mapOf(tachyonManifoldDiagram.rect().w / 2 to 1L)
        for (line in tachyonManifoldDiagram.drop(1)) {
            beams = buildMap {
                for ((beam, timelineCount) in beams) {
                    if (line[beam] == '.') {
                        plusTo(beam, timelineCount)
                    } else {
                        plusTo(beam - 1, timelineCount)
                        plusTo(beam + 1, timelineCount)
                    }
                }
            }
        }
        return beams.values.sum()
    }


    val tachyonManifoldDiagram = readInputLines("day-07-input")

    part1(tachyonManifoldDiagram).println()
    part2(tachyonManifoldDiagram).println()
}
