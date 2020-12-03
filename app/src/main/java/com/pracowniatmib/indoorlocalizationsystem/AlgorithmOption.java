package com.pracowniatmib.indoorlocalizationsystem;

class AlgorithmOption {
    private boolean enabled;
    private boolean available;
    private String name;
    private int iconResId;

    public AlgorithmOption(String name, int iconResId)
    {
        this.name = name;
        this.iconResId = iconResId;
        this.enabled = true;
        this.available = true;
    }

    public void setEnabled(boolean value)
    {
        enabled = value;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getName()
    {
        return name;
    }

    public int getIconResId()
    {
        return iconResId;
    }
}
