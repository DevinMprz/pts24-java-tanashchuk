package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.Collection;
import java.util.OptionalInt;
import java.util.HashSet;

public class VariableBuilding implements Building {
    private final int numberOfResources;
    private final int numberOfResourceTypes;

    public VariableBuilding(int numberOfResources, int numberOfResourceTypes) {
        this.numberOfResources = numberOfResources;
        this.numberOfResourceTypes = numberOfResourceTypes;
    }

    @Override
    public OptionalInt build(Collection<Effect> resources) {
        if (resources.size() != numberOfResources) {
            return OptionalInt.empty();
        }

        // Check all resources are valid
        for (Effect resource : resources) {
            if (!resource.isResource()) {
                return OptionalInt.empty();
            }
        }

        // Count unique resource types
        HashSet<Effect> uniqueResources = new HashSet<>(resources);
        System.out.println(uniqueResources.size());
        if (uniqueResources.size() != numberOfResourceTypes) {
            return OptionalInt.empty();
        }

        // Calculate points
        int points = 0;
        for (Effect resource : resources) {
            points += resource.points();
        }

        return OptionalInt.of(points);
    }

    @Override
    public String state() {
        return String.format("VariableBuilding[resources=%d,types=%d]",
                numberOfResources, numberOfResourceTypes);
    }
}
