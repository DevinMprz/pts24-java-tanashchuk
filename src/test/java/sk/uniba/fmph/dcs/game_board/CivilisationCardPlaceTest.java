package sk.uniba.fmph.dcs.game_board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.player_board.PlayerCivilizationCards;
import sk.uniba.fmph.dcs.stone_age.*;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.*;


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
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private CivilizationCardDeck ccd;
    private CivilizationCardPlace ccp1;
    private CivilizationCardPlace ccp2;
    private CivilizationCardPlace ccp3;
    private CivilizationCardPlace ccp4;
    private CivilizationCardDeck customDeck;

    private Field currentPlaceCard;
    @Before
    public void setUp() throws NoSuchFieldException {
        player1 = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        player2 = new Player(new PlayerOrder(2, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        player3 = new Player(new PlayerOrder(3, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        player4 = new Player(new PlayerOrder(4, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        List<CivilizationCard> allCardsList = new ArrayList<>(List.of(allCard));
        Collections.shuffle(allCardsList);
        ccd = new CivilizationCardDeck(allCardsList);
        ccp1 = new CivilizationCardPlace(null, ccd, 1);
        ccp2 = new CivilizationCardPlace(ccp1, ccd, 2);
        ccp3 = new CivilizationCardPlace(ccp2, ccd, 3);
        ccp4 = new CivilizationCardPlace(ccp3, ccd, 4);

        //Set last card in specialCardDeck to card with Immediate effect CARD
        allCardsList.set(allCardsList.size() - 1, new CivilizationCard(List.of(ImmediateEffect.CARD),
                List.of(EndOfGameEffect.WRITING)));
        customDeck = new CivilizationCardDeck(allCardsList);

        //Setting accesses to the card which is at the given card place
        currentPlaceCard = CivilizationCardPlace.class.getDeclaredField("nextCard");
        currentPlaceCard.setAccessible(true);
    }
    @Test
    public void initializationTest(){

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
        Player p = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));

        assertTrue(ccp1.placeFigures(p, 1));
        assertFalse(ccp1.placeFigures(p, 1));
    }
    @Test
    public void makeActionTest(){

        Player p = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, ccp1.tryToMakeAction(p));
        ccp1.placeFigures(p, 1);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, ccp1.tryToMakeAction(p));

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, ccp1.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, ccp1.tryToMakeAction(player3));

        assertFalse(ccp1.skipAction(player3));
        assertTrue(ccp1.skipAction(player1));
        assertTrue(ccp3.placeFigures(player3, 1));
        assertFalse(ccp3.skipAction(player1));
        assertTrue(ccp3.skipAction(player3));

        assertEquals(HasAction.NO_ACTION_POSSIBLE, ccp3.tryToMakeAction(player3));

        assertFalse(ccp1.newTurn());
        assertFalse(ccp2.newTurn());
        assertFalse(ccp3.newTurn());
        assertFalse(ccp4.newTurn());
    }
    @Test
    public void cardPurchasingTest() throws NoSuchFieldException, IllegalAccessException{
        assertTrue(ccp4.placeFigures(player1, 1));
        Effect[] badInput1 = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.FOOD};
        Effect[] badInput2 = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE};
        Effect[] input = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.CLAY};

        //Add player1 resources
        player1.getPlayerBoard().giveEffect(List.of(input));

        assertEquals(ActionResult.FAILURE, ccp4.makeAction(player3, List.of(input), List.of(new Effect[0])));
        assertEquals(ActionResult.FAILURE, ccp4.makeAction(player1, List.of(badInput1), List.of(new Effect[0])));
        assertEquals(ActionResult.FAILURE, ccp4.makeAction(player1, List.of(badInput2), List.of(new Effect[0])));

        assertEquals(ActionResult.ACTION_DONE, ccp4.makeAction(player1, List.of(input), List.of(Effect.WOOD, Effect.WOOD)));

        //Test if we there won't be any card, after player bought it
        Optional<CivilizationCard> cardOnPlace4 = (Optional<CivilizationCard>) currentPlaceCard.get(ccp4);
        assertEquals(Optional.empty(), cardOnPlace4);

        //Place player3 figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, ccp3.tryToPlaceFigures(player3, 1));

        //Good input, but player3 have only one gold
        Effect[] goodInput = new Effect[]{Effect.GOLD, Effect.STONE, Effect.GOLD};
        player3.getPlayerBoard().giveEffect(List.of(Effect.STONE, Effect.GOLD));

        assertEquals(ActionResult.FAILURE, ccp3.makeAction(player3, List.of(goodInput), List.of(new Effect[0])));
    }

    @Test
    public void immediateEffectTest() throws NoSuchFieldException, IllegalAccessException {
        // Place figures on cardPlace 1
        assertTrue(ccp1.placeFigures(player1, 1));
        player1.getPlayerBoard().giveEffect(List.of(Effect.WOOD));

        Optional<CivilizationCard> card = (Optional<CivilizationCard>) currentPlaceCard.get(ccp1);
        List<ImmediateEffect> immediateEffects = card.get().getImmediateEffectType();

        for (ImmediateEffect effect : immediateEffects) {
            switch (effect) {
                case FOOD:
                    // Remove all starter food (12 points)
                    for (int i = 0; i < 12; i++) {
                        assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                    }
                    assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                    assertEquals(ActionResult.ACTION_DONE, ccp1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                    for (int i = 0; i < immediateEffects.size(); i++) {
                        assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                    }
                    assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.FOOD)));
                    break;
                case WOOD, CLAY, STONE, GOLD:
                    assertEquals(ActionResult.ACTION_DONE, ccp1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                    Effect resourceToTest = switch (effect) {
                        case WOOD -> Effect.WOOD;
                        case CLAY -> Effect.CLAY;
                        case STONE -> Effect.STONE;
                        case GOLD -> Effect.GOLD;
                        default -> throw new IllegalStateException("Unexpected value: " + effect);
                    };
                    for (int i = 0; i < immediateEffects.size(); i++) {
                        assertTrue(player1.getPlayerBoard().takeResources(List.of(resourceToTest)));
                    }
                    assertFalse(player1.getPlayerBoard().takeResources(List.of(resourceToTest)));
                    break;
                case OneTimeTool2, OneTimeTool3, OneTimeTool4:
                    assertEquals(ActionResult.ACTION_DONE, ccp1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                    switch (effect) {
                        case OneTimeTool2 -> assertTrue(player1.getPlayerBoard().hasSufficientTools(2));
                        case OneTimeTool3 -> assertTrue(player1.getPlayerBoard().hasSufficientTools(3));
                        case OneTimeTool4 -> assertTrue(player1.getPlayerBoard().hasSufficientTools(4));
                    }
                    break;
                case POINT:
                    assertEquals(ActionResult.ACTION_DONE, ccp1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                    PlayerBoardGameBoardFacade facade = (PlayerBoardGameBoardFacade) player1.getPlayerBoard();
                    Field playerBoardField = PlayerBoardGameBoardFacade.class.getDeclaredField("playerBoard");
                    playerBoardField.setAccessible(true);
                    Field pointsField = PlayerBoard.class.getDeclaredField("points");
                    pointsField.setAccessible(true);
                    PlayerBoard playerBoard = (PlayerBoard) playerBoardField.get(facade);
                    int points = (int) pointsField.get(playerBoard);
                    assertEquals(3, points);
                    break;
                case ThrowGold, ThrowStone, ThrowWood, ThrowClay:
                    assertEquals(ActionResult.ACTION_DONE, ccp1.makeAction(player1, List.of(Effect.WOOD), List.of(new Effect[0])));
                    Field performerField = CivilizationCardPlace.class.getDeclaredField("performer");
                    performerField.setAccessible(true);
                    Field sumField = GetSomethingThrow.class.getDeclaredField("sum");
                    sumField.setAccessible(true);
                    GetSomethingThrow performer = (GetSomethingThrow) performerField.get(ccp1);
                    int sum = (int) sumField.get(performer);
                    Effect resource = switch (effect) {
                        case ThrowWood -> Effect.WOOD;
                        case ThrowClay -> Effect.CLAY;
                        case ThrowStone -> Effect.STONE;
                        case ThrowGold -> Effect.GOLD;
                        default -> throw new IllegalStateException("Unexpected value: " + effect);
                    };
                    for (int i = 0; i < sum / resource.points(); i++) {
                        assertTrue(player1.getPlayerBoard().takeResources(List.of(resource)));
                    }
                    assertFalse(player1.getPlayerBoard().takeResources(List.of(resource)));
                    break;
                case ArbitraryResource:
                    assertEquals(ActionResult.ACTION_DONE, ccp1.makeAction(player1, List.of(Effect.WOOD), List.of(Effect.GOLD, Effect.GOLD)));
                    for (int i = 0; i < 2; i++) {
                        assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.GOLD)));
                    }
                    assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.GOLD)));
                    break;
                case AllPlayersTakeReward:
                    // Implement the test for AllPlayersTakeReward if needed
                    break;
            }
        }
    }

    @Test
    public void endOfGameEffectTest() throws NoSuchFieldException, IllegalAccessException {
        // Initialize CivilizationCardPlace chain
        ccp4 = new CivilizationCardPlace(null, customDeck, 4);
        ccp3 = new CivilizationCardPlace(ccp4, customDeck, 3);
        ccp2 = new CivilizationCardPlace(ccp3, customDeck, 2);
        ccp1 = new CivilizationCardPlace(ccp2, customDeck, 1);

        // Place figures on ccp1 and apply initial effect to player board
        assertTrue(ccp1.placeFigures(player2, 1));
        player2.getPlayerBoard().giveEffect(List.of(Effect.WOOD));

        // Access the card from the current place
        Optional<CivilizationCard> card = (Optional<CivilizationCard>) currentPlaceCard.get(ccp1);

        // Access the end-of-game effects map and other necessary fields via reflection
        PlayerBoardGameBoardFacade playerBoardFacade = (PlayerBoardGameBoardFacade) player2.getPlayerBoard();
        sk.uniba.fmph.dcs.player_board.PlayerBoard playerBoard = getPrivateField(playerBoardFacade, "playerBoard");
        PlayerCivilizationCards playerCivilizationCards = getPrivateField(playerBoard, "playerCivilisationCards");
        Map<EndOfGameEffect, Integer> effects = getPrivateField(playerCivilizationCards, "endOfGameEffectMap");
        Stack<CivilizationCard> deckCards = getPrivateField(customDeck, "cardsDeck");

        // Combine end-of-game effects from current and top deck cards
        CivilizationCard topCard = deckCards.peek();
        ArrayList<EndOfGameEffect> endEffects = new ArrayList<>(card.get().getEndOfGameEffectType());
        endEffects.addAll(topCard.getEndOfGameEffectType());

        // Validate initial state
        assertTrue(effects.isEmpty());

        // Perform player action
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, ccp1.tryToMakeAction(player2));
        assertEquals(ActionResult.ACTION_DONE, ccp1.makeAction(player2, List.of(Effect.WOOD), List.of(Effect.WOOD, Effect.WOOD)));

        // Verify effects are added to the player board
        effects = getPrivateField(playerCivilizationCards, "endOfGameEffectMap");
        assertFalse(effects.isEmpty());

        // Validate that all expected effects were added
        for (EndOfGameEffect end : endEffects) {
            int val = effects.get(end);
            if (val - 1 == 0) {
                effects.remove(end);
            } else {
                effects.put(end, val - 1);
            }
        }
        assertTrue(effects.isEmpty());
    }

    // Utility method to get private fields using reflection
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }

}
