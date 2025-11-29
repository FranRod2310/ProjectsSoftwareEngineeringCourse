/**
 * @author Dinis Raleiras 67819
 * @author Filipe Nobre 67850
 * A support structure that provides a temporary damage boost to allied turrets within its range.
 * The tower periodically emits a circular pulse. When the pulse reaches a turret, a visual effect
 * is triggered and the turret receives a damage multiplier buff.
 * This block consumes power and remains active as long as it has sufficient efficiency.
 * The damage buff is applied continuously to all allied turrets inside the range.
 */
package mindustry.world.blocks.defense;


import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.blocks.defense.turrets.Turret;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.world.blocks.power.PowerBlock;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

import static mindustry.Vars.tilesize;

public class SupportBuffTower extends PowerBlock {
    public final float buffRange = 60f;
    public final float baseDamageMultiplier = 2.5f;

    /**
     * Creates a new SupportBuffTower with predefined construction time, power consumption,
     * and defensive stats.
     *
     * @param name name of the block in Mindustry's content system
     */
    public SupportBuffTower(String name) {
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        consumePower(1.2f);
        buildTime = 120f;
        health = 140;
        consumesPower = true;
        hasConsumers = true;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.damageMultiplier, baseDamageMultiplier, StatUnit.none);
        stats.add(Stat.range, buffRange / Vars.tilesize, StatUnit.blocks);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, buffRange, Pal.accent);
    }

    /**
     * Inner build class that handles runtime behavior, animations,
     * pulse effects, and buff application.
     */
    public class SupportBuffBuild extends Building {
        float visualTimer = 60f;
        float pulseTimer = 0f;
        final float pulseDuration = 90f;

        @Override
        public void drawSelect() {
            Drawf.dashCircle(x, y, buffRange, Pal.accent);
        }

        @Override
        public void draw() {
            super.draw();
            if (isNotPowered()) return;
            float radius = Mathf.lerp(0, buffRange, pulseTimer);
            float alpha = 0.7f * Mathf.curve(pulseTimer, 0f, 0.5f) * (1f - pulseTimer);
            Lines.stroke(3f * (1f - pulseTimer));
            Draw.color(Pal.accent, alpha);
            Lines.circle(x, y, radius);
            Draw.reset();
        }

        @Override
        public void updateTile() {
            super.updateTile();

            if (isNotPowered()) return;
            visualTimer += Time.delta;

            pulseTimer += Time.delta / pulseDuration;
            if (pulseTimer >= 1f) {
                pulseTimer = 0f;
            }

            applyDamageBoost();
        }

        /**
         * Applies the supported damage multiplier to all allied turrets within the buff radius.
         * Additionally, triggers a visual effect when the traveling pulse circle reaches a turret.
         */
        void applyDamageBoost() {
            if (isNotPowered()) return;

            float pulseRadius = Mathf.lerp(0, buffRange, pulseTimer);
            float tolerance = 0.2f;

            Vars.indexer.eachBlock(
                    this.team,
                    this.x,
                    this.y,
                    buffRange,
                    b -> b instanceof Turret.TurretBuild,
                    b -> {
                        Turret.TurretBuild turret = (Turret.TurretBuild) b;

                        // calcula distância do centro do buff até a torre
                        float dist = Mathf.dst(x, y, turret.x, turret.y);

                        // se o pulso "atingiu" a torre
                        if (pulseHits(dist, pulseRadius,  tolerance)) {

                            // Efeito visual
                            Fx.sparkExplosion.at(turret.x, turret.y, 0, Pal.accent);
                        }
                        // Aplica buff
                        turret.supportDamageMultiplier = baseDamageMultiplier;
                    }
            );
        }

        /**
         * Determines if the pulse circle has reached a turret based on distance, pulse radius, and tolerance.
         * @param dist
         * @param pulseRadius
         * @param tolerance
         * @return true if the pulse hits the turret, false otherwise
         */
        private boolean pulseHits(float dist, float pulseRadius, float tolerance) {
            return dist >= pulseRadius - tolerance && dist <= pulseRadius + tolerance;
        }

        /**
         * Checks if the SupportBuffTower is not powered based on its efficiency.
         * @return true if efficiency is less than or equal to zero, false otherwise
         */
        private boolean isNotPowered() {
            return efficiency <= 0;
        }

    }
}
