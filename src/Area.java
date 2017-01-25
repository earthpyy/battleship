
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.*;
import javafx.util.Duration;

/**
 * Together II
 * Battleship
 * Area
 *
 * @author Niwat Namisa, Patiphon Suriyawong, Preeti Yuankrathok, Panya Tangkanchanayeunyong
 */
public class Area {

    private final Grid grid;
    private final int row, col;
    private int status;
    private final ImageView image;
    private final ImageView bomb;

    public Area(Grid grid, int row, int col) {
        this.bomb = new ImageView();
        this.image = new ImageView();
        this.status = 0;
        this.grid = grid;
        this.row = row;
        this.col = col;
        image.setImage(getMain().getNormalWater());
        image.setOpacity(0.6);
        image.setOnMouseClicked(e -> hit());
    }

    private Main getMain() {
        return new Main();
    }

    public ImageView getImage() {
        return image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int getTurn() {
        return getMain().getTurn();
    }

    private int getOTurn() {
        return getMain().getOTurn();
    }

    private void changeTurn() {
        getMain().changeTurn();
    }

    public void clicked() {
        status = 1;
    }

    public void disClicked() {
        status = 0;
    }

    public void makeNormal() {
        image.setImage(getMain().getNormalWater());
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), image);
        ft.setFromValue(1.0);
        ft.setToValue(0.6);
        ft.play();
    }

    private void makeFired() {
        getMain().playBombSound();
        bomb.setImage(getMain().getBomb());
        grid.getGridPane().add(bomb, col, row);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(850), e -> bomb.setVisible(false)));
        timeline.play();
        image.setImage(getMain().getFiredWater());
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), image);
        ft.setFromValue(0.6);
        ft.setToValue(1.0);
        ft.play();
    }

    private void makeMiss() {
        getMain().playWaterSound();
        image.setImage(getMain().getMissWater());
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), image);
        ft.setFromValue(0.6);
        ft.setToValue(1.0);
        ft.play();
    }

    private void makeDef() {
        getMain().playShieldSound();
        image.setImage(getMain().getDefWater());
        image.setOpacity(1);
    }

    private void hit() {
        // check player's turn
        if (getTurn() != grid.getId()) {
            getMain().attacked();
            if (status == -1) {
                getMain().playShieldSound();
                status = 0;
                makeNormal();
                getMain().decreaseMana();
                getMain().increaseMana(getOTurn());
                grid.decreaseSkillValue();
                if (grid.getSkillValue() <= 0) {
                    grid.clearSkill();
                    changeTurn();
                }
                return;
            }

            if (!grid.isClicked(row, col)) {
                if (grid.isShipExist(row, col)) { // ship exist
                    Ship hittedShip = grid.getShipAtArea(row, col);
                    makeFired();
                    hittedShip.decreseHp();
                    if (hittedShip.getHp() == 0) {
                        grid.decreaseShipLeft();
                        getMain().increaseScore(getTurn());
                        grid.getGridPane().add(hittedShip.getImageView(), hittedShip.getCol(), hittedShip.getRow(), (hittedShip.getDirection() == 'h' ? hittedShip.getLength() : 1), (hittedShip.getDirection() == 'v' ? hittedShip.getLength() : 1));
                        FadeTransition ft = new FadeTransition(Duration.seconds(0.3), hittedShip.getImageView());
                        ft.setFromValue(0.0);
                        ft.setToValue(1.0);
                        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                        delay.setOnFinished(e -> ft.play());
                        delay.play();

                        // IF WIN
                        if (!grid.isShipLeft()) {
                            PauseTransition delay2 = new PauseTransition(Duration.seconds(1));
                            delay2.setOnFinished(e -> getMain().win(getTurn()));
                            delay2.play();
                            return;
                        }
                    }

                    if (grid.getSkill().equals("gainmana")) { // gainmana
                        getMain().gainMana(getMain().getCardMana(4) + 3); // TODO : change to dynamic variable
                        grid.clearSkill();
                    }

                    if (grid.getSkill().equals("stealmana")) { // stealmana
                        getMain().gainMana(getMain().getCardMana(5) + 2);
                        grid.clearSkill();
                    } else { // not stealmana
                        getMain().increaseMana(getOTurn());
                    }
                    if (hittedShip.getHp() == 0) { // show tony stark
                        getMain().showStark();
                    }
                } else { // ship not exist
                    makeMiss();
                    if (grid.getSkill().equals("2hit") || grid.getSkill().equals("3hit")) { // 2hit && 3hit
                        grid.decreaseSkillValue();
                        if (grid.getSkillValue() <= 0) {
                            grid.clearSkill();
                            changeTurn();
                        }
                    } else { // OTHER SKILL
                        if (grid.getSkill().equals(""));
                        getMain().decreaseMana(); // decrease mana every time you pick wrong area & NORMAL SKILL
                        grid.clearSkill();
                        changeTurn();
                    }
                }
                clicked();
            } else {
                getMain().playErrorSound();
            }

        } else if (getMain().getSkill().equals("def")) { // def
            status = -1;
            makeDef();
            getMain().clearSkill(); // clear skill
            changeTurn();
        } else {
            getMain().playErrorSound();
        }
    }

    public void bypassHit() {
        if (grid.isShipExist(row, col)) { // ship exist
            Ship hittedShip = grid.getShipAtArea(row, col);
            makeFired();
            hittedShip.decreseHp();
            if (hittedShip.getHp() == 0) {
                getMain().increaseScore(getTurn());
                grid.getGridPane().add(hittedShip.getImageView(), hittedShip.getCol(), hittedShip.getRow(), (hittedShip.getDirection() == 'h' ? hittedShip.getLength() : 1), (hittedShip.getDirection() == 'v' ? hittedShip.getLength() : 1));
                FadeTransition ft = new FadeTransition(Duration.seconds(0.3), hittedShip.getImageView());
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(e -> ft.play());
                delay.play();
            }
            getMain().increaseMana(getOTurn());
            if (hittedShip.getHp() == 0) { // show tony stark
                getMain().showStark();
            }
        } else { // ship not exist
            makeMiss();
        }
        clicked();
    }

}
