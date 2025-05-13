package game;

import java.util.*;

public class IslandMap {
    private final List<Tile> tiles = new ArrayList<>();

    public IslandMap() {
        generateMap();
    }

    private void generateMap() {
        String[] tileNames = {
                "Temple of the Moon", "Temple of the Sun", // Earth Stone
                "Cave of Shadows", "Cave of Embers",       // Crystal of Fire
                "Coral Palace", "Tidal Palace",            // Ocean’s Chalice
                "Whispering Garden", "Howling Garden",     // Statue of Wind
                "Fools’ Landing", "Watchtower", "Cliffs of Abandon",
                "Gold Gate", "Silver Gate", "Bronze Gate", "Iron Gate",
                "Breakers Bridge", "Lost Lagoon", "Dunes of Deception",
                "Misty Marsh", "Observatory", "Phantom Rock",
                "Twilight Hollow", "Crimson Forest"
        };

        Set<String> treasureSites = Set.of(
                "Temple of the Moon", "Temple of the Sun",
                "Cave of Shadows", "Cave of Embers",
                "Coral Palace", "Tidal Palace",
                "Whispering Garden", "Howling Garden"
        );

        List<Tile> tempTiles = new ArrayList<>();
        for (String name : tileNames) {
            tempTiles.add(new Tile(name, treasureSites.contains(name)));
        }

        Collections.shuffle(tempTiles);
        tiles.clear();
        tiles.addAll(tempTiles);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile findTileByName(String name) {
        for (Tile t : tiles) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public void printMap() {
        System.out.println("🌴 Island Map:");
        for (int i = 0; i < tiles.size(); i++) {
            System.out.println((i + 1) + ". " + tiles.get(i));
        }
    }
}