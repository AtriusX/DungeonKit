package sample

import dungeonkit.DungeonKit
import dungeonkit.DungeonKit.random
import dungeonkit.data.Direction
import dungeonkit.data.Grid
import dungeonkit.data.at
import dungeonkit.data.by
import dungeonkit.data.steps.*
import dungeonkit.data.tiles.Tiles
import dungeonkit.data.tiles.binding.SimpleCharTileMap
import dungeonkit.dim
import dungeonkit.platform
import dungeonkit.renderer.ConsoleRenderer
import dungeonkit.text.Text
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

class DungeonTests {
    init { println("Running $platform tests...") }

    @Test
    fun `Test coordinates and dimensions`() {
        val c1 = 10 at 15
        val c2 = 15 at 15
        // Test Coordinates
        assertEquals(c1 distance c2, 5.0)
        assertEquals(c2 - c1, 5 at 0)
        assertEquals(c1 + c2, 25 at 30)
        assertEquals(c1 relative Direction.SOUTH,      10 at 16)
        assertEquals(c1.relative(Direction.SOUTH, 10), 10 at 25)
        assertNotEquals(c1 relative Direction.NORTH, c1)
        // Test moving coordinates
        assertEquals(c1 move Direction.EAST, 11 at 15)
        assertEquals(c1.move(Direction.EAST, 10), c1)

        val d = 8 by 8
        // Test Dimensions
        assertEquals(d.area, 64)
        assertEquals(d + (3 by 2), 11 by 10)
        assertEquals(d - (3 by 2), 5  by 6)
    }

    @Test
    fun `Generate dungeon and check name, size, data and bounds`() {
        val dungeon = DungeonKit.create("Test Dungeon", 100 by 100, SimpleCharTileMap)
        assertEquals(dungeon.dimension.area, 10000)
        assertEquals(dungeon.name, "Test Dungeon")
    }

    @Test
    fun `Test grid bounds and tiles`() {
        val grid = Grid(100 by 100, Tiles.WALL)
        // Test bounds
        assertFails { grid[100, 100] }
        assertFails { grid[-1, -1] }
        assertFails { grid[100, 100] = Tiles.FLOOR }
        assertFails { grid[-1, -1]  = Tiles.FLOOR }
        // Update a tile within the valid range
        grid[50, 50] = Tiles.FLOOR
        // Check tiles
        assertEquals(grid[0, 0], Tiles.WALL)
        assertEquals(grid[50,50], Tiles.FLOOR)
    }

    @Test
    fun `Dungeon generation in command line`() {
        DungeonKit.create(dimension = 80 by 80, tileMap = SimpleCharTileMap).steps(
            MindlessWanderer(20, 450, newTileBias = 0.95, maxRetries = 10),
            Trim, Denoise, Eval { _, _, tile -> if (tile == Tiles.FLOOR &&
                random.nextInt(100) > 85) Tiles.EXIT else null }
        ).render(ConsoleRenderer)
    }

    @Test
    fun `Binary split partition testing`() {
        DungeonKit.create(dimension = 50.dim, tileMap = SimpleCharTileMap)
            .steps(BinarySplit, Trim(2)).render(ConsoleRenderer)
    }

    @Test
    fun `Test cellular automaton generator`() {
        DungeonKit.create(dimension = 70.dim, tileMap = SimpleCharTileMap)
            .steps(Automaton, Trim(2), Smoothing).render(ConsoleRenderer)
    }

    @Test
    fun `Generate rigid pathway`() {
        DungeonKit.create(dimension = 50.dim, tileMap = SimpleCharTileMap)
            .steps(Path(50.dim.random(), 50.dim.random(), randomness = 0.3), Trim).render(ConsoleRenderer)
    }

    @Test
    fun `Test dungeon name generator`() {
        println(Text.generate())
    }
}