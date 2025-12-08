class DisjointSets(private val size: Int) {

    private val parent: IntArray = IntArray(size) { it }

    private val rank: IntArray = IntArray(size)


    fun find(a: Int): Int {
        var a = a
        while (parent[a] != a) {
            parent[a] = parent[parent[a]]
            a = parent[a]
        }
        return a
    }

    fun union(a: Int, b: Int) {
        val aSetId = find(a)
        val bSetId = find(b)

        if (aSetId == bSetId) return

        if (rank[aSetId] < rank[bSetId]) {
            parent[aSetId] = bSetId
        } else {
            parent[bSetId] = aSetId

            if (rank[aSetId] == rank[bSetId]) {
                rank[aSetId] += 1
            }
        }
    }

    fun setIds(): List<Int> = (0..<size).map(::find)
}
