package com.mcnedward.keepfit.model;

import com.mcnedward.keepfit.activity.fragment.BaseFragment;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Unit;

/**
 * Created by Edward on 1/31/2016.
 */
public class FragmentCode extends BaseEntity {

    public enum Code {
        GOAL_OF_THE_DAY("Goal Of The Day"),
        GOALS("Goals"),
        HISTORY("History"),
        STATISTICS("Statistics");
        String title;

        Code(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    private int codeId;
    private String title;

    public FragmentCode() {

    }

    public Code getCodeByTitle() {
        for (Code code : Code.values()) {
            if (code.title().equals(title)) {
                return code;
            }
        }
        return null;
    }

    public FragmentCode(String title) {
        this.title = title;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
