import java.util.List;
import java.util.stream.Collectors;

/**
 * The ElevensBoard class represents the board in a game of Elevens.
 */
public class ElevensBoard extends Board {

    /**
     * The size (number of cards) on the board.
     */
    private static final int BOARD_SIZE = 9;

    /**
     * The ranks of the cards for this game to be sent to the deck.
     */
    private static final String[] RANKS =
            {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

    /**
     * The suits of the cards for this game to be sent to the deck.
     */
    private static final String[] SUITS =
            {"spades", "hearts", "diamonds", "clubs"};

    /**
     * The values of the cards for this game to be sent to the deck.
     */
    private static final int[] POINT_VALUES =
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0, 0};

    /**
     * Flag used to control debugging print statements.
     */
    private static final boolean I_AM_DEBUGGING = false;


    /**
     * Creates a new <code>ElevensBoard</code> instance.
     */
    public ElevensBoard() {
        super(BOARD_SIZE, RANKS, SUITS, POINT_VALUES);
    }

    /**
     * Determines if the selected cards form a valid group for removal.
     * In Elevens, the legal groups are (1) a pair of non-face cards
     * whose values add to 11, and (2) a group of three cards consisting of
     * a jack, a queen, and a king in some order.
     * @param selectedCards the list of the indices of the selected cards.
     * @return true if the selected cards form a valid group for removal;
     *         false otherwise.
     */
    @Override
    public boolean isLegal(List<Integer> selectedCards) {
        List<Card> cards = selectedCards.stream().map(v -> cardAt(v)).collect(Collectors.toList());
        List<String> cardRanks = cards.stream().map(v -> v.rank()).collect(Collectors.toList());

        return (selectedCards.size() == 2 && !this.containsJQK(selectedCards) && (cards.stream().mapToInt(v -> v.pointValue()).sum() == 10 || cards.stream().mapToInt(v -> v.pointValue()).sum() == 13)) ||
                (selectedCards.size() == 3 && cardRanks.contains("jack") && cardRanks.contains("queen") && cardRanks.contains("king"));
    }

    /**
     * Determine if there are any legal plays left on the board.
     * In Elevens, there is a legal play if the board contains
     * (1) a pair of non-face cards whose values add to 11, or (2) a group
     * of three cards consisting of a jack, a queen, and a king in some order.
     * @return true if there is a legal play left on the board;
     *         false otherwise.
     */
    @Override
    public boolean anotherPlayIsPossible() {
        List<Card> cards = this.cardIndexes().stream().map(v -> cardAt(v)).collect(Collectors.toList());
        List<String> cardRanks = cards.stream().map(v -> v.rank()).collect(Collectors.toList());

        return (cardRanks.contains("jack") && cardRanks.contains("queen") && cardRanks.contains("king")) ||
                this.containsPairSum11(this.cardIndexes());
    }

    /**
     * Check for an 11-pair in the selected cards.
     * @param selectedCards selects a subset of this board.  It is list
     *                      of indexes into this board that are searched
     *                      to find an 11-pair.
     * @return true if the board entries in selectedCards
     *              contain an 11-pair; false otherwise.
     */
    private boolean containsPairSum11(List<Integer> selectedCards) {
        List<Integer> cards = selectedCards.stream().map(v -> cardAt(v).pointValue()).collect(Collectors.toList());

        for (int x = 0; x < cards.size(); x++) {
            for (int y = x + 1; y < cards.size(); y++) {
                if (cards.get(x) != 0 && cards.get(y) != 0 && cards.get(x) + cards.get(y) == 10 || cards.get(x) + cards.get(y) == 13) return true;
            }
        }
        return false;
    }

    /**
     * Check for a JQK in the selected cards.
     * @param selectedCards selects a subset of this board.  It is list
     *                      of indexes into this board that are searched
     *                      to find a JQK group.
     * @return true if the board entries in selectedCards
     *              include a jack, a queen, and a king; false otherwise.
     */
    private boolean containsJQK(List<Integer> selectedCards) {
        return selectedCards.stream().map(v -> cardAt(v)).filter(v -> v.rank() == "jack" ||
                v.rank() == "queen" ||
                v.rank() == "king").count() > 0;
    }
}
