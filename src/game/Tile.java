package game;

public class Tile {
    private final String name;
    private TileState state;
    private final boolean isTreasureSite;

    public Tile(String name, boolean isTreasureSite) {
        this.name = name;
        this.state = TileState.NORMAL;
        this.isTreasureSite = isTreasureSite;
    }

    public String getName() {
        return name;
    }

    public TileState getState() {
        return state;
    }

    public void flood() {
        if (state == TileState.NORMAL) {
            state = TileState.FLOODED;
        } else if (state == TileState.FLOODED) {
            state = TileState.SUNKEN;
        }
    }

    public void shoreUp() {
        if (state == TileState.FLOODED) {
            state = TileState.NORMAL;
        }
    }

    public boolean isTreasureSite() {
        return isTreasureSite;
    }

    public boolean isSunken() {
        return state == TileState.SUNKEN;
    }

    @Override
    public String toString() {
        return name + " (" + state + ")";
    }
}