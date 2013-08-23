/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
package org.celstec.arlearn2.android.variable;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.VariableDelegator;

import java.util.ArrayList;
import java.util.Iterator;


public class VariableDisplayHandler {

    private Activity activity;
    private Long runId;
    private Long gameId;

    private Integer score;
    private Integer goldCoin;

    public VariableDisplayHandler(Activity activity) {
        this.activity = activity;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void sync() {
        VariableDelegator.getInstance().syncVariable(this.activity, getRunId(), gameId, new String[] {"score", "goldCoin"});
//        VariableDelegator.getInstance().syncVariable(this.activity, getRunId(), gameId, "goldCoin");
        this.score = VariableDelegator.getInstance().getVariable(getRunId(), "score");
        this.goldCoin = VariableDelegator.getInstance().getVariable(getRunId(), "goldCoin");
        redraw();
    }

    public void redraw() {
        LinearLayout ll = (LinearLayout) activity.findViewById(R.id.scoreLinearLayout);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.removeAllViews();
        if (score != null) ll.addView(createScore(score));
        if (goldCoin != null) ll.addView(createGoldScore(goldCoin));
    }

    private LinearLayout createGoldScore(Integer coins){
        LinearLayout scoreLL = new LinearLayout(activity);
        scoreLL.setGravity(Gravity.RIGHT);
        TextView scoreAsText = new TextView(activity);
        scoreAsText.setText(coins+"x ");
        scoreAsText.setTextColor(activity.getResources().getColor(R.color.black));
        scoreAsText.setTypeface(null, Typeface.BOLD);
        scoreAsText.setTextSize(16);
        scoreLL.addView(scoreAsText);
//        for (int i = 0 ; i<coins; i++) {
            ImageView scoreView = new ImageView(activity);
            scoreView.setImageResource(R.drawable.var_coin_gold);
            scoreLL.addView(scoreView);
//        }


        return scoreLL;
    }

    private LinearLayout createScore(Integer newScore) {
        LinearLayout scoreLL = new LinearLayout(activity);
        scoreLL.setGravity(Gravity.RIGHT);
        ImageView scoreView = new ImageView(activity);
        scoreView.setImageResource(R.drawable.score1);
        scoreLL.addView(scoreView);

        if (newScore < 0) {
            scoreView = new ImageView(activity);
            scoreView.setImageResource(R.drawable.scoremin);
            scoreLL.addView(scoreView);
            newScore = newScore * -1;
        }
        Iterator<Integer> it =getScoreParts(newScore).iterator();

        while (it.hasNext()) {
            scoreLL.addView(getViewForInt(it.next()));
        }

        scoreView = new ImageView(activity);
        scoreView.setImageResource(R.drawable.score4);
        scoreLL.addView(scoreView);
        return scoreLL;
    }

    private ImageView getViewForInt(Integer intNr) {
        ImageView scoreView = new ImageView(activity);
        switch (intNr) {
            case 0:
                scoreView.setImageResource(R.drawable.score20);
                break;
            case 1:
                scoreView.setImageResource(R.drawable.score21);
                break;
            case 2:
                scoreView.setImageResource(R.drawable.score22);
                break;
            case 3:
                scoreView.setImageResource(R.drawable.score23);
                break;
            case 4:
                scoreView.setImageResource(R.drawable.score24);
                break;
            case 5:
                scoreView.setImageResource(R.drawable.score25);
                break;
            case 6:
                scoreView.setImageResource(R.drawable.score26);
                break;
            case 7:
                scoreView.setImageResource(R.drawable.score27);
                break;
            case 8:
                scoreView.setImageResource(R.drawable.score28);
                break;
            case 9:
                scoreView.setImageResource(R.drawable.score29);
                break;

            default:
                break;
        }
        return scoreView;
    }

    private ArrayList<Integer> getScoreParts(Integer score) {
        if (score < 10 && score >= 0) {
            ArrayList<Integer> result = new ArrayList<Integer>();
            result.add(score);
            return result;
        }
        if (score < 0) {
            ArrayList<Integer> result = new ArrayList<Integer>();
            result.add(0);
            return result;
        }
        Integer lastInt = score % 10;
        ArrayList<Integer> result = getScoreParts(score / 10);
        result.add(lastInt);
        return result;

    }

}
