import java.util.ArrayList;

/**
 * Created by Ben Remer on 3/22/2017.
 */
public class Deck
{
    private ArrayList<Card> cards = new ArrayList<>();
    private Card card;

    Deck()
    {
        for (int count = 0; count < 52; count++)
        {
            card = new Card(count + 1);
            cards.add(card);
        }
        cards.trimToSize();
    }

    public Card getCards(int number)
    {
        return cards.get(number);
    }

    public ArrayList<Card> getCards()
    {
        return cards;
    }

    public ArrayList shuffleCards()
    {
        java.util.Collections.shuffle(cards);
        return cards;
    }

    public int cardNumber(int number)
    {
        return cards.get(number).getNumber();
    }

}
