package custom;

import java.awt.*;

/**
 * A Helper class to keep track of common values used as well as helper functions such as
 */
public class Utility {
    /**
     * Color corresponding to the hexadecimal color #
     */
    public static final Color GREY600 = Color.decode("#757575");

    /**
     * Color corresponding to the hexadecimal color #
     */
    public static final Color GREY700 = Color.decode("#616161");

    /**
     * Color corresponding to the hexadecimal color #
     */
    public static final Color GREY800 = Color.decode("#424242");

    /**
     * Color corresponding to the hexadecimal color #
     */
    public static final Color GREY900 = Color.decode("#212121");

    /**
     * Color corresponding to the hexadecimal color #EF5350
     */
    public static final Color RED = Color.decode("#EF5350");

    /**
     * Color corresponding to the hexadecimal color #EC407A
     */
    public static final Color PINK = Color.decode("#EC407A");

    /**
     * Color corresponding to the hexadecimal color #AB47BC
     */
    public static final Color PURPLE = Color.decode("#AB47BC");

    /**
     * Color corresponding to the hexadecimal color #7E57C2
     */
    public static final Color DEEPPURPLE = Color.decode("#7E57C2");

    /**
     * Color corresponding to the hexadecimal color #5C6BC0
     */
    public static final Color INDIGO = Color.decode("#5C6BC0");

    /**
     * Color corresponding to the hexadecimal color #42A5F5
     */
    public static final Color BLUE = Color.decode("#42A5F5");

    /**
     * Color corresponding to the hexadecimal color #29B6F6
     */
    public static final Color LIGHTBLUE = Color.decode("#29B6F6");

    /**
     * Color corresponding to the hexadecimal color #26C6DA
     */
    public static final Color CYAN = Color.decode("#26C6DA");

    /**
     * Color corresponding to the hexadecimal color #26A69A
     */
    public static final Color TEAL = Color.decode("#26A69A");

    /**
     * Color corresponding to the hexadecimal color #66BB6A
     */
    public static final Color GREEN = Color.decode("#66BB6A");

    /**
     * Color corresponding to the hexadecimal color #9CCC65
     */
    public static final Color LIGHTGREEN = Color.decode("#9CCC65");

    /**
     * Color corresponding to the hexadecimal color #D4E157
     */
    public static final Color LIME = Color.decode("#D4E157");

    /**
     * Color corresponding to the hexadecimal color #FFEE58
     */
    public static final Color YELLOW = Color.decode("#FFEE58");

    /**
     * Color corresponding to the hexadecimal color #FFCA28
     */
    public static final Color AMBER = Color.decode("#FFCA28");

    /**
     * Color corresponding to the hexadecimal color #FFA726
     */
    public static final Color ORANGE = Color.decode("#FFA726");

    /**
     * Color corresponding to the hexadecimal color #FF7043
     */
    public static final Color DEEPORANGE = Color.decode("#FF7043");

    /**
     * Color[] Containing the non-grey colors of this class
     */
    public static final Color[] PALETTECOLORS = {
            RED, PINK, PURPLE, DEEPPURPLE, INDIGO, BLUE, LIGHTBLUE, CYAN,
            TEAL, GREEN, LIGHTGREEN, LIME, YELLOW, AMBER, ORANGE, DEEPORANGE
    };

    /**
     * Draws a square checker board pattern as the background choosing the larger of the sizes at the board size
     * @param g Graphics of this canvas
     * @param w desired width
     * @param h desired height
     * @param gridSize Size of each square in the checker board pattern
     */
    public static void drawTransparencyGrid(Graphics g, int w, int h, int gridSize){
        int bigger = (h <= w) ? w : h;
        int numSquares = (int)Math.ceil((double)bigger/gridSize);

        for(int i = 0; i < numSquares; i++){
            for(int j = 0; j < numSquares; j++){
                if(numSquares % 2 == 1){
                    if((i*numSquares + j) % 2 == 0){
                        g.setColor(Utility.GREY600);
                    }else {
                        g.setColor(Color.WHITE);
                    }
                }else{
                    if(i % 2 == 0){
                        if((i*numSquares + j) % 2 == 0) g.setColor(Color.WHITE);
                        else g.setColor(Utility.GREY600);
                    }else{
                        if((i*numSquares + j + 1) % 2 == 0) g.setColor(Color.WHITE);
                        else g.setColor(Utility.GREY600);
                    }
                }
                g.fillRect(j*gridSize, i*gridSize, gridSize, gridSize);
            }
        }
    }

    /**
     * Gets a translucent version of the color specified
     * @param c Original color
     * @param alpha The desired alpha of the original color
     * @return The original color with the specified alpha
     */
    public static Color getTranslucentColor(Color c, int alpha){
        if (c == null) return null;
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        int a = (alpha < c.getAlpha()) ? alpha : c.getAlpha();

        return new Color(r, g, b, a);
    }

    /**
     * Tries to parse a string array to a Color object. Will throw exception if string array cannot be parsed.
     * @param args String array with hexadecimal values. E.g. ["#000000", "#FF"]
     * @return Parsed color
     * @throws Exception Thrown when Color.decode() or Integer.decode() fails
     */
    public static Color vecParseColor(String[] args) throws Exception  {
        Exception e = new Exception("Invalid color format, please check the file. Correct example: \"PEN #000000\" ");
        if(args.length > 2) throw e;
        if(args[0].toUpperCase().equals("OFF")) return null;

        Color c;

        try{
            c = Color.decode(args[0]);
        }catch (Exception ex){
            throw e;
        }

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int a;

        if(args.length > 1){
            try{
                a = Integer.decode("#"+args[1]);
            }catch (Exception ex){
                throw e;
            }
            return new Color(r,g,b,a);
        }else {
            return new Color(r,g,b);
        }
    }


    /**
     * Tries to parse a String[] to an Integer[]. Will throw exception if String[] cannot be parsed.
     * @param strArgs String array of doubles. E.g. ["0.1", "1.0", "0.7",...]
     * @param scale The scale at which the parsed integers should be multiplied by. 1 for no scaling
     * @return Integer array containing parsed Integers
     * @throws Exception Thrown when Double.parseDouble() fails
     */
    public static Integer[] vecArgsToInt(String[] strArgs, int scale) throws Exception{
        Integer[] intArgs = new Integer[strArgs.length];
        for(int i = 0; i < strArgs.length; i++){
            // parsing args to integer and scaling up args
            try {
                intArgs[i] = (int)Math.round(Double.parseDouble(strArgs[i]) * scale);
            }catch (Exception e){
                throw new Exception("Unable to parse arguments. Please check file.");
            }
        }
        return intArgs;
    }
}
