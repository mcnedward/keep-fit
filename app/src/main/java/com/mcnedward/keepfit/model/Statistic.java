package com.mcnedward.keepfit.model;

/**
 * Created by Edward on 1/31/2016.
 */
public class Statistic extends BaseEntity {

    public enum Stat {
        AVERAGE("Average"),
        MINIMUM("Minimum"),
        MAXIMUM("Maximum"),
        PERCENTAGE("Percentage Complete");

        private String title;

        Stat(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    private int statId;
    private String title;
    private boolean show;
    private boolean open;

    public Statistic() {

    }

    public Stat getStatByTitle() {
        for (Stat code : Stat.values()) {
            if (code.title().equals(title)) {
                return code;
            }
        }
        return null;
    }

    public Statistic(String title) {
        this.title = title;
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
