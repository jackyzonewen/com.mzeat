package com.mzeat.util;

import com.mzeat.R;

public class CountStartNum {

	public static final int[] starts = { R.drawable.star0, R.drawable.star5,
			R.drawable.star10, R.drawable.star15, R.drawable.star20,
			R.drawable.star25, R.drawable.star30, R.drawable.star35,
			R.drawable.star40, R.drawable.star45, R.drawable.star50, };

	public static int getStartNum(String avg_point) {

		double num = Double.valueOf(avg_point);
		if (num >= 5.0f) {
			return starts[10];
		} else if (num < 5.0f && num >= 4.5f) {
			return starts[9];
		} else if (num < 4.5f && num >= 4.0f) {
			return starts[8];
		} else if (num < 4f && num >= 3.5f) {
			return starts[7];
		} else if (num < 3.5f && num >= 3.0f) {
			return starts[6];
		} else if (num < 3.0f && num >= 2.5f) {
			return starts[5];
		} else if (num < 2.5f && num >= 2.0f) {
			return starts[4];
		} else if (num < 2.0f && num >= 1.5f) {
			return starts[3];
		} else if (num < 1.5f && num >= 1.0f) {
			return starts[2];
		} else if (num < 1.0f && num >= 0.5f) {
			return starts[1];
		} else if (num < 0.5f && num >= 0f) {
			return starts[0];
		}
		return starts[10];
	}
}
