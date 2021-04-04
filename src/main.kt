import java.io.File
import javax.imageio.ImageIO

fun main() {
    val graphFile = "examples/small-wide-tree-20n"
    val root = readGraphml("$graphFile.graphml")
    val nodesPositions = LayeredTreeAlgorithm().place(root)
    val image = GraphGraphics(grid = true).drawTree(nodesPositions)
    val file = File("$graphFile.png")
    ImageIO.write(image, "png", file)
}




