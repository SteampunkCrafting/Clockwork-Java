package spc.clockwork.gameobject.entity;


import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.FontTexture;
import spc.clockwork.graphics.Material;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.util.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TextEntity is an {@link Entity} that is a rectangle with a text inside.
 * It contains {@link Font}, and a text {@link String}
 * Is mainly used as a part of gui
 *
 * @author wize
 * @version 0 (20 May 2018)
 */
public class TextEntity extends Entity {

    /* ATTRIBUTES
    /*--------------------*/

    /** A text that is rendered by this {@link TextEntity} */
    private String text;

    /** A {@link Font} of this {@link TextEntity} */
    private Font font;

    /** A generated {@link spc.clockwork.graphics.FontTexture} of this {@link TextEntity} */
    private FontTexture fontTexture;

    /** Current charset */
    private String charset;

    /** The font-fontTexture {@link HashMap}, which is used to avoid font texture recreations */
    private static HashMap<Font, FontTexture> fontMap = new HashMap<>();



    /** Default charset of this class */
    private static final String DEFAULT_CHARSET = "ISO-8859-1";

    /** The amount of object scale to do in order to increase the visible quality of a text */
    private static final float DEFAULT_QUALITY_BOOST_SCALE = 0.25f;

    /** Error message, if failed to set new font texture for this TextEntity */
    private static final String ERROR_UNABLE_TO_SET_FONT_TEXTURE =
            "Error: failed to set the font texture due to java exception. ";

    /** Default z-position of all TextEntity meshes */
    private static final float DEFAULT_VERTEX_Z_POS = 0f;

    /** Number of vertices in a quad */
    private static final int VERTICES_PER_QUAD = 4;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Gets the required font texture by means of generating a new one or getting one of the recently used
     * @param font the font of the texture
     * @param charset the charset of the texture
     * @return an instance of {@link FontTexture}, which has the required font
     * @throws IOException in case the generation of a texture was failed
     */
    private static FontTexture getFontTexture(GameWorld gameWorld, Font font, String charset) throws IOException{
        return TextEntity.fontMap.containsKey(font) ? TextEntity.fontMap.get(font) : new FontTexture(gameWorld, font, charset);
    }


    /**
     * Creates a {@link Mesh}, which is a rectangle with required by parameter text
     * letters as texture coordinates for its polygons
     * @param text text to write on the quads
     * @param fontTexture the texture of the font to use
     * @return the new instance of Mesh
     */
    private static Mesh buildMesh(GameWorld gameWorld, String text, FontTexture fontTexture) {

        /* ---- Getting character codes for this string ---- */
        char[] chars = text.toCharArray();


        /* ---- Declaring lists that keep the vertex values ---- */
        ArrayList<Float> positions = new ArrayList<>();
        ArrayList<Float> textureCoordinates = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();


        /* ---- Computing the mesh points according to the character widths and positions ---- */
        float startX = 0;
        for(int i = 0; i < chars.length; i++) {
            //TODO: FIX MESH CLOCKWISE ORDER GENERATION -- THE REASON OF IMPROPER FACE CULLING
            /* ---- Building a single quad for the character ---- */

            /* -- Preparation -- */
            int characterStartX = fontTexture.getCharacterStartX(chars[i]);
            int characterWidth = fontTexture.getCharacterWidth(chars[i]);
            float fontTextureHeight = (float) fontTexture.getHeight();
            float fontTextureWidth = (float) fontTexture.getWidth();


            /* -- Left top corner -- */
            positions.add(startX); // x
            positions.add(0.0f); //y
            positions.add(DEFAULT_VERTEX_Z_POS); //z
            textureCoordinates.add((float)characterStartX / fontTextureWidth);
            textureCoordinates.add(1f);
            indices.add(i * VERTICES_PER_QUAD);


            /* -- Left bottom corner -- */
            positions.add(startX); // x
            positions.add(fontTextureHeight); //y
            positions.add(DEFAULT_VERTEX_Z_POS); //z
            textureCoordinates.add((float) characterStartX / fontTextureWidth);
            textureCoordinates.add(0f);
            indices.add(i * VERTICES_PER_QUAD + 1);


            /* -- Right bottom corner -- */
            positions.add(startX + characterWidth); // x
            positions.add(fontTextureHeight); //y
            positions.add(DEFAULT_VERTEX_Z_POS); //z
            textureCoordinates.add((characterStartX + characterWidth) / fontTextureWidth);
            textureCoordinates.add(0f);
            indices.add(i * VERTICES_PER_QUAD + 2);


            /* -- Right top corner -- */
            positions.add(startX + characterWidth); // x
            positions.add(0.0f); //y
            positions.add(DEFAULT_VERTEX_Z_POS); //z
            textureCoordinates.add((characterStartX + characterWidth) / fontTextureWidth);
            textureCoordinates.add(1f);
            indices.add(i * VERTICES_PER_QUAD + 3);

            /* -- Additional indices -- */
            indices.add(i * VERTICES_PER_QUAD);
            indices.add(i * VERTICES_PER_QUAD + 2);



            startX += characterWidth;
        }

        /* ---- Assembling the mesh ---- */
        float[] normalsArray = new float[positions.size()];
        return new Mesh(
                gameWorld,
                Utils.floatListToArray(positions),
                Utils.floatListToArray(textureCoordinates),
                normalsArray,
                Utils.intListToArray(indices),
                "TextEntity: \"" + text + "\"");
    }


    /**
     * Updates the mesh of this, deleting the previous mesh
     */
    private void updateContents() {
        if(this.getFontTexture() == null || this.getText() == null) return;
        this.setMesh(TextEntity.buildMesh(GameWorld.getTemp(), this.getText(), this.getFontTexture()));
        this.setMaterial(new Material(this.getGameWorld(), this.getFontTexture()));
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Font getter
     * @return current font
     */
    public Font getFont() {
        return this.font;
    }


    /**
     * Font texture getter
     * @return the font texture of this
     */
    public FontTexture getFontTexture() {
        return this.fontTexture;
    }


    /**
     * A text getter
     * @return current text
     */
    public String getText() {
        return this.text;
    }


    /**
     * A text setter
     * @param text new text
     */
    public void setText(String text) {
        if(this.getText() != null && this.getText().equals(text)) return;
        this.text = text;
        this.updateContents();
    }


    /**
     * A font setter
     * @param font new font
     */
    public void setFont(Font font) {
        this.setFont(font, this.charset != null ? this.charset : TextEntity.DEFAULT_CHARSET);
        this.updateContents();
    }


    /**
     * A font setter with new charset
     * @param font new font
     * @param charset new charset
     */
    public void setFont(Font font, String charset) {
        if (this.getFont() != null && this.font.equals(font)) return;
        try {
            this.fontTexture = TextEntity.getFontTexture(this.getGameWorld(), font, charset);
            this.font = font;
            this.updateContents();
        } catch(IOException e) {
            System.err.println(ERROR_UNABLE_TO_SET_FONT_TEXTURE);
            e.printStackTrace();
        }
    }


    /**
     * {@link Mesh} setter
     * @param mesh new object's mesh
     */
    @Override
    public void setMesh(Mesh mesh) {
        super.setMesh(mesh);
    }

    @Override
    public String toString() {
        return "TextEntity:" + "\"" + this.getText() + "\"";
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates an empty object with random id and default parameters
     *
     * @param gameWorld the game world to attach this object to
     */
    public TextEntity(GameWorld gameWorld, String text, Font font) {
        this(null, gameWorld, text, font);
    }

    /**
     * A constructor that creates an empty object with the specified id and default parameters
     *
     * @param id        an object, which is a convertible to long number or a string representation of a hex long number
     * @param gameWorld the game world to attach this object to
     */
    public TextEntity(Object id, GameWorld gameWorld, String text, Font font) {
        super(id, gameWorld);
        this.setText(text);
        this.setFont(font);
    }
    /*--------------------*/
}