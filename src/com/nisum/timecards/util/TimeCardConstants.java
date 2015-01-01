package com.nisum.timecards.util;

import java.text.SimpleDateFormat;

public class TimeCardConstants {
	public static final String GID_CLIENT_ID = "1";
	public static final String TIME_ENTRY_STATUS_BELOW_NORMAL = "BN";
	public static final String TIME_ENTRY_STATUS_ABOVE_NORMAL = "AN";
	public static final String TIME_ENTRY_STATUS_NORMAL = "NO";
	public static final String TIME_ENTRY_STATUS_JUSTIFIED = "JS";
	public static final int HOURS_PER_DAY = 8;
	public static final String HOURS_UPDATED_YES = "Y";
	public static final String HOURS_PROC_IND_NO = "N";
	public static final String HOURS_PROC_IND_YES = "Y";

	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");

	public static final String ROLLUP_PERIOD_START = "START_DATE";
	public static final String ROLLUP_PERIOD_END = "END_DATE";
	public static final String TIME_IMPORT_SEQ_ID = "SEQ_ID";

	public static final int COMMENTS_MAX_LENGTH = 2000;
	
	//To maintain the Employee Active & Deactive status
	public static final int ACTIVE = 1;
	public static final int DE_ACTIVE =0;
	
}