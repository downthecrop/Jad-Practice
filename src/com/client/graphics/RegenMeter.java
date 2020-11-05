package com.client.graphics;

import com.client.Client;
import com.client.Skill;

import java.awt.*;
import java.awt.geom.Arc2D;

public class RegenMeter
{
    private static final int SPEC_REGEN_TICKS = 50;
    private static final int NORMAL_HP_REGEN_TICKS = 100;

    private static final Color HITPOINTS_COLOR = brighter(0x9B0703);
    private static final Color SPECIAL_COLOR = brighter(0x1E95B0);
    private static final double DIAMETER = 25D;
    public boolean showHitpoints;
    public boolean showSpecial;

    private double hitpointsPercentage;
    private double specialPercentage;
    private int ticksSinceSpecRegen;
    private int ticksSinceHPRegen;
    private boolean wasRapidHeal;


    private static Color brighter(int color)
    {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color >>> 16, (color >> 8) & 0xFF, color & 0xFF, hsv);
        return Color.getHSBColor(hsv[0], 1.f, 1.f);
    }

    public Dimension render(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if (showHitpoints)
        {
            renderRegen(g,hitpointsPercentage, HITPOINTS_COLOR,0);
        }

        if (showSpecial)
        {
            renderRegen(g, specialPercentage, SPECIAL_COLOR,1);
        }

        return null;
    }

    private void renderRegen(Graphics2D g, double percent, Color color, int orb)
    {
        int x = 5;
        int y = 5;

        if(orb == 1){
            x= 10;
            y = 10;
        }else{
            x = 15;
                    y= 15;

        }


        Arc2D.Double arc = new Arc2D.Double(x + 28d, y + (26 / 2 - DIAMETER / 2), DIAMETER, DIAMETER, 88.d, -360.d * percent, Arc2D.OPEN);
        final Stroke STROKE = new BasicStroke(2f);
        g.setStroke(STROKE);
        g.setColor(color);
        g.draw(arc);
    }




    private void onTick()
    {
        if (Client.instance.specialAttack == 100)
        {
            showSpecial=false;
            // The recharge doesn't tick when at 100%
            ticksSinceSpecRegen = 0;
        }
        else
        {
            showSpecial=true;
            ticksSinceSpecRegen = (ticksSinceSpecRegen + 1) % SPEC_REGEN_TICKS;
        }
        specialPercentage = ticksSinceSpecRegen / (double) SPEC_REGEN_TICKS;


        int ticksPerHPRegen = NORMAL_HP_REGEN_TICKS;
        //if (client.isPrayerActive(Prayer.RAPID_HEAL))
       // {
        //    ticksPerHPRegen /= 2;
       // }

        ticksSinceHPRegen = (ticksSinceHPRegen + 1) % ticksPerHPRegen;
        hitpointsPercentage = ticksSinceHPRegen / (double) ticksPerHPRegen;

        int currentHP = Client.instance.currentStats[3];
        int maxHP = Client.instance.maxStats[3];
        if (currentHP == maxHP)
        {
            showHitpoints=false;
            hitpointsPercentage = 0;
        } else if (currentHP > maxHP)
        {
            showHitpoints=true;
            // Show it going down
            hitpointsPercentage = 1 - hitpointsPercentage;
        }else{
            showHitpoints=true;
        }
    }
}
