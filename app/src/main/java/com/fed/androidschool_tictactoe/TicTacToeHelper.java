package com.fed.androidschool_tictactoe;

import android.util.Log;

public class TicTacToeHelper {


    private int[][] mField;


    public TicTacToeHelper(int[][] mField) {
        this.mField = mField;
    }


    public int check() {
        for (int i = 0; i < 3; i++) {

            if (mField[i][1] == mField[i][2] && mField[i][2] == mField[i][0] && mField[i][0] != FieldView.NULL)
                return mField[i][0];
            if (mField[1][i] == mField[2][i] && mField[2][i] == mField[0][i] && mField[0][i] != FieldView.NULL)
                return mField[0][i];
        }
        if (mField[0][0] == mField[1][1] && mField[1][1] == mField[2][2] && mField[2][2] != FieldView.NULL) {
            return mField[1][1];
        }
        if (mField[0][2] == mField[1][1] && mField[1][1] == mField[2][0] && mField[2][0] != FieldView.NULL) {
            return mField[1][1];
        }
        if (mField[0][0]!=FieldView.NULL && mField[1][0]!=FieldView.NULL&& mField[2][0]!=FieldView.NULL
                && mField[0][1]!=FieldView.NULL&& mField[1][1]!=FieldView.NULL&& mField[2][1]!=FieldView.NULL
                && mField[0][2]!=FieldView.NULL&& mField[1][2]!=FieldView.NULL&& mField[2][2]!=FieldView.NULL) {

            return FieldView.NO_WINNER;
        }
        else return FieldView.NO_END;
    }


}
