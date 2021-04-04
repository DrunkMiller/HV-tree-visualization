import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val defaultGraphFile = "examples/small-wide-tree-20n"
    val graphFile = if (args.isEmpty()) defaultGraphFile else args[0]
    val root = readGraphml("$graphFile.graphml")
    val nodesPositions = LayeredTreeAlgorithm().place(root)
    val graphGraphics = GraphGraphics(
        nodeRadius = 20.0,
        fillColor = Color.blue,
        borderColor = Color.black,
        xPadding = 80.0,
        yPadding = 100.0,
        bezierEdges = true,
        nodeTitle = true,
        grid = true
    )
    val image = graphGraphics.drawTree(nodesPositions)
    val file = File("$graphFile.png")
    ImageIO.write(image, "png", file)
}




