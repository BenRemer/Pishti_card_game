import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Created by Ben Remer on 3/22/2017.
 */
public class Pishti extends Application
{
    private GridPane top;
    private GridPane bottom;
    private BorderPane pane;
    private ArrayList<Image> playerHand;
    private ArrayList<Image> botHand;
    private Deck deck;
    private Player player;
    private Player bot;
    private Rectangle table;
    private Image blankCard = new Image("card/b1fv.png");
    private Stack stack;
    private StackPane flow;
    private StackPane left;
    private StackPane centerStack;
    private ImageView card1;
    private ImageView card2;
    private ImageView card3;
    private ImageView card4;
    private int topCard;
    private int lastCard;
    private ArrayList<Integer> onTable = new ArrayList<>();
    private ArrayList<Integer> playerWin = new ArrayList<>();
    private ArrayList<Integer> botWin = new ArrayList<>();
    private int playerPishti = 0;
    private int botPishti = 0;
    private int playerScore = 0;
    private int botScore = 0;
    private int moved = 0;
    private int playerNumber = 0;
    private Pane cPane1 = new Pane();
    private Pane cPane2 = new Pane();
    private Pane cPane3 = new Pane();
    private Pane cPane4 = new Pane();
    private int moveNumber = 48;
    private boolean playerLastWin;
    private Text winner;
    private Button btn;
    private BorderPane finalScreen = new BorderPane();
    private final int JACK_OF_SPADES = 11;
    private final int JACK_OF_HEARTS = 24;
    private final int JACK_OF_DIAMONDS = 37;
    private final int JACK_OF_CLUBS = 50;
    private final int TWO_OF_CLUBS = 41;
    private final int TEN_OF_DIAMONDS = 36;
    private boolean gameOver = false;

    public void start(Stage primaryScene)
    {
        /* Make the GUI  */
        top = new GridPane();
        bottom = new GridPane();
        pane = new BorderPane();
        flow = new StackPane();
        left = new StackPane();
        centerStack = new StackPane();
        top.setAlignment(Pos.CENTER);
        bottom.setAlignment(Pos.CENTER);

        top.setVgap(5.5);
        top.setHgap(5.5);
        bottom.setHgap(5.5);
        bottom.setVgap(5.5);

        pane.setPadding(new Insets(11,11,11,11));

        table = new Rectangle(450,200);
        table.setStroke(Color.BLACK);
        table.setFill(Color.BURLYWOOD);
        flow.getChildren().add(table);
        left.setAlignment(Pos.CENTER);
        left.setPadding(new Insets(0,0,0,350));
        flow.getChildren().add(left);
        flow.setAlignment(Pos.CENTER);
        flow.getChildren().add(centerStack);

        deck = new Deck(); //Make Deck
        deck.shuffleCards(); //Shuffle Deck
        stack = new Stack(deck); //Make tangible Stack if cards

        makeStack(); // make the deck accessible
        makePlayerHand(); //Make player hand
        makeBotHand(); //Make bot hand
        makeCenterStack(); //Make center cards

        /* Check if the top card will be a Jack, if so reshuffle */
        if (stack.getCardNumber(3) == JACK_OF_SPADES || stack.getCardNumber(3) == JACK_OF_HEARTS ||
                stack.getCardNumber(3) == JACK_OF_DIAMONDS || stack.getCardNumber(3) == JACK_OF_CLUBS)
        {
            deck.shuffleCards();
            stack = new Stack(deck);
            makeStack();
            makePlayerHand();
            makeBotHand();
            makeCenterStack();
        }

        /* Add player hand to bottom of screen */
        bottom.add(cPane1, 3, 4);
        bottom.add(cPane2, 4, 4);
        bottom.add(cPane3, 5, 4);
        bottom.add(cPane4, 6, 4);

        /* Set GUI to screen */
        pane.setTop(top);
        pane.setCenter(flow);
        pane.setBottom(bottom);

        /* Check if card is clicked */
        cPane1.setOnMouseClicked(event ->
        {
            moveCard(card1,0); //Move player card to center
            botMove(); // AI play
            moved++; //Add one to 'moved' counter
            if (moved == 4) //Checks to see if hand is gone
            {
                playerNumber += 8; //used to make new hand
                removeMoveNumber(); //subtract to signify a new hand is dealt
                checkWin(); //Check if game is over
                if (!gameOver) //If more cards are available
                {
                    makePlayerHand(); //Make new hand
                    makeBotHand(); //Make new bot hand
                    left.getChildren().remove(0, 7); //Used to see if game is over
                    moved = 0; //Reset how many moves have been made
                }
            }
        });

        /* Same as above */
        cPane2.setOnMouseClicked(event ->
        {
            moveCard(card2, 1);
            botMove();
            moved++;
            if (moved == 4)
            {
                playerNumber += 8;
                removeMoveNumber();
                checkWin();
                if (!gameOver)
                {
                    makePlayerHand();
                    makeBotHand();
                    left.getChildren().remove(0, 7);
                    moved = 0;
                }
            }
        });

        /* Same as above */
        cPane3.setOnMouseClicked(event ->
        {
            moveCard(card3, 2);
            botMove();
            moved++;
            if (moved == 4)
            {
                playerNumber += 8;
                removeMoveNumber();
                checkWin();
                if (!gameOver)
                {
                    makePlayerHand();
                    makeBotHand();
                    left.getChildren().remove(0, 7);
                    moved = 0;
                }
            }
        });

        /* Same as above */
        cPane4.setOnMouseClicked(event ->
        {
            moveCard(card4, 3);
            botMove();
            moved++;
            if (moved == 4)
            {
                playerNumber += 8;
                removeMoveNumber();
                checkWin();
                if (!gameOver)
                {
                    makePlayerHand();
                    makeBotHand();
                    left.getChildren().remove(0, 7);
                    moved = 0;
                }
            }
        });

        /* Make and display scene */
        Scene scene = new Scene(pane, 1000, 600);
        primaryScene.setTitle("Pishti");
        primaryScene.setScene(scene);
        primaryScene.show();
    }

    private void makePlayerHand() //Makes a hand for the Player
    {
        player = new Player(deck, playerNumber, playerNumber + 2, playerNumber + 4, playerNumber + 6); //Makes player with hand
        playerHand = player.getHand(); //Makes the hand
        card1 = new ImageView(playerHand.get(0)); //Image for Card 1
        card2 = new ImageView(playerHand.get(1)); //Image for Card 2
        card3 = new ImageView(playerHand.get(2)); //Image for Card 3
        card4 = new ImageView(playerHand.get(3)); //Image for Card 4

        /* Clears panes */
        cPane1.getChildren().clear();
        cPane2.getChildren().clear();
        cPane3.getChildren().clear();
        cPane4.getChildren().clear();

        /* Adds cards to separate panes */
        cPane1.getChildren().add(card1);
        cPane2.getChildren().add(card2);
        cPane3.getChildren().add(card3);
        cPane4.getChildren().add(card4);

    }

    private void makeBotHand() //Makes a hand for the bot
    {
        bot = new Player(deck, playerNumber + 1, playerNumber + 3, playerNumber + 5, playerNumber + 7); //Makes bot player with cards
        botHand = bot.getHand(); //makes bots hand

        /* Adds hidden cards to top row */
        top.add(new ImageView(blankCard), 3, 0);
        top.add(new ImageView(blankCard), 4, 0);
        top.add(new ImageView(blankCard), 5, 0);
        top.add(new ImageView(blankCard), 6, 0);
    }

    private void makeStack() //Makes the stack, which is used to see if there are cards in deck
    {
        /* Makes 48 cards in the stack b/c 4 are already on the table */
        for (int count = 0; count < 48; count++)
            left.getChildren().add(new ImageView(blankCard));
    }

    private void makeCenterStack() //Make the cards in the center
    {
        /* Remove four cards already on table from the stack */
        for (int count = 0; count < 4; count++)
            stack.getCardsLeft().remove(0);

        onTable.add(stack.getCardNumber(0)); //Adds card number to 'onTable'
        onTable.add(stack.getCardNumber(1)); //Adds card number to 'onTable'
        onTable.add(stack.getCardNumber(2)); //Adds card number to 'onTable'
        onTable.add(stack.getCardNumber(3)); //Adds card number to 'onTable'

        centerStack.getChildren().add(new ImageView(blankCard)); //Adds new hidden card to stack
        centerStack.getChildren().add(new ImageView(blankCard)); //Adds new hidden card to stack
        centerStack.getChildren().add(new ImageView(blankCard)); //Adds new hidden card to stack
        Image top = new Image("card/" + stack.getCardNumber(3) + ".png"); //Makes image of fourth card
        centerStack.getChildren().add(new ImageView(top)); //Adds fourth card to top of stack
        topCard = stack.getCardNumber(3); //Makes 'topCard' = to the fourth card
    }

    private void moveCard(ImageView card, int number) //Moves the clicked card to center
    {
        centerStack.getChildren().add(card); //Add new card to the center stack
        lastCard = topCard; //Makes the old topCard the lastCard
        topCard = player.getHandNumber(number); //New topCard = card being moved
        bottom.getChildren().remove(card); //Remove the card from players hand
        onTable.add(player.getHandNumber(number)); //Add card number to 'onTable'

        /* Checks if card is the same */
        if (topCard == (lastCard + 13) || topCard == (lastCard + 26) || topCard == (lastCard + 39) ||
                topCard == (lastCard - 13) || topCard == (lastCard - 26) || topCard == (lastCard - 39))
        {
            /* If there is only one other card, add points to Pishti */
            if (onTable.size() == 2)
                playerPishti++;
            for (Integer anOnTable : onTable) playerWin.add(anOnTable); //Add numbers from 'onTable' to the playerWin
            onTable.clear(); //Clear numbers from 'onTable'
            centerStack.getChildren().clear(); //Clear cards from centerStack
            pane.setLeft(new ImageView(blankCard)); //Add hidden card to left showing player has a point
            topCard = -140; //Set topCard to very low number as to not have any cards in play
            lastCard = -140; //Set lastCard to very low number as to not have any cards in play
            playerLastWin = true; //Set for if its the last win, player gets the extra cards
        }
        else if (lastCard != -140 && (topCard == JACK_OF_SPADES || topCard == JACK_OF_HEARTS ||
                topCard == JACK_OF_DIAMONDS || topCard == JACK_OF_CLUBS)) //Checks  if the card is a Jack and another card is on the table
        {
            /* If only one other card on the table */
            if (onTable.size() == 2)
                if (lastCard == JACK_OF_SPADES || lastCard == JACK_OF_HEARTS || lastCard == JACK_OF_DIAMONDS ||
                        lastCard == JACK_OF_CLUBS) //If that last card was a Jack add two to Pishti score
                    playerPishti += 2;
            for (Integer anOnTable : onTable) //Add numbers from 'ontable' to 'playerWin' ArrayList
                playerWin.add(anOnTable);
            onTable.clear(); //Clears 'onTable'
            centerStack.getChildren().clear(); //Removes cards from CenterStack
            pane.setLeft(new ImageView(blankCard)); //Adds hidden card to side
            topCard = -140; //Set topCard to very low number as to not have any cards in play
            lastCard = -140; //Set lastCard to very low number as to not have any cards in play
            playerLastWin = true; //Set for if its the last win, player gets the extra cards
        }
    }

    private void botMove() //Bot AI moves
    {
        /* Makes bots hand easier to control */
        int botCard1 = bot.getHandNumber(0);
        int botCard2 = bot.getHandNumber(1);
        int botCard3 = bot.getHandNumber(2);
        int botCard4 = bot.getHandNumber(3);

        /* If the card is still in play and is the same as the card on top */
        if (botCard1 != -1 && (botCard1 == (topCard + 13) || botCard1 == (topCard + 26) || 
                botCard1 == (topCard + 39) || botCard1 == (topCard - 13) || botCard1 == (topCard - 26) || 
                botCard1 == (topCard - 39)))
        {
            onTable.add(botCard1); //Add card to table
            for (Integer anOnTable : onTable) botWin.add(anOnTable); //add table to botWin

            onTable.clear(); //clear table
            centerStack.getChildren().clear(); //clear center
            top.getChildren().remove(0); //remove one card from bot hand
            bot.setHandNumber(0, -1); //Take card out of play by setting it to -1
            pane.setRight(new ImageView(blankCard)); //Add hidden card to right to show bot has a point
            topCard = -140; //set top card to low number showing there is no top card
            playerLastWin = false; //bot won last hand
        }
        /*Else if second card is in play and is same as top card */
        else if (botCard2 != -1 && (botCard2 == (topCard + 13) || botCard2 == (topCard + 26) ||
                botCard2 == (topCard + 39) || botCard2 == (topCard - 13) || botCard2 == (topCard - 26) || 
                botCard2 == (topCard - 39)))
        {
            /* Same as above */
            onTable.add(botCard2);
            for (Integer anOnTable : onTable) botWin.add(anOnTable);

            onTable.clear();
            centerStack.getChildren().clear();
            top.getChildren().remove(0);
            bot.setHandNumber(1, -1);
            pane.setRight(new ImageView(blankCard));
            topCard = -140;
            playerLastWin = false;
        }
        /*Else if third card is in play and is same as top card */
        else if (botCard3 != -1 && (botCard3 == (topCard + 13) || botCard3 == (topCard + 26) ||
                botCard3 == (topCard + 39) || botCard3 == (topCard - 13) || botCard3 == (topCard - 26) || 
                botCard3 == (topCard - 39)))
        {
            /* Same as above */
            onTable.add(botCard3);
            for (Integer anOnTable : onTable) botWin.add(anOnTable);

            onTable.clear();
            centerStack.getChildren().clear();
            top.getChildren().remove(0);
            bot.setHandNumber(2, -1);
            pane.setRight(new ImageView(blankCard));
            topCard = -140;
            playerLastWin = false;
        }
        /*Else if fourth card is in play and is same as top card */
        else if (botCard4 != -1 && (botCard4 == (topCard + 13) || botCard4 == (topCard + 26) ||
                botCard4 == (topCard + 39) || botCard4 == (topCard - 13) || botCard4 == (topCard - 26) ||
                botCard4 == (topCard - 39)))
        {
            /* Same as above */
            onTable.add(botCard4);
            for (Integer anOnTable : onTable) botWin.add(anOnTable);

            onTable.clear();
            centerStack.getChildren().clear();
            top.getChildren().remove(0);
            bot.setHandNumber(3, -1);
            pane.setRight(new ImageView(blankCard));
            topCard = -140;
            playerLastWin = false;
        }
        else
        {
            if (topCard == -140) //If there is a top card
            {
                if (botCard1 != -1) //Card one is in play
                {
                    centerStack.getChildren().add(new ImageView(botHand.get(0))); //Add card to center
                    topCard = botCard1; //Make card number the topCard
                    onTable.add(botCard1); //Add card to 'onTable'
                    top.getChildren().remove(0); //Remove card from bots hand
                    bot.setHandNumber(0, -1); //Set card out of play
                }
                else if (botCard2 != -1) //Card two is in play
                {
                    /* Same as above */
                    centerStack.getChildren().add(new ImageView(botHand.get(1)));
                    topCard = botCard2;
                    onTable.add(botCard2);
                    top.getChildren().remove(0);
                    bot.setHandNumber(1, -1);
                }
                else if (botCard3 != -1) //Card three is in play
                {
                    /* Same as above */
                    centerStack.getChildren().add(new ImageView(botHand.get(2)));
                    topCard = botCard3;
                    onTable.add(botCard3);
                    top.getChildren().remove(0);
                    bot.setHandNumber(2, -1);
                }
                else if (botCard4 != -1) //Card four is in play
                {
                    /* Same as above */
                    centerStack.getChildren().add(new ImageView(botHand.get(3)));
                    topCard = botCard4;
                    onTable.add(bot.getHandNumber(3));
                    top.getChildren().remove(0);
                    bot.setHandNumber(3, -1);
                }
            }
            else if ((botCard1 == JACK_OF_SPADES || botCard1 == JACK_OF_HEARTS || botCard1 == JACK_OF_DIAMONDS ||
                    botCard1 == JACK_OF_CLUBS)) //If card one is a Jack
            {
                onTable.add(bot.getHandNumber(0)); //Add card ti table
                for (Integer anOnTable : onTable) botWin.add(anOnTable); //Add table numbers to botWin array

                onTable.clear(); //Clear table
                centerStack.getChildren().clear(); //Clear center images
                top.getChildren().remove(0); //Remove card from bots hand
                bot.setHandNumber(0, -1); //Take card out of play
                pane.setRight(new ImageView(blankCard)); //Add card to bot win
                topCard = -140; //Set card to low number to signify there is no top card
                playerLastWin = false; //Bot won last hand
            }
            /* Same as above */
            else if ((botCard2 == JACK_OF_SPADES || botCard2 == JACK_OF_HEARTS || botCard2 == JACK_OF_DIAMONDS ||
                    botCard2 == JACK_OF_CLUBS))
            {
                onTable.add(bot.getHandNumber(1));
                for (Integer anOnTable : onTable) botWin.add(anOnTable);

                onTable.clear();
                centerStack.getChildren().clear();
                top.getChildren().remove(0);
                bot.setHandNumber(1, -1);
                pane.setRight(new ImageView(blankCard));
                topCard = -140;
                playerLastWin = false;
            }
            /* Same as above */
            else if ((botCard3 == JACK_OF_SPADES || botCard3 == JACK_OF_HEARTS || botCard3 == JACK_OF_DIAMONDS ||
                    botCard3 == JACK_OF_CLUBS))
            {
                onTable.add(bot.getHandNumber(2));
                for (Integer anOnTable : onTable) botWin.add(anOnTable);

                onTable.clear();
                centerStack.getChildren().clear();
                top.getChildren().remove(0);
                bot.setHandNumber(2, -1);
                pane.setRight(new ImageView(blankCard));
                topCard = -140;
                playerLastWin = false;
            }
            /* Same as above */
            else if ((botCard4 == JACK_OF_SPADES || botCard4 == JACK_OF_HEARTS || botCard4 == JACK_OF_DIAMONDS ||
                    botCard4 == JACK_OF_CLUBS))
            {
                onTable.add(bot.getHandNumber(3));
                for (Integer anOnTable : onTable) botWin.add(anOnTable);

                onTable.clear();
                centerStack.getChildren().clear();
                top.getChildren().remove(0);
                bot.setHandNumber(3, -1);
                pane.setRight(new ImageView(blankCard));
                topCard = -140;
                playerLastWin = false;
            }
            else
            {
                if (botCard1 != -1) //If card is in play
                {
                    centerStack.getChildren().add(new ImageView(botHand.get(0))); //Add it to stack
                    topCard = botCard1; //Make 'topCard' = card number
                    onTable.add(bot.getHandNumber(0)); //Add card number to table
                    top.getChildren().remove(0); //Remove card from bot hand
                    bot.setHandNumber(0, -1); //Take card out of play
                }
                /* Same as above */
                else if (botCard2 != -1)
                {
                    centerStack.getChildren().add(new ImageView(botHand.get(1)));
                    topCard = botCard2;
                    onTable.add(bot.getHandNumber(1));
                    top.getChildren().remove(0);
                    bot.setHandNumber(1, -1);
                }
                /* Same as above */
                else if (botCard3 != -1)
                {
                    centerStack.getChildren().add(new ImageView(botHand.get(2)));
                    topCard = botCard3;
                    onTable.add(bot.getHandNumber(2));
                    top.getChildren().remove(0);
                    bot.setHandNumber(2, -1);
                }
                /* Same as above */
                else if (botCard4 != -1)
                {
                    centerStack.getChildren().add(new ImageView(botHand.get(3)));
                    topCard = botCard4;
                    onTable.add(bot.getHandNumber(3));
                    top.getChildren().remove(0);
                    bot.setHandNumber(3, -1);
                }
            }
        }

    }

    private void removeMoveNumber()
    {
        moveNumber -= 8; //Take 8 away from 'moveNumber' to signify a turn has been done
    }

    private void checkWin() //Checks if game is over
    {
        if (moveNumber == 8) //If there is only one more hand
            flow.getChildren().remove(left); //Remove deck from GUI
        else if (moveNumber == 0) //If last hand
        {
            gameOver = true; //Shows there are no more turns
            if (playerLastWin) //If player won the last hand
            {
                for (Integer anOnTable : onTable) playerWin.add(anOnTable); //Add table to playerWin
                onTable.clear(); //Clear table
                centerStack.getChildren().clear(); //Clear center images
                pane.setLeft(new ImageView(blankCard)); //Add hidden card to left
            }
            else
            {
                for (Integer anOnTable : onTable) botWin.add(anOnTable); //Add table to botWin
                onTable.clear(); //Clear table
                centerStack.getChildren().clear(); //Clear center images
                pane.setRight(new ImageView(blankCard)); //Add hidden card to right
            }
            getScores(); //Calculate Scores for each player
        }
    }

    private void getScores() //Figures out score for each player
    {
        int Ten = 10;
        int Jack = 11;
        int Queen = 12;
        int King = 13;
        int Ace = 1;

        for (int count = 0; count < 4; count++) //Find cards for each suit
        {
            /* If card is in player/bot win Array, add points to player/bot */
            if (playerWin.contains(Ten))
                playerScore++;
            if (playerWin.contains(Jack))
                playerScore++;
            if (playerWin.contains(Queen))
                playerScore++;
            if (playerWin.contains(King))
                playerScore++;
            if (playerWin.contains(Ace))
                playerScore++;

            if (botWin.contains(Ten))
                botScore++;
            if (botWin.contains(Jack))
                botScore++;
            if (botWin.contains(Queen))
                botScore++;
            if (botWin.contains(King))
                botScore++;
            if (botWin.contains(Ace))
                botScore++;
            /* Make numbers the next suit */
            Ten += 13;
            Jack += 13;
            Queen += 13;
            King += 13;
            Ace += 13;
        }

        if (playerWin.contains(TWO_OF_CLUBS)) //Checks if player has two of clubs
            playerScore += 2;
        else //If not, bot has two of clubs
            botScore += 2;

        if (playerWin.contains(TEN_OF_DIAMONDS)) //If player has ten of diamonds
            playerScore += 2;
        else //If not, bot has ten of diamonds
            botScore += 3;

        if (playerWin.size() > botWin.size()) //If player has more cards add points
            playerScore += 3;
        else //Else, bot has more cards so it gets points
            botScore += 3;

        playerScore += (playerPishti * 10); //Add ten points for each Pishti
        botScore += (botPishti * 10); //Add ten points for each Pishti

        displayWinner(); //Display who won
    }

    private void displayWinner() //Displays the winner
    {
        if (playerScore > botScore) //If player won
        {
            pane.getChildren().clear(); //Clear pane
            winner = new Text("Player wins with " + playerScore + " points!"); //Add text
            winner.setUnderline(true); //Underline text
            winner.setStroke(Color.BLACK); //Set text color to black
            winner.setScaleX(3); //Set text size
            winner.setScaleY(3); //Set text size
            btn = new Button("Thanks for playing!"); //Add text
            btn.setAlignment(Pos.CENTER); //Add button
            finalScreen.setTop(winner); //Set who won to top of pane
            finalScreen.setBottom(btn); //Set button to bottom
            finalScreen.setPadding(new Insets(200,0,200,450)); //Add Padding to center
            /* Checks if button is pressed */
            btn.setOnMouseClicked(event -> {
                System.exit(0); //Quit program
            });

            pane.setCenter(finalScreen); //Add finalScreen to the pane
        }
        /* Same as above */
        else if (botScore > playerScore)
        {
            pane.getChildren().clear();
            winner = new Text("Bot wins with " + botScore + " points, better luck next time.");
            winner.setUnderline(true);
            winner.setStroke(Color.BLACK);
            winner.setScaleX(3);
            winner.setScaleY(3);
            btn = new Button("Thanks for playing!");
            btn.setAlignment(Pos.CENTER);
            finalScreen.setTop(winner);
            finalScreen.setBottom(btn);
            finalScreen.setPadding(new Insets(200,0,200,400));
            btn.setOnMouseClicked(event -> {
                System.exit(0);
            });

            pane.setCenter(finalScreen);
        }
        else
        {
            pane.getChildren().clear();
            winner = new Text("You tied with " + botScore + " points, better luck next time.");
            winner.setUnderline(true);
            winner.setStroke(Color.BLACK);
            winner.setScaleX(3);
            winner.setScaleY(3);
            btn = new Button("Thanks for playing!");
            btn.setAlignment(Pos.CENTER);
            finalScreen.setTop(winner);
            finalScreen.setBottom(btn);
            finalScreen.setPadding(new Insets(200,0,200,400));
            btn.setOnMouseClicked(event -> {
                System.exit(0);
            });

            pane.setCenter(finalScreen);
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    } //Makes everything launch correctly
}
