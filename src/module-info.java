module SnowMachine {
    exports lamprey.javafx.util.effect;
    exports lamprey.javafx.util.effect.test to javafx.graphics;
    
    requires javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
}