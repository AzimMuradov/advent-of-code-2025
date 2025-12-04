fun main() {
    fun canAccess(obj: Char, pos: Pos, grid: List<List<Char>>, dim: Rect): Boolean {
        return obj == '@' &&
                Vec.MOVES_8.map { pos + it }.count { it in dim && grid[it] == '@' } < 4
    }

    fun part1(grid: List<List<Char>>): Int {
        val dim = grid.rect()
        var count = 0
        grid.iterateMap { pos, obj ->
            if (canAccess(obj, pos, grid, dim)) {
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
                if (canAccess(obj, pos, grid, dim)) {
                    count += 1
                    grid[pos] = 'x'
                    keepTrying = true
                }
            }
        } while (keepTrying)
        return count
    }


    val grid = readInputLines("day-04-input").map {
        it.toMutableList()
    }

    part1(grid).println()
    part2(grid).println()
}
