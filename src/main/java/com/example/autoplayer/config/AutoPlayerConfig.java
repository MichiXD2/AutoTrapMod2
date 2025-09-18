package com.example.autoplayer.config;

/**
 * Runtime configuration toggles for the auto player.
 */
public class AutoPlayerConfig {

    private boolean enabled = true;
    private boolean enableHud = true;
    private boolean autoCrafting = true;
    private int scanRadius = 32;
    private boolean cautiousMode = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnableHud() {
        return enableHud;
    }

    public void setEnableHud(boolean enableHud) {
        this.enableHud = enableHud;
    }

    public boolean isAutoCrafting() {
        return autoCrafting;
    }

    public void setAutoCrafting(boolean autoCrafting) {
        this.autoCrafting = autoCrafting;
    }

    public int getScanRadius() {
        return scanRadius;
    }

    public void setScanRadius(int scanRadius) {
        this.scanRadius = scanRadius;
    }

    public boolean isCautiousMode() {
        return cautiousMode;
    }

    public void setCautiousMode(boolean cautiousMode) {
        this.cautiousMode = cautiousMode;
    }
}
