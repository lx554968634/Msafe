package org.com.lix_.plugin.shadow;

public class ShadowProperty {
	private int shadowColor;
    /**
     * ÒõÓ°°ë¾¶
     */
    private int shadowRadius;
    /**
     * ÒõÓ°xÆ«ÒÆ
     */
    private int shadowDx;
    /**
     * ÒõÓ°yÆ«ÒÆ
     */
    private int shadowDy;

    public int getShadowOffset() {
        return getShadowOffsetHalf() * 2;
    }

    public int getShadowOffsetHalf() {
        return 0 >= shadowRadius ? 0 : Math.max(shadowDx, shadowDy) + shadowRadius;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public ShadowProperty setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public ShadowProperty setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
        return this;
    }

    public int getShadowDx() {
        return shadowDx;
    }

    public ShadowProperty setShadowDx(int shadowDx) {
        this.shadowDx = shadowDx;
        return this;
    }

    public int getShadowDy() {
        return shadowDy;
    }

    public ShadowProperty setShadowDy(int shadowDy) {
        this.shadowDy = shadowDy;
        return this;
    }
}
