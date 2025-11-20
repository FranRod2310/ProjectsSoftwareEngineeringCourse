/**
 * TODO
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

    public SupportBuffTower(String name) {
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        consumePower(1.2f);
        buildTime = 120f;
        health = 140;
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
            if (efficiency <= 0f) return;
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

            if (efficiency <= 0f) return;
            visualTimer += Time.delta;

            pulseTimer += Time.delta / pulseDuration;
            if (pulseTimer >= 1f) {
                pulseTimer = 0f;
            }

            applyDamageBoost();
        }

        void applyDamageBoost() {
            if (efficiency <= 0) return;

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
                        if (dist >= pulseRadius - tolerance && dist <= pulseRadius + tolerance) {

                            // Efeito visual
                            Fx.sparkExplosion.at(turret.x, turret.y, 0, Pal.accent);
                        }
                        // Aplica buff
                        turret.supportDamageMultiplier = baseDamageMultiplier;
                    }
            );
        }

    }
}
