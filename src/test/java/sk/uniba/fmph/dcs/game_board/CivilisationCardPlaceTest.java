package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;


public class CivilisationCardPlaceTest {
    private final CivilizationCard[] allCard = {
            //Dice roll ( 10 cards )
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.WRITING)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.SUNDIAL)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.POTTERY)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.TRANSPORT)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward),
                    Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)),

            // FOOD (7 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.MEDICINE)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.POTTERY)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.WEAVING)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.WEAVING)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD),
                    Arrays.asList(EndOfGameEffect.BUILDER)),

// Resource (5 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.STONE, ImmediateEffect.STONE),
                    Arrays.asList(EndOfGameEffect.TRANSPORT)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.STONE),
                    Arrays.asList(EndOfGameEffect.FARMER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.STONE),
                    Arrays.asList(EndOfGameEffect.SHAMAN)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.CLAY),
                    Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.GOLD),
                    Arrays.asList(EndOfGameEffect.SHAMAN)),
// Resources with dice roll (3 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowStone),
                    Arrays.asList(EndOfGameEffect.SHAMAN)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowGold),
                    Arrays.asList(EndOfGameEffect.ART)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.WOOD),
                    Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)),

// Victory POINTs (3 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    Arrays.asList(EndOfGameEffect.MUSIC)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    Arrays.asList(EndOfGameEffect.MUSIC)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),

// Extra.Tool tile (1 card)
            new CivilizationCard(Arrays.asList(ImmediateEffect.Tool),
                    Arrays.asList(EndOfGameEffect.ART)),

// Agriculture (2 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.Field),
                    Arrays.asList(EndOfGameEffect.SUNDIAL)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.Field),
                    Arrays.asList(EndOfGameEffect.FARMER)),

// Civilization card for the final scoring (1 card)
            new CivilizationCard(Arrays.asList(ImmediateEffect.CARD),
                    Arrays.asList(EndOfGameEffect.WRITING)),

// One-use.tool (3 cards)
            new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool2),
                    Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool3),
                    Arrays.asList(EndOfGameEffect.BUILDER)),
            new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool4),
                    Arrays.asList(EndOfGameEffect.BUILDER)),

// Any 2 resources (1 card)
            new CivilizationCard(Arrays.asList(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource),
                    Arrays.asList(EndOfGameEffect.MEDICINE)),

};

    private static class MockPlayer implements Player {
        private final PlayerOrder order;
        private final PlayerBoard board;

        public MockPlayer(int orderNum, PlayerBoard board) {
            this.order = new PlayerOrder(orderNum, 2);
            this.board = board;
        }

        @Override
        public PlayerOrder playerOrder() {
            return order;
        }

        @Override
        public InterfacePlayerBoardGameBoard playerBoard() {
            return (InterfacePlayerBoardGameBoard) board;
        }
    }
    private static class MockPlayerBoard implements PlayerBoard {
        private boolean hasFiguresResponse = true;

        @Override
        public boolean hasFigures(int count) {
            return hasFiguresResponse;
        }

        @Override
        public void giveEffect(Effect[] stuff) {

        }

        public void setHasFiguresResponse(boolean response) {
            this.hasFiguresResponse = response;
        }
    }

    @Test
    public void initializationTest(){
        CivilizationCardDeck ccd = new CivilizationCardDeck(allCard);
        CivilizationCardPlace ccp1 = new CivilizationCardPlace(null, ccd, 1);
        CivilizationCardPlace ccp2 = new CivilizationCardPlace(ccp1, ccd, 2);
        CivilizationCardPlace ccp3 = new CivilizationCardPlace(ccp2, ccd, 3);
        CivilizationCardPlace ccp4 = new CivilizationCardPlace(ccp3, ccd, 4);

        assertEquals(1, ccp1.getRequiredResources());
        assertEquals(2, ccp2.getRequiredResources());
        assertEquals(3, ccp3.getRequiredResources());
        assertEquals(4, ccp4.getRequiredResources());

        assertNotEquals(ccp1.getNextCard(), ccp2.getNextCard());
        assertNotEquals(ccp2.getNextCard(), ccp3.getNextCard());
        assertNotEquals(ccp3.getNextCard(), ccp4.getNextCard());
    }

    @Test
    public void placeFiguresTest(){
        CivilizationCardDeck ccd = new CivilizationCardDeck(allCard);
        CivilizationCardPlace ccp1 = new CivilizationCardPlace(null, ccd, 1);
        CivilizationCardPlace ccp2 = new CivilizationCardPlace(ccp1, ccd, 2);
        CivilizationCardPlace ccp3 = new CivilizationCardPlace(ccp2, ccd, 3);
        CivilizationCardPlace ccp4 = new CivilizationCardPlace(ccp3, ccd, 4);
        Player p = new MockPlayer(1, new MockPlayerBoard());

        assertTrue(ccp1.placeFigures(p, 1));
        assertFalse(ccp1.placeFigures(p, 1));
    }
    @Test
    public void makeActionTest(){
        CivilizationCardDeck ccd = new CivilizationCardDeck(allCard);
        CivilizationCardPlace ccp1 = new CivilizationCardPlace(null, ccd, 1);
        CivilizationCardPlace ccp2 = new CivilizationCardPlace(ccp1, ccd, 2);
        CivilizationCardPlace ccp3 = new CivilizationCardPlace(ccp2, ccd, 3);
        CivilizationCardPlace ccp4 = new CivilizationCardPlace(ccp3, ccd, 4);
        Player p = new MockPlayer(1, new MockPlayerBoard());
        assertEquals(HasAction.NO_ACTION_POSSIBLE, ccp1.tryToMakeAction(p));
        ccp1.placeFigures(p, 1);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, ccp1.tryToMakeAction(p));

    }
}
