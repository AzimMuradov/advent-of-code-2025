fun main() {
    fun findMaxJoltage(bank: String, joltageLength: Int): Long {
        var maxJoltageString = ""
        var rangeStart = 0
        for (rangeEnd in bank.length - joltageLength..<bank.length) {
            var max = '0'
            var maxI: Int? = null
            for (i in rangeEnd downTo rangeStart) {
                if (bank[i] >= max) {
                    max = bank[i]
                    maxI = i
                }
            }
            rangeStart = maxI!! + 1
            maxJoltageString += max
        }
        return maxJoltageString.toLong()
    }

    fun part1(banks: List<String>): Long = banks.sumOf { bank ->
        findMaxJoltage(bank, joltageLength = 2)
    }

    fun part2(banks: List<String>): Long = banks.sumOf { bank ->
        findMaxJoltage(bank, joltageLength = 12)
    }


    val banks = readInputLines("day-03-input")

    part1(banks).println()
    part2(banks).println()
}
