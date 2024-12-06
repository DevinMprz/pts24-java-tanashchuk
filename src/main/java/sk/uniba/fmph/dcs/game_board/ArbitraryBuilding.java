package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.Collection;
import java.util.OptionalInt;

public class ArbitraryBuilding implements Building {
    private final int maxNumberOfResources;

    public ArbitraryBuilding(int maxNumberOfResources) {
        this.maxNumberOfResources = maxNumberOfResources;
    }

    @Override
    public OptionalInt build(Collection<Effect> resources) {
        if (resources.isEmpty() || resources.size() > maxNumberOfResources) {
            return OptionalInt.empty();
        }
        // Verify all resources are valid
        for (Effect resource : resources) {
            if (!resource.isResource()) {
                return OptionalInt.empty();
            }
        }

        // Calculate points
        int points = 0;
        for (Effect resource : resources) {
            points += resource.points();
        }

        return OptionalInt.of(points);
    }
    public int getMaxNumberOfResources() {
        return maxNumberOfResources;
    }
    @Override
    public String state() {
        return String.format("ArbitraryBuilding[maxResources=%d]", maxNumberOfResources);
    }
}
