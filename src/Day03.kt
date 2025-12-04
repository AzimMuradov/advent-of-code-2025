fun main() {
    fun common(bank: String, len: Int): Long {
        var num = ""
        var rangeStart = 0
        for (j in len - 1 downTo 0) {
            var max = '0'
            var maxI: Int? = null
            for (i in bank.lastIndex - j downTo rangeStart) {
                if (bank[i] >= max) {
                    max = bank[i]
                    maxI = i
                }
            }
            rangeStart = maxI!! + 1
            num += max
        }
        return num.toLong()
    }

    fun part1(banks: List<String>): Long = banks.sumOf { bank ->
        common(bank, len = 2)
    }

    fun part2(banks: List<String>): Long = banks.sumOf { bank ->
        common(bank, len = 12)
    }


    val banks = readInputLines("day-03-input")

    part1(banks).println()
    part2(banks).println()
}
