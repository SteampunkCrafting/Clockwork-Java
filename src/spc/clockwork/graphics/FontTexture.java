package spc.clockwork.graphics;

import spc.clockwork.collections.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;


/**
 * {@link FontTexture} is a special kind of {@link Texture}, which is created from the {@link Font} object
 *
 * @author wize
 * @version 0 (20 May 2018)
 */
public final class FontTexture extends Texture {

    /* ATTRIBUTES
    /*--------------------*/

    /** The default image format for font textures */
    private static final String DEFAULT_IMAGE_FORMAT = "png";

    /** An error message, if we look for info about non-existing character */
    private static final String ERROR_BAD_CHAR_ARGUMENT =
            "Error: during FontTexture lookup there was no required symbol detected.";

    /** A buffer of the char map, which is used as a trick to write the char map in the construction */
    private static HashMap<Character, CharInfo> charMapBuffer;

    /** A char map of this instance */
    private final HashMap<Character, CharInfo> charMap;

    /** A charset name of this instance */
    private final String charsetName;



    /**
     * The information about the character
     */
    private static class CharInfo {
        private final int startX;
        private final int width;


        CharInfo(int startX, int width) {
            this.startX = startX;
            this.width = width;
        }

        public int getStartX() {
            return startX;
        }

        public int getWidth() {
            return width;
        }
    }
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Given font and charset name, return the image of the charset
     * @param font given {@link Font}
     * @param charsetName the name of the charset
     * @return the {@link BufferedImage} of the font, which can be used for rendering texts
     */
    private static BufferedImage buildTexture(
            final Font font,
            final String charsetName,
            final HashMap<Character, CharInfo> charMap) {

        /* ---- OBTAINING THE INFORMATION ABOUT THE CHARACTERS OF THIS FONT ---- */
        BufferedImage image = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        String charset = FontTexture.getAllAvailableChars(charsetName);
        int width = 0, height = 0;

        for(char c : charset.toCharArray()) {
            CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            width += charInfo.getWidth();
            height = height > fontMetrics.getHeight() ? height : fontMetrics.getHeight();
        }
        graphics2D.dispose();


        /* ---- DRAWING THE STRING OVER A BUFFERED IMAGE ---- */
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setFont(font);
        fontMetrics = graphics2D.getFontMetrics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(charset, 0, fontMetrics.getAscent());
        graphics2D.dispose();

        // try {
        //     ImageIO.write(image, "png", new File("Temp.png"));
        // } catch (Exception e) { e.printStackTrace(); }

        return image;
    }


    /**
     * Given the name of the charset, return the string, which contains all characters that it supports
     * @param charsetName the name of the charset
     * @return String with all the characters the charset supports
     */
    private static String getAllAvailableChars(String charsetName) {
        CharsetEncoder encoder = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for(char c = 0; c < Character.MAX_VALUE; c++) {
            if(encoder.canEncode(c)) result.append(c);
        }
        return result.toString();
    }


    /**
     * Given the font texture, return the {@link ByteArrayInputStream}, that can be used to build textures
     * @param fontTexture the font texture
     * @return the byte buffer of the font texture
     */
    private static InputStream dumpImage(final BufferedImage fontTexture) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(fontTexture, FontTexture.DEFAULT_IMAGE_FORMAT, out);
        return new ByteArrayInputStream(out.toByteArray());
    }


    /**
     * A private character info getter
     * @param character the character to return info about
     * @return the character info (width and starting position on the texture)
     */
    private CharInfo getCharInfo(Character character) {
        CharInfo charInfo;
        if((charInfo = this.charMap.get(character)) == null) throw new IllegalStateException(ERROR_BAD_CHAR_ARGUMENT);
        return charInfo;
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Charset name getter
     * @return the name of a charset as {@link String}
     */
    public String getCharsetName() {
        return charsetName;
    }


    /**
     * CharInfo starting position X getter
     * @param character the required character
     * @return the pixel number, where this character starts
     */
    public int getCharacterStartX(Character character) {
        return this.getCharInfo(character).getStartX();
    }


    /**
     * Character width getter
     * @param character the character that is required to check
     * @return the width of the character given
     */
    public int getCharacterWidth(Character character) {
        return this.getCharInfo(character).getWidth();
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor with a generated description for this combination of {@link Font} and character set
     * @param font the java.awt.Font instance
     * @param charsetName the name of the charset
     * @throws IOException in case the texture was not created
     */
    public FontTexture(GameWorld gameWorld, Font font, String charsetName) throws IOException {
        this(gameWorld, font, charsetName, font.toString() + " : " + charsetName);
    }


    /**
     * Default constructor with a custom description
     * @param font the java.awt.Font instance
     * @param charsetName the name of the charset
     * @param description a custom description
     * @throws IOException in case the texture was not created
     */
    public FontTexture(GameWorld gameWorld, Font font, String charsetName, String description) throws IOException {
        super(gameWorld,
                FontTexture.dumpImage(
                        FontTexture.buildTexture(
                                font, charsetName, charMapBuffer = new HashMap<>())),
                null, null, description);
        this.charMap = FontTexture.charMapBuffer;
        FontTexture.charMapBuffer = null;
        this.charsetName = charsetName;
    }
    /*--------------------*/
}
