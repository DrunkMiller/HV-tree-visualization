interface NodeInt {
    var left: NodeInt?
    var right: NodeInt?
}

data class Node(
    val id: String,
    override var left: NodeInt? = null,
    override var right: NodeInt? = null
): NodeInt

data class PlacedNode(
    val base: Node,
    var xPos: Int = 0,
    var yPos: Int = 0
) : NodeInt by base {
    override var left: NodeInt? = null
    override var right: NodeInt? = null
}