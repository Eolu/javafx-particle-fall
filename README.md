# javafx-particle-fall
This is a particle-fall effect for JavaFX. It can be used to create rain, snow, bubbles, etc.

Properties: 
* sourceProperty The source image used by each particle. TODO: Allow
                 multiple source images that particles will randomly pick from
                 while generating.
 * boundsProperty The viewport which the particles are drawn in.
                  Typically the local bounds of the parent.
 *  numParticlesProperty The number of particles on the screen at any
                         given moment.
 * speedProperty Multiplier controlling the speed of the particles.
 * minSizeProperty The minimum size of the particles. This is a
                   multiplier.
 * maxSizeProperty The maximum size of the particles. This is a
                   multiplier.
 * fallAngleProperty The angle at which the particles will fall, in
                     degrees.
 * spinSpeedProperty The speed multiplier for the spin.
 * spinOrientationProperty The orientation of the spin. May be set to
                           null to disable spinning entirely.
 * bottomInputProperty May be used to combine this effect with other
                       effects.

Here's a usage example:

```
GridPane iWantThisPaneToSnow = new GridPane();
ParticleFall snow = new ParticleFall(snowflakeImage, iWantThisPaneToSnow.boundsInLocalProperty());
iWantThisPaneToSnow.setEffect(snow);
```

You should always pass in the `boundsInLocalProperty` of the node you want to
add this effect to. Failure to do so will cause snow to render outside the
visible viewport. In addition, you may also include the optional parameters
`numFlakes` and `snowSpeed` to control other aspects of the effect. All of
these properties may be interacted with like the usual kinds of JavaFX
properties.

```
ParticleFall snow = new ParticleFall(source, bounds, numParticles, speed);
```

The ParticleFallTest class is an entry-point which may be run to create a snowstorm on your monitor! 
