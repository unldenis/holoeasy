import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.line.hologram.TextBlockStandardLoader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class LibTests {

  private ServerMock server;
  private JavaPlugin plugin;
  private WorldMock world;

  PlayerMock p1;
  PlayerMock p2;

  HologramPoolMock pool;


  @BeforeEach
  public void setUp() {
    // Start the mock server
    server = MockBukkit.mock();
    // Load your plugin
    plugin = MockBukkit.createMockPlugin("HologramApiTest");

    world = new WorldMock(Material.GRASS_BLOCK, 99);
    server.addWorld(world);

    p1 = server.addPlayer("P1");
    p2 = server.addPlayer("P2");

    pool = new HologramPoolMock(plugin, 70);
  }

  @AfterEach
  public void tearDown() {
    // Stop the mock server
    MockBukkit.unmock();
  }

  private Hologram testHolo(Location location) {
    TextLineMock line1 = new TextLineMock(plugin, "line1");
    TextLineMock line2 = new TextLineMock(plugin, "line2");

    Hologram hologram = new Hologram(plugin, location, new TextBlockStandardLoader());
    hologram.load(line1, line2);
    return hologram;
  }

  @Test
  public void poolFindPlayersTest() {
    Location l1 = new Location(world, 100, 100, 100);
    p1.teleport(l1);

    Hologram hologram = testHolo(l1);
    pool.takeCareOf(hologram);

    Assertions.assertFalse(hologram.isShownFor(p1));

    server.getScheduler().performTicks(100L);

    Assertions.assertTrue(hologram.isShownFor(p1));
  }

  @Test
  public void issue36() {
    Location l1 = new Location(world, 100, 100, 100);
    p1.teleport(new Location(world, 0,0,0));
    p2.teleport(l1);

    Hologram hologram = testHolo(l1);
    pool.takeCareOf(hologram);

    server.getScheduler().performTicks(100L);
    Assertions.assertFalse(hologram.isShownFor(p1));

    pool.remove(hologram);

    server.getScheduler().performTicks(100L);

    Assertions.assertFalse(hologram.isShownFor(p2));
  }





}
