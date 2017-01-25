
import java.io.File;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Together II
 * Battleship
 * Main
 *
 * @author Niwat Namisa, Patiphon Suriyawong, Preeti Yuankrathok, Panya Tangkanchanayeunyong
 */
public class Main extends Application {

    private static final double VERSION = 0.9;
    private static final int COLS = 10, ROWS = 10;

    private static int[] score = {0, 0};
    private static int[] mana = {0, 0};

    private static String imageLocation = "data/images/";
    private static String soundLocation = "data/sounds/";
    private static Image stark;
    private static Image menuBackgroundImage = new Image((new File(imageLocation + "menubg.jpg")).toURI().toString(), 1200, 650, false, true);
    private static Image backgroundImage = new Image((new File(imageLocation + "bgnew.jpg")).toURI().toString(), 1200, 650, false, true);
    private static Image youWinBackgroundImage = new Image((new File(imageLocation + "youwin.gif")).toURI().toString(), 650, 650, false, true);
    private static Image selectBackgroundImage = new Image((new File(imageLocation + "selectbg.jpg")).toURI().toString(), 1200, 650, false, true); // new
    private static Image normalWater = new Image((new File(imageLocation + "normalwater.jpg")).toURI().toString(), 40, 40, false, true);
    private static Image firedWater = new Image((new File(imageLocation + "firedwater.jpg")).toURI().toString(), 40, 40, false, true);
    private static Image missWater = new Image((new File(imageLocation + "misswater.jpg")).toURI().toString(), 40, 40, false, true);
    private static Image defWater = new Image((new File(imageLocation + "defwater.jpg")).toURI().toString(), 40, 40, false, true);
    private static Image bomb = new Image((new File(imageLocation + "bomb.gif")).toURI().toString(), 40, 40, false, true);
    private static Image menuTitle = new Image((new File(imageLocation + "menutitle.png")).toURI().toString(), 536, 85, false, true);
    private static Image title = new Image((new File(imageLocation + "menutitle.png")).toURI().toString(), 280, 60, false, true);
    private static Image manaImage = new Image((new File(imageLocation + "mana.png")).toURI().toString(), 30, 30, false, true);
    private static Image fish = new Image((new File(imageLocation + "fish.gif")).toURI().toString(), 400, 400, false, true);
    private static Image selectTitle = new Image((new File(imageLocation + "selecttitle.png")).toURI().toString(), 427, 63, false, true);
    private static Image[] turnCard = new Image[]{new Image((new File(imageLocation + "turnCard0.jpg")).toURI().toString(), 160, 280, false, true), new Image((new File(imageLocation + "turncard1.jpg")).toURI().toString(), 160, 280, false, true), new Image((new File(imageLocation + "turncard2.jpg")).toURI().toString(), 160, 280, false, true)}; // new
    private static AudioClip bombSound = new AudioClip(new File(soundLocation + "bomb.mp3").toURI().toString());
    private static AudioClip waterSound = new AudioClip(new File(soundLocation + "water.mp3").toURI().toString());
    private static AudioClip shieldSound = new AudioClip(new File(soundLocation + "shield.mp3").toURI().toString());
    private static AudioClip errorSound = new AudioClip(new File(soundLocation + "error.mp3").toURI().toString());
    private static AudioClip youWinSound = new AudioClip(new File(soundLocation + "youwin.mp3").toURI().toString());

    private static ImageView starkView = new ImageView();
    private static ImageView[][] manaView = new ImageView[2][10];

    private static int shipAmount = 5;
    private static String[] shipName = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
    private static int[] shipLength = {5, 4, 3, 3, 2};

    private static int cardAmount = 7;
    private static String[] cardSkill = {"2hit", "3hit", "rand5", "def", "gainmana", "stealmana", "clear"};
    private static int[] cardMana = {2, 3, 3, 3, 5, 5, 10};

    private static Scene menuScene, planScene, selectScene, mainScene; //new
    private static Pane mainMenu = new Pane();
    private static HBox planBox = new HBox();
    private static Pane parentPane = new Pane();
    private static Pane selectPane = new Pane(); //new
    private static VBox[] playerBox = new VBox[2];
    private static Text[] playerText = new Text[2];
    private static StackPane[] playerScore = new StackPane[2];
    private static Rectangle[] playerScoreRec = new Rectangle[2];
    private static Text[] scoreText = new Text[2];
    private static GridHolder[] player = new GridHolder[2];
    private static ImageView cardMiddleShow = new ImageView();
    private static boolean attacked = false;
    private static boolean turnSelected = false;

    private static int turn = 0;
    private static int turnCount = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("file:" + imageLocation + "icon.png"));

        // ----- MAINMENU PANE -----
        // initilize parent pane
        mainMenu = new Pane();
        BackgroundImage menuBgImage = new BackgroundImage(menuBackgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        mainMenu.setBackground(new Background(menuBgImage));

        // insert logo title
        ImageView menuTitleView = new ImageView();
        menuTitleView.setImage(menuTitle);
        menuTitleView.setX(70);
        menuTitleView.setY(180);
        mainMenu.getChildren().add(menuTitleView);

        // insert start button
        Text startText = new Text("press any key to continue..");
        startText.setFont(Font.font("Ubuntu", 20));
        startText.setFill(Color.WHITE);
        startText.setEffect(new DropShadow(40, Color.rgb(255, 255, 255, 0.8)));
        startText.setX(470);
        startText.setY(620);
        mainMenu.getChildren().add(startText);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), startText);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        // ----- SELECT PANE -----
        BackgroundImage selectBgImage = new BackgroundImage(selectBackgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        selectPane.setBackground(new Background(selectBgImage));

        ImageView selectTitleView = new ImageView();
        selectTitleView.setImage(selectTitle);
        selectTitleView.setX(386.5);
        selectTitleView.setY(70);

        ImageView[] turnCardView = new ImageView[]{new ImageView(), new ImageView()};
        turnCardView[0].setImage(turnCard[0]);
        turnCardView[1].setImage(turnCard[0]);
        turnCardView[0].setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.8)));
        turnCardView[1].setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.8)));
        turnCardView[0].setX(300);
        turnCardView[0].setY(200);
        turnCardView[1].setX(740);
        turnCardView[1].setY(200);
        turnCardView[0].setOnMouseClicked(e -> clickTurnCard(primaryStage, turnCardView[0]));
        turnCardView[1].setOnMouseClicked(e -> clickTurnCard(primaryStage, turnCardView[1]));

        selectPane.getChildren().add(selectTitleView);
        selectPane.getChildren().addAll(turnCardView);

        // ----- PARENT PANE -----
        // initilize parent pane
        BackgroundImage bgImage = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        parentPane.setBackground(new Background(bgImage));

        // initilize main pane
        HBox mainPane = new HBox();
        mainPane.setPadding(new Insets(40, 15, 15, 15));
        mainPane.setSpacing(25);

        // add main pane & cardmiddleshow to parent pane
        parentPane.getChildren().add(mainPane);
        parentPane.getChildren().add(cardMiddleShow);

        // initilize two grids
        player[0] = new GridHolder(0);
        player[1] = new GridHolder(1);
        player[0].getGridHolder().setPadding(new Insets(60, 0, 0, 0));
        player[1].getGridHolder().setPadding(new Insets(60, 0, 0, 0));

        // middle menu
        VBox menuPane = new VBox();
        menuPane.setMaxSize(260, 600);
        menuPane.setMinSize(260, 600);
        menuPane.setSpacing(25);
        menuPane.setAlignment(Pos.TOP_CENTER);
        menuPane.setPadding(new Insets(0, 0, 0, 60));

        // menu
        ImageView titleView = new ImageView();
        titleView.setImage(title);
        titleView.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.8)));
        menuPane.getChildren().add(titleView);

        HBox scoreBox = new HBox();
        scoreBox.setMaxWidth(260);
        scoreBox.setMinWidth(260);
        scoreBox.setSpacing(30);
        scoreBox.setAlignment(Pos.TOP_CENTER);
        scoreBox.setPadding(new Insets(70, 0, 0, 0));

        // Player 1
        playerBox[0] = new VBox();
        playerBox[0].setSpacing(5);
        playerBox[0].setAlignment(Pos.TOP_CENTER);

        playerText[0] = new Text("Player 1");
        playerText[0].setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        playerBox[0].getChildren().add(playerText[0]);

        playerScore[0] = new StackPane();

        playerScoreRec[0] = new Rectangle();
        playerScoreRec[0].setHeight(50);
        playerScoreRec[0].setWidth(70);
        playerScore[0].getChildren().add(playerScoreRec[0]);

        scoreText[0] = new Text(score[0] + "");
        scoreText[0].setFont(Font.font("Helvetica", FontWeight.BOLD, 40));
        scoreText[0].setFill(Color.WHITE);
        playerScore[0].getChildren().add(scoreText[0]);
        playerBox[0].getChildren().add(playerScore[0]);

        // Player 2
        playerBox[1] = new VBox();
        playerBox[1].setSpacing(5);
        playerBox[1].setAlignment(Pos.TOP_CENTER);

        playerText[1] = new Text("Player 2");
        playerText[1].setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        playerBox[1].getChildren().add(playerText[1]);

        playerScore[1] = new StackPane();

        playerScoreRec[1] = new Rectangle();
        playerScoreRec[1].setHeight(50);
        playerScoreRec[1].setWidth(70);
        playerScore[1].getChildren().add(playerScoreRec[1]);

        scoreText[1] = new Text(score[1] + "");
        scoreText[1].setFont(Font.font("Helvetica", FontWeight.BOLD, 40));
        scoreText[1].setFill(Color.WHITE);
        playerScore[1].getChildren().add(scoreText[1]);
        playerBox[1].getChildren().add(playerScore[1]);

        // MANA
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                manaView[i][j] = new ImageView();
                manaView[i][j].setImage(manaImage);
                manaView[i][j].setX(i * 300 + 435);
                manaView[i][j].setY(j * 30 + 200);
            }
        }

        // initilize for first player
        playerText[turn].setFill(Color.RED);
        playerScoreRec[turn].setStroke(Color.RED);
        increaseMana(turn); // set mana = 1
        playerText[turn == 1 ? 0 : 1].setFill(Color.WHITE);
        playerScoreRec[turn == 1 ? 0 : 1].setStroke(Color.WHITE);

        // initilize stark
        starkView = new ImageView();
        starkView.setX(350);
        starkView.setY(222);
        parentPane.getChildren().add(starkView);

        // card show
        StackPane cardShow = new StackPane();
        cardShow.setAlignment(Pos.TOP_CENTER);

        Rectangle cardBorder = new Rectangle(160, 280);
        cardBorder.setStroke(Color.WHITE);
        cardBorder.setFill(Color.TRANSPARENT);
        cardBorder.setEffect(new DropShadow(20, Color.rgb(255, 255, 255, 0.8)));

        cardShow.getChildren().add(cardBorder);

        // add both player score to scorebox
        scoreBox.getChildren().add(playerBox[0]);
        scoreBox.getChildren().add(playerBox[1]);

        cardMiddleShow.setOpacity(0);
        cardMiddleShow.setX(520);
        cardMiddleShow.setY(296);

        // add scorebox & card show to menupane
        menuPane.getChildren().add(scoreBox);
        menuPane.getChildren().add(cardShow);

        // add all pane mainpane
        mainPane.getChildren().addAll(player[0].getGridHolder(), menuPane, player[1].getGridHolder());

        // mainscene
        mainScene = new Scene(parentPane, 1200, 650);

        // menuscene
        menuScene = new Scene(mainMenu, 1200, 650);

        // selectscene
        selectScene = new Scene(selectPane, 1200, 650);

        // planscene
        menuScene.setOnKeyPressed(e -> {
            primaryStage.setScene(selectScene);
        });

        // primarystage
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public Pane getParent() {
        return parentPane;
    }

    public Image getNormalWater() {
        return normalWater;
    }

    public Image getFiredWater() {
        return firedWater;
    }

    public Image getMissWater() {
        return missWater;
    }

    public Image getDefWater() {
        return defWater;
    }

    public Image getBomb() {
        return bomb;
    }

    public Image getFishImage() {
        return fish;
    }

    public int getRowAmount() {
        return ROWS;
    }

    public int getColAmount() {
        return COLS;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public int getTurn() {
        return turn;
    }

    public int getOTurn() {
        return (turn == 0 ? 1 : 0);
    }

    public int getShipAmount() {
        return shipAmount;
    }

    public String getShipName(int i) {
        return shipName[i];
    }

    public int getShipLength(int i) {
        return shipLength[i];
    }

    public int getCardAmount() {
        return cardAmount;
    }

    public String getCardSkill(int i) {
        return cardSkill[i];
    }

    public int getCardMana(int i) {
        return cardMana[i];
    }

    public Grid getGrid(int player) {
        return this.player[player].getGrid();
    }

    public Grid getGrid() {
        return player[turn].getGrid();
    }

    public ImageView getCardMiddleShow() {
        return cardMiddleShow;
    }

    public void playBombSound() {
        bombSound.play();
    }

    public void playWaterSound() {
        waterSound.play();
    }

    public void playShieldSound() {
        shieldSound.play();
    }

    public void playErrorSound() {
        errorSound.play();
    }

    public void playYouWinSound() {
        youWinSound.play();
    }

    public void showStark() {
        stark = new Image((new File(imageLocation + "stark.gif")).toURI().toString(), 500, 206, false, true);
        starkView.setImage(stark);
        starkView.setVisible(true);
        starkView.toFront();
        PauseTransition delay = new PauseTransition(Duration.millis(1750));
        delay.setOnFinished(e -> starkView.setVisible(false));
        delay.play();
    }

    public void win(int player) {
        playYouWinSound();
        Text youWinText = new Text("PLAYER " + (player + 1) + " WIN!");
        youWinText.setFont(Font.font("Ubuntu", 40));
        youWinText.setFill(Color.WHITE);
        youWinText.setEffect(new DropShadow(40, Color.rgb(255, 255, 255, 0.8)));
        youWinText.setX(460);
        youWinText.setY(620);
        Rectangle rect = new Rectangle(1200, 650, Color.color(1.0, 0.455, 0.145));
        ImageView youWinView = new ImageView();
        youWinView.setImage(youWinBackgroundImage);
        youWinView.setX(275);
        parentPane.getChildren().add(rect);
        parentPane.getChildren().add(youWinView);
        parentPane.getChildren().add(youWinText);
        mainScene.setOnKeyPressed(e -> Platform.exit());
    }

    public void changeTurn() {
        turn = (turn == 0 ? 1 : 0);
        turnCount++;
        attacked = false; // reset attacked indicator
        increaseMana(); // increase player's mana every turn
        playerText[turn].setFill(Color.RED);
        playerScoreRec[turn].setStroke(Color.RED);
        playerText[turn == 1 ? 0 : 1].setFill(Color.WHITE);
        playerScoreRec[turn == 1 ? 0 : 1].setStroke(Color.WHITE);

        cardMiddleShow.setOpacity(0);
        ColorAdjust desaturate;
        for (int i = 0; i < cardAmount; i++) {
            desaturate = new ColorAdjust();
            if (player[turn].getCard(i).isUsed()) {
                desaturate.setSaturation(-1);
            } else if (mana[turn] < player[turn].getCard(i).getMana()) {
                desaturate.setSaturation(-1);
            } else {
                desaturate.setSaturation(0);
            }
            player[turn].getCard(i).getImageView().setEffect(desaturate);
        }
    }

    public int getScore(int player) {
        return score[player];
    }

    public void setScore(int player, int newScore) {
        score[player] = newScore;
        scoreText[player].setText(getScore(player) + "");
    }

    public void increaseScore(int player) {
        setScore(player, getScore(player) + 1);
    }

    public boolean isAttacked() {
        return attacked;
    }

    public void attacked() {
        attacked = true;
    }

    public int getMana(int player) {
        return mana[player];
    }

    public int getMana() {
        return mana[turn];
    }

    public void setMana(int player, int newMana) {
        if (newMana > 10) {
            newMana = 10; // limit mana to 10
        } else if (newMana < 0) {
            newMana = 0; // limit mana to 0
        }
        if (newMana > getMana(player)) {
            for (int i = getMana(player); i < newMana; i++) {
                parentPane.getChildren().add(manaView[player][i]);
            }
        } else if (newMana < getMana(player)) {
            for (int i = newMana; i < getMana(player); i++) {
                parentPane.getChildren().remove(manaView[player][i]);
            }
        }
        mana[player] = newMana;
    }

    public void setMana(int mana) {
        setMana(turn, mana);
    }

    public void gainMana(int player, int mana) {
        setMana(player, getMana(player) + mana);
    }

    public void gainMana(int mana) {
        gainMana(turn, mana);
    }

    public void increaseMana(int player) {
        gainMana(player, 1);
    }

    public void increaseMana() {
        gainMana(turn, 1);
    }

    public void decreaseMana(int player) {
        gainMana(player, -1);
    }

    public void decreaseMana() {
        gainMana(turn, -1);
    }

    public void setSkill(String skill) {
        player[turn == 0 ? 1 : 0].getGrid().setSkill(skill);
    }

    public void setSkillValue(int skillValue) {
        player[turn == 0 ? 1 : 0].getGrid().setSkillValue(skillValue);
    }

    public String getSkill() {
        return player[turn == 0 ? 1 : 0].getGrid().getSkill();
    }

    public void clearSkill() {
        setSkill("");
        setSkillValue(-1);
    }

    private void clickTurnCard(Stage stage, ImageView card) {
        ScaleTransition stHideFront = new ScaleTransition(Duration.millis(500), card);
        stHideFront.setFromX(1);
        stHideFront.setToX(0);
        if (!turnSelected) {
            stHideFront.play();
        }

        ScaleTransition stShowBack = new ScaleTransition(Duration.millis(500), card);
        stShowBack.setFromX(0);
        stShowBack.setToX(1);

        int turnPick = (int) (Math.random() * 2 + 1);
        if (turnPick == 2) {
            setMana(0, 2);
            setMana(1, 2);
            changeTurn();
        } else {
            setMana(0, 3);
            setMana(1, 2);
        }
        turnSelected = true;

        ImageView playFirst = new ImageView();
        playFirst.setImage(new Image((new File(imageLocation + turnPick + "first.png")).toURI().toString(), 331, 47, false, true));
        playFirst.setX(434.5);
        playFirst.setY(520);
        playFirst.setOpacity(0);
        selectPane.getChildren().add(playFirst);
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), playFirst);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);

        stHideFront.setOnFinished(e -> {
            card.setImage(turnCard[turnPick]);
            stShowBack.play();
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            stShowBack.setOnFinished(e3 -> {
                delay.play();
                ft.play();
            });
            delay.setOnFinished(e2 -> stage.setScene(mainScene));
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
