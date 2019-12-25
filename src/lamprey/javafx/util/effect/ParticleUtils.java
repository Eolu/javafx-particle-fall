package lamprey.javafx.util.effect;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * This class contains some utils for the particle effect.
 */
public class ParticleUtils {
    
    /**
     * Generate a snowflake image.
     * 
     * <pre>
     *            ▓          
     *     ▓  ▓   ▓   ▓  ▓   
     *      ▓ ▓   ▓   ▓ ▓    
     *       ▓▓   ▓   ▓▓     
     *     ▓▓▓▓  ▓▓▓  ▓▓▓▓   
     *         ▓  ▓  ▓       
     *          ▓ ▓ ▓        
     *   ▓ ▓     ▓ ▓     ▓ ▓ 
     *    ▓    ▓  ▓  ▓    ▓  
     *  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ 
     *    ▓    ▓  ▓  ▓    ▓  
     *   ▓ ▓     ▓ ▓     ▓ ▓ 
     *          ▓ ▓ ▓        
     *         ▓  ▓  ▓       
     *     ▓▓▓▓  ▓▓▓  ▓▓▓▓   
     *       ▓▓   ▓   ▓▓     
     *      ▓ ▓   ▓   ▓ ▓    
     *     ▓  ▓   ▓   ▓  ▓   
     *            ▓
     * </pre>
     * 
     * @return An Image object which is a snowflake!
     */
    public static Image genSnowflake() {
        final var image = new WritableImage(19, 21);
        final var colorizer = Colorizer.of(image.getPixelWriter(), 0xFFCCCCCC);
        colorizer.color(0, 10);
        colorizer.color(1, 3);
        colorizer.color(1, 6);
        colorizer.color(1, 10);
        colorizer.color(1, 14);
        colorizer.color(1, 17);
        colorizer.color(2, 4);
        colorizer.color(2, 6);
        colorizer.color(2, 10);
        colorizer.color(2, 14);
        colorizer.color(2, 16);
        colorizer.color(3, 5);
        colorizer.color(3, 6);
        colorizer.color(3, 10);
        colorizer.color(3, 14);
        colorizer.color(3, 15);
        colorizer.color(4, 3);
        colorizer.color(4, 4);
        colorizer.color(4, 5);
        colorizer.color(4, 6);
        colorizer.color(4, 9);
        colorizer.color(4, 10);
        colorizer.color(4, 11);
        colorizer.color(4, 14);
        colorizer.color(4, 15);
        colorizer.color(4, 16);
        colorizer.color(4, 17);
        colorizer.color(5, 7);
        colorizer.color(5, 10);
        colorizer.color(5, 13);
        colorizer.color(6, 8);
        colorizer.color(6, 10);
        colorizer.color(6, 12);
        colorizer.color(7, 1);
        colorizer.color(7, 3);
        colorizer.color(7, 9);
        colorizer.color(7, 11);
        colorizer.color(7, 17);
        colorizer.color(7, 19);
        colorizer.color(8, 2);
        colorizer.color(8, 7);
        colorizer.color(8, 10);
        colorizer.color(8, 13);
        colorizer.color(8, 18);
        colorizer.color(9, 0);
        colorizer.color(9, 1);
        colorizer.color(9, 2);
        colorizer.color(9, 3);
        colorizer.color(9, 4);
        colorizer.color(9, 5);
        colorizer.color(9, 6);
        colorizer.color(9, 7);
        colorizer.color(9, 8);
        colorizer.color(9, 9);
        colorizer.color(9, 10);
        colorizer.color(9, 11);
        colorizer.color(9, 12);
        colorizer.color(9, 13);
        colorizer.color(9, 14);
        colorizer.color(9, 15);
        colorizer.color(9, 16);
        colorizer.color(9, 17);
        colorizer.color(9, 18);
        colorizer.color(9, 19);
        colorizer.color(10, 2);
        colorizer.color(10, 7);
        colorizer.color(10, 10);
        colorizer.color(10, 13);
        colorizer.color(10, 18);
        colorizer.color(11, 1);
        colorizer.color(11, 3);
        colorizer.color(11, 9);
        colorizer.color(11, 11);
        colorizer.color(11, 17);
        colorizer.color(11, 19);
        colorizer.color(12, 8);
        colorizer.color(12, 10);
        colorizer.color(12, 12);
        colorizer.color(13, 7);
        colorizer.color(13, 10);
        colorizer.color(13, 13);
        colorizer.color(14, 3);
        colorizer.color(14, 4);
        colorizer.color(14, 5);
        colorizer.color(14, 6);
        colorizer.color(14, 9);
        colorizer.color(14, 10);
        colorizer.color(14, 11);
        colorizer.color(14, 14);
        colorizer.color(14, 15);
        colorizer.color(14, 16);
        colorizer.color(14, 17);
        colorizer.color(15, 5);
        colorizer.color(15, 6);
        colorizer.color(15, 10);
        colorizer.color(15, 14);
        colorizer.color(15, 15);
        colorizer.color(16, 4);
        colorizer.color(16, 6);
        colorizer.color(16, 10);
        colorizer.color(16, 14);
        colorizer.color(16, 16);
        colorizer.color(17, 3);
        colorizer.color(17, 6);
        colorizer.color(17, 10);
        colorizer.color(17, 14);
        colorizer.color(17, 17);
        colorizer.color(18, 10);
        return image;
    }
    
    /**
     * A colorizer interface.
     */
    @FunctionalInterface
    private static interface Colorizer {
        
        /**
         * Color the pixel at the given coordinate.
         * 
         * @param x The x-coordinate.
         * @param y The y-coordinate.
         */
        abstract void color(int x, int y);
        
        /**
         * Generate a function which colors a given coordinate a specific color
         * 
         * @param pixelWriter The PixelWriter that this colorizer will use.
         * @param argb The ARGB color (in the form 0xAARRGGBB).
         * @return A Colorizer which colors a coordinate a specific color.
         */
        static Colorizer of(PixelWriter pixelWriter, int argb) {
            return (x, y) -> pixelWriter.setArgb(x, y, argb);
        }
    }
}
