package com.javiervasquez.chefs.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by javiervasquez on 19/05/17.
 */

@IgnoreExtraProperties
public class Score implements Serializable{

    private float score_1;
    private float score_2;
    private float score_3;

    public float getScore_1() {
        return score_1;
    }

    public void setScore_1(float score_1) {
        this.score_1 = score_1;
    }

    public float getScore_2() {
        return score_2;
    }

    public void setScore_2(float score_2) {
        this.score_2 = score_2;
    }

    public float getScore_3() {
        return score_3;
    }

    public void setScore_3(float score_3) {
        this.score_3 = score_3;
    }
}
