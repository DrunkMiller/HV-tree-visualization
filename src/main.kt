import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val defaultGraphFile = "examples/small-wide-tree-20n"
    val graphFile = if(args.isEmpty()) defaultGraphFile else args[0]
    val root = readGraphml("$graphFile.graphml")
    val nodesPositions = LayeredTreeAlgorithm().place(root)
    val image = GraphGraphics(grid = true).drawTree(nodesPositions)
    val file = File("$graphFile.png")
    ImageIO.write(image, "png", file)
}




