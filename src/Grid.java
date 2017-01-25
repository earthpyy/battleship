
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Together II
 * Battleship
 * Grid
 *
 * @author Niwat Namisa, Patiphon Suriyawong, Preeti Yuankrathok, Panya Tangkanchanayeunyong
 */
public class Grid {

    private final GridPane pane;
    private final int id;
    private final Area[][] table;
    private final Ship[] ship;
    private final int[][] shipTable;
    private final StackPane stack;
    private String usingSkill;
    private int skillValue;
    private int shipLeft;

    public Grid(int id) {
        this.skillValue = -1;
        this.usingSkill = "";
        this.shipLeft = getMain().getShipAmount();
        this.table = new Area[getMain().getRowAmount()][getMain().getColAmount()];
        this.ship = new Ship[getMain().getShipAmount()];
        this.shipTable = new int[getMain().getRowAmount()][getMain().getColAmount()];
        this.id = id;

        // stack for grid background
        stack = new StackPane();
        stack.setMaxSize(400, 400);
        stack.setMinSize(400, 400);

        if (id == 1) {
            stack.setPadding(new Insets(0, 0, 0, 120));
        }

        // initilize grid
        pane = new GridPane();

        for (int i = 0; i < getMain().getRowAmount(); i++) {
            for (int j = 0; j < getMain().getColAmount(); j++) {
                table[i][j] = new Area(this, i, j);
                pane.add(table[i][j].getImage(), j, i);
            }
        }

        // make ship table all -1
        initTable();

        // generate ships
        for (int i = 0; i < getMain().getShipAmount(); i++) {
            ship[i] = new Ship(this, getMain().getShipName(i), getMain().getShipLength(i));
            // for debug only
//            ship[i] = new Ship(shipName[i], shipLength[i], 0, 0, 'H');
            if (ship[i].getDirection() == 'h') {
                for (int j = ship[i].getCol(); j < ship[i].getCol() + ship[i].getLength(); j++) {
                    shipTable[ship[i].getRow()][j] = i;
                }
            } else {
                for (int j = ship[i].getRow(); j < ship[i].getRow() + ship[i].getLength(); j++) {
                    shipTable[j][ship[i].getCol()] = i;
                }
            }
            // show ship on grid (debug)
//            pane.add(ship[i].getImageView(), ship[i].getCol(), ship[i].getRow(), (ship[i].getDirection() == 'H' ? ship[i].getLength() : 1), (ship[i].getDirection() == 'V' ? ship[i].getLength() : 1));
        }

        // fish background
        ImageView fishView = new ImageView();
        fishView.setImage(getMain().getFishImage());

        // add to stack
        stack.getChildren().add(fishView);
        stack.getChildren().add(pane);
    }

    private Main getMain() {
        return new Main();
    }

    public GridPane getGridPane() {
        return pane;
    }

    public StackPane getPane() {
        return stack;
    }

    public int getId() {
        return id;
    }

    public boolean isShipLeft() {
        return (shipLeft > 0);
    }

    public void decreaseShipLeft() {
        shipLeft--;
    }

    private void initTable() {
        for (int i = 0; i < getMain().getRowAmount(); i++) {
            for (int j = 0; j < getMain().getColAmount(); j++) {
                shipTable[i][j] = -1;
            }
        }
    }

    public Area getArea(int row, int col) {
        return table[row][col];
    }

    public boolean isShipExist(int row, int col) {
        return (shipTable[row][col] != -1);
    }

    public Ship getShipAtArea(int row, int col) {
        return ship[shipTable[row][col]];
    }

    public int getShipTable(int row, int col) {
        return shipTable[row][col];
    }

    public boolean isClicked(int row, int col) {
//        return (clickTable[row][col] == 1);
        return (table[row][col].getStatus() == 1);
    }

    public boolean canPlaceShip(int row, int col, int length, char direction) {
        if (direction == 'h') {
            for (int j = col; j < col + length; j++) {
                if (isShipExist(row, j)) {
                    return false;
                }
            }
        } else {
            for (int j = row; j < row + length; j++) {
                if (isShipExist(j, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getSkill() {
        return usingSkill;
    }

    public void setSkill(String skill) {
        usingSkill = skill;
    }

    public int getSkillValue() {
        return skillValue;
    }

    public void setSkillValue(int skillValue) {
        this.skillValue = skillValue;
    }

    public void clearSkill() {
        usingSkill = "";
        skillValue = -1;
    }

    public void increaseSkillValue() {
        skillValue++;
    }

    public void decreaseSkillValue() {
        skillValue--;
    }

}
