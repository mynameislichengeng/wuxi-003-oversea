package com.wizarpos.pay.db;

import android.net.Uri;
import android.provider.BaseColumns;

import com.wizarpos.pay.common.MemberProviderConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * Columns for the {@code ticket_card_def} table.
 */
public class TicketCardDefColumns implements BaseColumns {
	public static final String TABLE_NAME = "ticket_card_def";
	public static final Uri CONTENT_URI = Uri
			.parse(MemberProviderConstants.CONTENT_URI_BASE + "/" + TABLE_NAME);

	public static final String ID = "id";
	public static final String MID = "mid";
	public static final String MERCHANTID = "merchantid";
	public static final String PAYID = "payid";
	public static final String TICKETNAME = "ticketname";
	public static final String TICKETCODE = "ticketcode";
	public static final String BALANCE = "balance";
	public static final String REUSEDFLAG = "reusedflag";
	public static final String USEDFLAG = "usedflag";
	public static final String VALIDPERIOD = "validperiod";
	public static final String ACTIVEAMOUNT = "activeamount";
	public static final String DESCRIPTION = "description";
	public static final String CREATETIME = "createtime";

	public static final String DEFAULT_ORDER = TABLE_NAME + "." + ID;

	// @formatter:off
	public static final String[] FULL_PROJECTION = new String[] {
			TABLE_NAME + "." + ID, TABLE_NAME + "." + MID,
			TABLE_NAME + "." + MERCHANTID, TABLE_NAME + "." + PAYID,
			TABLE_NAME + "." + TICKETNAME, TABLE_NAME + "." + TICKETCODE,
			TABLE_NAME + "." + BALANCE, TABLE_NAME + "." + REUSEDFLAG,
			TABLE_NAME + "." + USEDFLAG, TABLE_NAME + "." + VALIDPERIOD,
			TABLE_NAME + "." + ACTIVEAMOUNT, TABLE_NAME + "." + DESCRIPTION,
			TABLE_NAME + "." + CREATETIME };
	// @formatter:on

	private static final Set<String> ALL_COLUMNS = new HashSet<String>();

	static {
		ALL_COLUMNS.add(ID);
		ALL_COLUMNS.add(MID);
		ALL_COLUMNS.add(MERCHANTID);
		ALL_COLUMNS.add(PAYID);
		ALL_COLUMNS.add(TICKETNAME);
		ALL_COLUMNS.add(TICKETCODE);
		ALL_COLUMNS.add(BALANCE);
		ALL_COLUMNS.add(REUSEDFLAG);
		ALL_COLUMNS.add(USEDFLAG);
		ALL_COLUMNS.add(VALIDPERIOD);
		ALL_COLUMNS.add(ACTIVEAMOUNT);
		ALL_COLUMNS.add(DESCRIPTION);
		ALL_COLUMNS.add(CREATETIME);
	}

	public static boolean hasColumns(String[] projection) {
		if (projection == null)
			return true;
		for (String c : projection) {
			if (ALL_COLUMNS.contains(c))
				return true;
		}
		return false;
	}
}
