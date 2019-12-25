package lamprey.javafx.util.effect;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.PerspectiveTransform;
import javafx.stage.Screen;

/**
 * This is a particle effect for JavaFX. Here's a usage example:
 * 
 * <pre>
 * GridPane iWantThisPaneToSnow = new GridPane();
 * ParticleFall snow = new ParticleFall(snowflakeImage, iWantThisPaneToSnow.boundsInLocalProperty());
 * iWantThisPaneToSnow.setEffect(snow);
 * </pre>
 * 
 * You should always pass in the `boundsInLocalProperty` of the node you want to
 * add this effect to. Failure to do so will cause snow to render outside the
 * visible viewport. In addition, you may also include the optional parameters
 * `numFlakes` and `snowSpeed` to control other aspects of the effect. All of
 * these properties may be interacted with like the usual kinds of JavaFX
 * properties.
 * 
 * <pre>
 * ParticleFall snow = new ParticleFall(source, bounds, numParticles, speed);
 * </pre>
 * 
 * @param sourceProperty The source image used by each particle. TODO: Allow
 *            multiple source images that particles will randomly pick from
 *            while generating.
 * @param boundsProperty The viewport which the particles are drawn in.
 *            Typically the local bounds of the parent.
 * @param numParticlesProperty The number of particles on the screen at any
 *            given moment.
 * @param speedProperty Multiplier controlling the speed of the particles.
 * @param minSizeProperty The minimum size of the particles. This is a
 *            multiplier.
 * @param maxSizeProperty The maximum size of the particles. This is a
 *            multiplier.
 * @param fallAngleProperty The angle at which the particles will fall, in
 *            degrees.
 * @param spinSpeedProperty The speed multiplier for the spin.
 * @param spinOrientationProperty The orientation of the spin. May be set to
 *            null to disable spinning entirely.
 * @param bottomInputProperty May be used to combine this effect with other
 *            effects.
 * 
 * @author Griffin O'Neill
 */
public class ParticleFall extends Blend {
    
    // Structural constants
    private final List<Particle> particles;
    private final AnimationTimer animation;
    
    // Properties
    private final ObjectProperty<ImageInput>  src             = new SimpleObjectProperty<>();
    private final ObjectProperty<Bounds>      bounds          = new SimpleObjectProperty<>();
    private final ObjectProperty<Orientation> spinOrientation = new SimpleObjectProperty<>(Orientation.HORIZONTAL);;
    private final IntegerProperty             numParticles    = new SimpleIntegerProperty(100);
    private final DoubleProperty              speed           = new SimpleDoubleProperty(1);
    private final DoubleProperty              minSize         = new SimpleDoubleProperty(0.4);
    private final DoubleProperty              maxSize         = new SimpleDoubleProperty(1);
    private final DoubleProperty              fallAngle       = new SimpleDoubleProperty(0);
    private final DoubleProperty              spinSpeed       = new SimpleDoubleProperty(20);
    
    /**
     * Constructor.
     * 
     * @param src The source image for this particle.
     * @param bounds The bounds of the parent node for this effect, accessed via
     *            {@link Node#getBoundsInLocal() node.boundsInLocalProperty()}.
     *            Necessary for this effect to function. May also be a custom bounds
     *            expression to generate an arbitrary viewport for this effect (will
     *            create a binding to the given bounds expression). If null, this
     *            will default to using the entire screen as bounds (which may waste
     *            resources generating particles outside the visible stage).
     */
    public ParticleFall(ImageInput src, ObjectExpression<Bounds> bounds) {
        if (bounds != null) {
            this.bounds.bind(bounds);
        }
        this.src.set(src);
        particles = new ArrayList<>(numParticles.get());
        animation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Particle particle : particles) {
                    particle.updatePosition();
                }
            }
        };
        numParticles.addListener((observable, oldVal, newVal) -> setTopInput(createEffectChain(this.src.get(), newVal.intValue())));
        this.src.addListener((obs, oldVal, newVal) -> setTopInput(createEffectChain(newVal, numParticles.get())));
        setMode(BlendMode.SRC_OVER);
        setTopInput(createEffectChain(src, numParticles.get()));
    }
    
    /**
     * This class represent the snowflake image.
     */
    private class Particle extends PerspectiveTransform {
        
        // Calculated constants
        private final double yAxis;
        private final double xAxis;
        
        // The particle attributes
        private double x;
        private double y;
        private double size;
        private double speed;
        private double spin;
        private double currentSpin;
        
        /**
         * Particle Constructor.
         * 
         * @param src The source image for this particle.
         */
        Particle(ImageInput src) {
            setInput(src);
            yAxis = src.getSource().getWidth() / 2;
            xAxis = src.getSource().getHeight() / 2;
            generate();
            
            // Set initial position to random
            final double max = 1 - calculateMovementIncrement();
            x = Math.random() * max;
            y = Math.random() * max;
        }
        
        /**
         * Generate this particle.
         */
        void generate() {
            
            // Set randomized values
            speed = 1 + Math.random() * 0.2;
            size = (maxSize.get() - minSize.get()) * Math.random() + minSize.get();
            spin = Math.random();
            
            // Determine the update increment
            final double inc = calculateMovementIncrement();
            
            // Determine a random starting point along the edge
            double cos = Math.cos(Math.toRadians(fallAngle.get()));
            double sin = Math.sin(Math.toRadians(fallAngle.get()));
            if (Math.random() < Math.abs(cos)) {
                x = Math.random() * (1 - inc);
                y = cos >= 0 ? inc : 1 - inc;
            } else {
                x = sin >= 0 ? inc : 1 - inc;
                y = Math.random() * (1 - inc);
            }
        }
        
        /**
         * Update the particle's position.
         */
        void updatePosition() {
            
            // Increment the position
            final double inc = calculateMovementIncrement();
            
            // If reached the end, re-init the particle's position and speed
            if (x > 1 - inc || x < inc || y > 1 - inc || y < inc) {
                generate();
            } else {
                // Update x and y location
                double cosAngle = Math.cos(Math.toRadians(fallAngle.get()));
                double sinAngle = Math.sin(Math.toRadians(fallAngle.get()));
                x += inc * sinAngle;
                y += inc * cosAngle;
                currentSpin = Math.cos((sinAngle * x + cosAngle * y) * spin * spinSpeed.get());
            }
            
            // Move the ImageInput object to the correct location
            convertPositionToLocalBounds();
        }
        
        /**
         * Turn relative position into an actual pixel location.
         */
        private void convertPositionToLocalBounds() {
            
            // Set the image input position
            final double pixelX;
            final double pixelY;
            if (bounds != null && bounds.get() != null) {
                pixelY = y * (bounds.get().getMaxY());
                pixelX = x * (bounds.get().getMaxX());
            } else {
                pixelY = y * (Screen.getPrimary().getVisualBounds().getMaxY() - src.get().getSource().getHeight());
                pixelX = x * (Screen.getPrimary().getVisualBounds().getMaxX() - src.get().getSource().getWidth());
            }
            
            // Calculate the position
            final double yAngle = spinOrientation.get() == Orientation.HORIZONTAL ? currentSpin : 1;
            final double xAngle = spinOrientation.get() == Orientation.VERTICAL ? currentSpin : 1;
            final double left = testBoundsX(pixelX + yAxis - (yAngle * yAxis * size));
            final double top = testBoundsY(pixelY + xAxis - (xAngle * xAxis * size));
            final double right = testBoundsX(pixelX + yAxis + (yAngle * yAxis * size));
            final double bottom = testBoundsY(pixelY + xAxis + (xAngle * xAxis * size));
            
            // Set the PerspectiveTransform properties.
            setUlx(left);
            setLlx(left);
            setUrx(right);
            setLrx(right);
            setUly(top);
            setUry(top);
            setLly(bottom);
            setLry(bottom);
        }
        
        /**
         * Calculate the bounds for any X value
         * 
         * @param xVal The x value to test against the bounds
         * @return A value guaranteed to be in bounds.
         */
        private double testBoundsX(double xVal) {
            return Math.min(bounds.get().getMaxX(), Math.max(xVal, bounds.get().getMinX()));
        }
        
        /**
         * Calculate the bounds for any Y value
         * 
         * @param yVal The y value to test against the bounds
         * @return A value guaranteed to be in bounds.
         */
        private double testBoundsY(double yVal) {
            return Math.min(bounds.get().getMaxY(), Math.max(yVal, bounds.get().getMinY()));
        }
        
        /**
         * Calculate the actual pixel incrememnt at the given speed.
         * 
         * @return The amount of pixels to move the particle at the given speed.
         */
        private double calculateMovementIncrement() {
            return ParticleFall.this.speed.get() * speed * 0.01;
        }
    }
    
    /**
     * Create the appropriate effect chain.
     *
     * @param src The source image input
     * @param The number of particles to generate
     * @return The effect chain for this snow.
     */
    private Blend createEffectChain(ImageInput src, int numParticles) {
        
        // Don't recreate particles if just adding more of the same
        if (numParticles < particles.size() || (!particles.isEmpty() && particles.get(0).getInput() != src)) {
            particles.clear();
        }
        
        // Create the effect chain
        Blend blend = new Blend(BlendMode.ADD);
        for (int i = particles.size(); i < numParticles; i++) {
            final Particle particle = new Particle(src);
            particles.add(particle);
            blend.setTopInput(particle);
            final Blend next = new Blend(BlendMode.ADD);
            next.setBottomInput(blend);
            blend = next;
        }
        
        return blend;
    }
    
    /**
     * @return The AnimationTimer which controls the snow animation.
     */
    public AnimationTimer getAnimation() {
        return animation;
    }
    
    /**
     * @return The source image of this effect. All particles will be generated from
     *         the given source image.
     */
    public ObjectProperty<ImageInput> sourceProperty() {
        return src;
    }
    
    /**
     * Set the source image of this effect. All particles will be generated from the
     * given source image.
     * 
     * @param src The new source image.
     */
    public void setSource(ImageInput src) {
        this.src.set(src);
    }
    
    /**
     * @return The source image of this effect. All particles will be generated from
     *         the given source image.
     */
    public ImageInput getSource() {
        return src.get();
    }
    
    /**
     * @return The bounds which define the viewport of this effect.
     */
    public ObjectProperty<Bounds> boundsProperty() {
        return bounds;
    }
    
    /**
     * Set the bounds of this effect. Necessary if this effect is going to be moved
     * from one node to another.
     * 
     * @param bounds The bounds of the parent node for this effect, accessed via
     *            {@link Node#getBoundsInLocal() node.boundsInLocalProperty()}.
     *            Necessary for this effect to function. May also be a custom bounds
     *            expression to generate an arbitrary viewport for this effect (will
     *            create a binding to the given bounds expression). If null, this
     *            will default to using the entire screen as bounds (which may
     *            render some snowflakes outside the visible stage).
     */
    public void setBounds(Bounds bounds) {
        this.bounds.set(bounds);
    }
    
    /**
     * @return The bounds which define the viewport of this effect.
     */
    public Bounds getBounds() {
        return bounds.get();
    }
    
    /**
     * @return The number of particles in this effect.
     */
    public IntegerProperty numParticlesProperty() {
        return numParticles;
    }
    
    /**
     * Set the number of particles in this effect.
     *
     * @param numFlakes The new number of particles.
     */
    public void setNumParticles(int numParticles) {
        this.numParticles.set(numParticles);
    }
    
    /**
     * @return The number of particles in this effect.
     */
    public int getNumParticles() {
        return numParticles.get();
    }
    
    /**
     * @return The speed multiplier of the snow.
     */
    public DoubleProperty speedProperty() {
        return speed;
    }
    
    /**
     * Set the speed.
     *
     * @param speed The new speed.
     */
    public void setSpeed(double speed) {
        this.speed.set(speed);
    }
    
    /**
     * @return The snow speed.
     */
    public double getSpeed() {
        return speed.get();
    }
    
    /**
     * @return The min size of any snowflake.
     */
    public DoubleProperty minSizeProperty() {
        return minSize;
    }
    
    /**
     * Set the min size of any snowflake.
     *
     * @param minSize The new min size
     */
    public void setMinSize(double minSize) {
        this.minSize.set(minSize);
    }
    
    /**
     * @return The min size of any snowflake.
     */
    public double getMinSize() {
        return minSize.get();
    }
    
    /**
     * @return The max size of any snowflake.
     */
    public DoubleProperty maxSizeProperty() {
        return maxSize;
    }
    
    /**
     * Set the max size of any snowflake.
     *
     * @param maxSize The new max size
     */
    public void setMaxSize(double maxSize) {
        this.maxSize.set(maxSize);
    }
    
    /**
     * @return The max size of any snowflake.
     */
    public double getMaxSize() {
        return maxSize.get();
    }
    
    /**
     * @return The angle of the fall in degrees. 0 Means fall is straight down, 180
     *         means straight up.
     */
    public DoubleProperty fallAngleProperty() {
        return fallAngle;
    }
    
    /**
     * Set the angle of the fall in degrees.
     *
     * @param angle The new fall angle.
     */
    public void setFallAngle(double angle) {
        this.fallAngle.set(angle);
    }
    
    /**
     * @return The angle of the fall in degrees. 0 Means fall is straight down, 180
     *         means straight up.
     */
    public double getFallAngle() {
        return fallAngle.get();
    }
    
    /**
     * @return The maximum speed that the particle can spin.
     */
    public DoubleProperty spinSpeedProperty() {
        return spinSpeed;
    }
    
    /**
     * Set The maximum speed that the particle can spin.
     *
     * @param spinSpeed The new spin speed.
     */
    public void setSpinSpeed(double spinSpeed) {
        this.spinSpeed.set(spinSpeed);
    }
    
    /**
     * @return The maximum speed that the particle can spin.
     */
    public double getSpinSpeed() {
        return spinSpeed.get();
    }
    
    /**
     * @return The orientation of the spin.
     */
    public ObjectProperty<Orientation> spinOrientationProperty() {
        return spinOrientation;
    }
    
    /**
     * Set The orientation of the spin.
     *
     * @param spinOrientation The new spin orientation.
     */
    public void setSpinOrientation(Orientation spinOrientation) {
        this.spinOrientation.set(spinOrientation);
    }
    
    /**
     * @return The orientation of the spin.
     */
    public Orientation getSpinOrientation() {
        return spinOrientation.get();
    }
}
