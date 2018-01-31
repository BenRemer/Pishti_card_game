import java.util.ArrayList;

/**
 * Created by Ben Remer on 3/23/2017.
 */
public class Stack
{
    private ArrayList<Card> cardsLeft;
    Stack(Deck deck)
    {
        cardsLeft = deck.getCards();
    }

    public ArrayList getCardsLeft()
    {
        return cardsLeft;
    }

    public int getCardNumber(int number)
    {
        return cardsLeft.get(number).getNumber();
    }

}
