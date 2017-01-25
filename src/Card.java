
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Together II
 * Battleship
 * Card
 *
 * @author Niwat Namisa, Patiphon Suriyawong, Preeti Yuankrathok, Panya Tangkanchanayeunyong
 */
public class Card {

    private final int id;
    private final String skill;
    private final int mana;
    private final Image cardImage;
    private final ImageView cardImageView;
    private boolean used;

    public Card(int id, String skill, int mana) {
        this.id = id;
        this.skill = skill;
        this.mana = mana;
        this.cardImage = new Image((new File(getMain().getImageLocation() + "card" + id + ".jpg")).toURI().toString(), 160, 280, false, true);
        this.cardImageView = new ImageView();
        cardImageView.setImage(cardImage);
        this.used = false;
    }

    private Main getMain() {
        return new Main();
    }

    public int getId() {
        return id;
    }

    public String getSkill() {
        return skill;
    }

    public int getMana() {
        return mana;
    }

    public Image getImage() {
        return cardImage;
    }

    public ImageView getImageView() {
        return cardImageView;
    }

    public boolean isUsed() {
        return used;
    }

    public void use() {
        used = true;
    }

}
