open class DungeonException(message: String) : RuntimeException("Unable to create dungeon: $message")

class RenderException : DungeonException("No TileMap to generate")

//            val x = Random.nextInt(w)
//            val y = Random.nextInt(h)
//            val w = Random.nextInt(x, w)
//            val h = Random.nextInt(y, h)

//        tiles.forEach { x, y -> if (y and x shr y % 2 == 0) 1 else 0 }