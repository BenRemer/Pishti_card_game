import javafx.scene.image.Image;
import java.util.ArrayList;

/**
 * Created by Ben Remer on 3/23/2017.
 */
public class Player
{
    private ArrayList<Image> hand = new ArrayList<>();
    private ArrayList<Integer> handNumber = new ArrayList<>();
    Player(Deck deck, int one, int two, int three, int four)
    {
        Image card1 = new Image("card/" + deck.cardNumber(one) + ".png");
        Image card2 = new Image("card/" + deck.cardNumber(two) + ".png");
        Image card3 = new Image("card/" + deck.cardNumber(three) + ".png");
        Image card4 = new Image("card/" + deck.cardNumber(four) + ".png");

        hand.add(card1);
        hand.add(card2);
        hand.add(card3);
        hand.add(card4);

        handNumber.add(deck.getCards(one).getNumber());
        handNumber.add(deck.getCards(two).getNumber());
        handNumber.add(deck.getCards(three).getNumber());
        handNumber.add(deck.getCards(four).getNumber());
    }

    public ArrayList getHand()
    {
        return hand;
    }

    public int getHandNumber(int number)
    {
        return handNumber.get(number);
    }

    public void setHandNumber(int where, int number)
    {
        handNumber.set(where, number);
    }
}
