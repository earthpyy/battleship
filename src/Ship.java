
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Together II
 * Battleship
 * Ship
 *
 * @author Niwat Namisa, Patiphon Suriyawong, Preeti Yuankrathok, Panya Tangkanchanayeunyong
 */
public class Ship {

    private final String name;
    private final int length, row_index, col_index;
    private final Image[] shipImage;
    private final ImageView shipImageView;
    private final char direction;
    private int hp;
    private boolean usedSkill;

    public Ship(String name, int length, int row_index, int col_index, char direction) {
        this.shipImage = new Image[2];
        this.name = name;
        this.length = length;
        this.hp = length;
        this.row_index = row_index;
        this.col_index = col_index;
        this.direction = direction;
        usedSkill = false;
        shipImage[0] = new Image((new File(getMain().getImageLocation() + "ship" + length + "h.png")).toURI().toString(), length * 40, 40, false, true);
        shipImage[1] = new Image((new File(getMain().getImageLocation() + "ship" + length + "v.png")).toURI().toString(), 40, length * 40, false, true);
        shipImageView = new ImageView();
        shipImageView.setImage(shipImage[direction == 'h' ? 0 : 1]);
        shipImageView.setOpacity(0.0);
    }

    public Ship(Grid grid, String name, int length) {
        this.shipImage = new Image[2];
        // random position
        int maxCol, maxRow, col, row;
        char direction;
        if ((int) (Math.random() * 2) == 0) {
            direction = 'h';
            maxCol = getMain().getColAmount() - length;
            maxRow = getMain().getRowAmount() - 1;
        } else {
            direction = 'v';
            maxCol = getMain().getColAmount() - 1;
            maxRow = getMain().getRowAmount() - length;
        }

        // check ship exist
        do {
            col = (int) (Math.random() * (maxCol + 1));
            row = (int) (Math.random() * (maxRow + 1));
        } while (!grid.canPlaceShip(row, col, length, direction));

        // original constructor
        this.name = name;
        this.length = length;
        this.hp = length;
        this.col_index = col;
        this.row_index = row;
        this.direction = direction;
        usedSkill = false;
        shipImage[0] = new Image((new File(getMain().getImageLocation() + "ship" + length + "h.png")).toURI().toString(), length * 40, 40, false, true);
        shipImage[1] = new Image((new File(getMain().getImageLocation() + "ship" + length + "v.png")).toURI().toString(), 40, length * 40, false, true);
        shipImageView = new ImageView();
        shipImageView.setImage(shipImage[direction == 'h' ? 0 : 1]);
        shipImageView.setOpacity(0.0);
    }

    private Main getMain() {
        return new Main();
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getRow() {
        return row_index;
    }

    public int getCol() {
        return col_index;
    }

    public Image getImage(char direction) {
        return shipImage[direction == 'h' ? 0 : 1];
    }

    public Image getImage() {
        return getImage(direction);
    }

    public ImageView getImageView() {
        return shipImageView;
    }

    public char getDirection() {
        return direction;
    }

    public int getHp() {
        return hp;
    }

    public boolean isUsedSkill() {
        return usedSkill;
    }

    public void decreseHp() {
        hp--;
    }

    public void useSkill() {
        usedSkill = true;
    }

}
