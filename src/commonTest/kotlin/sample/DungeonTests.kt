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
import dungeonkit.text.Title
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals
class DungeonTests {
    init { println("Running $platform tests...") }

    @Test @JsName("TestCoordsAndDims")
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

    @Test @JsName("TestGenDungeon")
    fun `Generate dungeon and check name, size, data and bounds`() {
        val dungeon = DungeonKit.create("Test Dungeon", 100 by 100, SimpleCharTileMap)
        assertEquals(dungeon.dimension.area, 10000)
        assertEquals(dungeon.name, "Test Dungeon")
    }

    @Test @JsName("TestBounds")
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

    @Test @JsName("TestGenCommandLine")
    fun `Dungeon generation in command line`() {
        val d = DungeonKit.create(dimension = 80 by 80, tileMap = SimpleCharTileMap).steps(
            MindlessWanderer(20, 450, newTileBias = 0.95, maxRetries = 10), Trim, Denoise,
            Eval { _, _, tile ->
                if (tile == Tiles.FLOOR && random.nextInt(100) > 85) Tiles.EXIT else null
            }
        )

        d.render(ConsoleRenderer)
    }

    @Test @JsName("TestGenBinarySplit")
    fun `Binary split partition testing`() {
        DungeonKit.create(dimension = 50.dim, tileMap = SimpleCharTileMap)
            .steps(
                BinarySplit(6),
                Trim(2),
                RegionDetect { region, map, tileMap ->
                    Path(
                        region.positions.random(),
                        region.next?.positions?.random() ?: return@RegionDetect,
                        5
                    ).process(map, tileMap)
                    if (Random.nextBoolean()) {
                        for (pos in region.positions) {
                            if (Random.nextDouble() > 0.6)
                                map[pos] = tileMap["exit"].tile
                        }
                    }
                }
            ).render(ConsoleRenderer)
    }

    @Test @JsName("TestGenCellularAutonoma")
    fun `Test cellular automaton generator`() {
        DungeonKit.create(dimension = 70.dim, tileMap = SimpleCharTileMap)
            .steps(Automaton, Trim(2), Smoothing).render(ConsoleRenderer)
    }

    @Test @JsName("TestGenRigidPath")
    fun `Generate rigid pathway`() {
        DungeonKit.create(dimension = 50.dim, tileMap = SimpleCharTileMap)
            .steps(Path(50.dim.random(), 50.dim.random(), randomness = 0.3), Trim).render(ConsoleRenderer)
    }

    @Test @JsName("TestGenCellTree")
    fun `Generate cell tree map`() {
        DungeonKit.create(dimension = 60.dim, tileMap = SimpleCharTileMap)
            .steps(CellTree, Trim).render(ConsoleRenderer)
    }

    @Test @JsName("TestGenDungeonTitle")
    fun `Test dungeon name generator`() {
        println(Title.generate())
    }
}

