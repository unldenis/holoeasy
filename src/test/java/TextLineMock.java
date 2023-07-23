import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.Line;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TextLineMock implements ILine<String> {

  private final Plugin plugin;
  private final int id = Line.IDs_COUNTER.getAndIncrement();

  private String obj;
  private Location location;

  public TextLineMock(Plugin plugin, String obj) {
    this.plugin = plugin;
    this.obj = obj;
  }

  @Override
  public Plugin getPlugin() {
    return plugin;
  }

  @Override
  public Type getType() {
    return Type.EXTERNAL;
  }

  @Override
  public int getEntityId() {
    return id;
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public String getObj() {
    return obj;
  }

  @Override
  public void setObj(String obj) {
    this.obj = obj;
  }

  @Override
  public void hide(Player player) {
    // hide logic
  }

  @Override
  public void teleport(Player player) {
    // teleport logic
  }

  @Override
  public void show(Player player) {
    // shown logic
  }

  @Override
  public void update(Player player) {
    // update logic
  }
}
