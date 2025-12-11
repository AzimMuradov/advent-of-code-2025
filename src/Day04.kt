fun main() {
    fun canAccess(pos: Pos, grid: List<List<Char>>, dim: Rect): Boolean =
        Vec.MOVES_8
            .asSequence()
            .map { move -> pos + move }
            .filter { pos -> pos in dim }
            .map { pos -> grid[pos] }
            .count { obj -> obj == '@' } < 4

    fun part1(grid: List<List<Char>>): Int {
        val dim = grid.rect()
        var count = 0
        grid.iterateMap { pos, obj ->
            if (obj == '@' && canAccess(pos, grid, dim)) {
                count += 1
            }
        }
        return count
    }

    fun part2(grid: List<MutableList<Char>>): Int {
        val dim = grid.rect()
        var count = 0
        do {
            var keepTrying = false
            grid.iterateMap { pos, obj ->
                if (obj == '@' && canAccess(pos, grid, dim)) {
                    count += 1
                    grid[pos] = 'x'
                    keepTrying = true
                }
            }
        } while (keepTrying)
        return count
    }


    val grid = readInputLines("day-04-input").map { line ->
        line.toMutableList()
    }

    part1(grid).println()
    part2(grid).println()
}
