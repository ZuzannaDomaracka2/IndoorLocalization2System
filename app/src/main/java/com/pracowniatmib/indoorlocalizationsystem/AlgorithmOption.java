package com.pracowniatmib.indoorlocalizationsystem;

class AlgorithmOption {
    private boolean enabled;
    private String name;
    private int iconResId;

    public AlgorithmOption(String name, int iconResId)
    {
        this.name = name;
        this.iconResId = iconResId;
        this.enabled = true;
    }

    public void setEnabled(boolean value)
    {
        enabled = value;
    }

    public boolean isEnabled()
    {
        return enabled;
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
