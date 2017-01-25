
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Together II
 * Battleship
 * GridHolder
 *
 * @author Niwat Namisa, Patiphon Suriyawong, Preeti Yuankrathok, Panya Tangkanchanayeunyong
 */
public class GridHolder {

    private final int id;
    private final VBox gridHolder;
    private final StackPane cardHolder;
    private final Grid grid;
    private final Card[] card;
    private final ImageView cardShowing;

    public GridHolder(int id) {
        this.id = id;
        this.card = new Card[getMain().getCardAmount()];
        grid = new Grid(id);

        // showing card when mouse entered
        cardShowing = new ImageView();
        cardShowing.setX(710 * id + 15);
        cardShowing.setY(270);
        cardShowing.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.8)));

        // VBox for grid and card
        gridHolder = new VBox();
        gridHolder.setMaxWidth(400);
        gridHolder.setMinWidth(400);
        gridHolder.setSpacing(50);

        // card pane
        cardHolder = new StackPane();
        cardHolder.setAlignment(Pos.TOP_LEFT);
        ImageView cardIV;
        for (int i = 0; i < getMain().getCardAmount(); i++) {
            final int index = i;
            card[i] = new Card(i, getMain().getCardSkill(i), getMain().getCardMana(i));
            cardIV = card[i].getImageView();
            if (id == 1) {
                cardIV.setX(200);
            }
            cardIV.setTranslateX(i * 50);
            ColorAdjust desaturate = new ColorAdjust();
            desaturate.setSaturation(-1);
            desaturate.setInput(new DropShadow(10, Color.rgb(0, 0, 0, 0.8)));
            cardIV.setEffect(desaturate);

            cardHolder.getChildren().add(cardIV);
            cardIV.setOnMouseEntered(e -> showCard(index));
            cardIV.setOnMouseExited(e -> hideCard(index));
            cardIV.setOnMouseClicked(e -> useSkill(index));
        }

        // add to VBox
        gridHolder.getChildren().add(grid.getPane());
        gridHolder.getChildren().add(cardHolder);
    }

    private Main getMain() {
        return new Main();
    }

    public int getId() {
        return id;
    }

    public VBox getGridHolder() {
        return gridHolder;
    }

    public Grid getGrid() {
        return grid;
    }

    public Card getCard(int i) {
        return card[i];
    }

    private void showCard(int i) {
        if (getMain().getTurn() == id && getMain().getSkill().equals("") && !getMain().isAttacked()) {
            cardShowing.setImage(card[i].getImage());
            cardShowing.setTranslateX(i * 50);
            getMain().getParent().getChildren().add(cardShowing);
        }
    }

    private void hideCard(int i) {
        getMain().getParent().getChildren().remove(cardShowing);
    }

    private void useSkill(int i) {
        if (id != getMain().getTurn()) {
            getMain().playErrorSound();
            return;
        }

        if (getMain().getMana() < card[i].getMana()) {
            getMain().playErrorSound();
            return;
        }

        if (card[i].isUsed()) {
            getMain().playErrorSound();
            return;
        }

        if (getMain().getSkill().equals("") && !getMain().isAttacked()) {
            // show card in middle
            getMain().getCardMiddleShow().setImage(card[i].getImage());
            FadeTransition ft = new FadeTransition(Duration.seconds(0.3), getMain().getCardMiddleShow());
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();

            card[i].use();
            getMain().setSkill(card[i].getSkill());
            getMain().gainMana(-card[i].getMana());
            if (getMain().getSkill().equals("rand5")) {
                int randRow, randCol, attackLeft = 5;
                while (attackLeft > 0) {
                    randRow = (int) (Math.random() * getMain().getRowAmount());
                    randCol = (int) (Math.random() * getMain().getColAmount());
                    if (!getMain().getGrid(getMain().getOTurn()).isClicked(randRow, randCol) && getMain().getGrid(getMain().getOTurn()).getArea(randRow, randCol).getStatus() != -1) {
                        getMain().getGrid(getMain().getOTurn()).getArea(randRow, randCol).bypassHit();
                        attackLeft--;
                    }
                }
                getMain().clearSkill();
                getMain().changeTurn();
            } else if (getMain().getSkill().equals("clear")) {
                for (int j = 0; j < getMain().getRowAmount(); j++) {
                    for (int k = 0; k < getMain().getColAmount(); k++) {
                        if (getMain().getGrid().isClicked(j, k) && !getMain().getGrid().isShipExist(j, k)) {
                            getMain().getGrid().getArea(j, k).disClicked();
                            getMain().getGrid().getArea(j, k).makeNormal();
                        }
                    }
                }
                getMain().clearSkill();
                getMain().changeTurn();
            } else if (card[i].getSkill().equals("2hit")) {
                getMain().setSkillValue(2);
            } else if (card[i].getSkill().equals("3hit")) {
                getMain().setSkillValue(3);
            }
        } else {
            getMain().playErrorSound();
        }
    }

}
