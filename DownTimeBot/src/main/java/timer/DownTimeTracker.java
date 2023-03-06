package timer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Update;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import service.ServiceFactory;


public class DownTimeTracker extends TimerTask
{
    static final String SPREADSHEET_ID = "1hWa9BuyQ8vB5AQyzV0-o0IIa_Zpfo4eyrrfNYARGMUA";

    // This needs to go to J because if I is blank, I is not included.
    // (row will not contain the blank cell's value)
    static final String RANGE_FULL = "Town Directory!A3:J";
    static final String RANGE_DT = "Town Directory!F3";

    // zero based
    static final int ACT_MISSION_INDEX = 8;
    static final int DT_HOURS_INDEX = 5;

    @Override
    public void run()
    {
	addDownTime();

    }

    public static void addDownTime()
    {
	ValueRange sheetData = getDownTimeDdata();

	// for debugging
	// printValueRange(sheetData.getValues());

	UpdateValuesResponse response = updateData(sheetData);

	// for debugging
	// printValueRange(response.getUpdatedData().getValues());

	// for debugging
	// sheetData = getDownTimeDdata();

	// printValueRange(sheetData.getValues());

    }

    private static UpdateValuesResponse updateData(ValueRange originalData)
    {
	UpdateValuesResponse result = null;

	Sheets sheets = ServiceFactory.getSheetsService();

	ValueRange range = new ValueRange();

	List<List<Object>> newColumn = new ArrayList<List<Object>>();

	for (List<Object> originalRow : originalData.getValues())
	{
	    // if there is no adventure in the Active Mission column?
	    if (originalRow.get(ACT_MISSION_INDEX).toString().isEmpty())
	    {
		// Add one DownTime day
		newColumn.add(plusOneAsList(originalRow));
	    }
	    else
	    {
		// The character is on a Mission, no additional DownTime has been earned.
		// add the same original data into the newColumn data.
		newColumn.add(Arrays.asList(originalRow.get(DT_HOURS_INDEX)));
	    }
	}

	range.setValues(newColumn);

	Update update;
	try
	{
	    update = sheets.spreadsheets().values().update(SPREADSHEET_ID, "F3", range);

	    update.setValueInputOption("RAW");

	    // for debugging
	    // update.setIncludeValuesInResponse(true);

	    result = update.execute();

	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return result;
    }


    private static List<Object> plusOneAsList(List<Object> original)
    {
	List<Object> update = new ArrayList<Object>();

	Integer newData = 1 + Integer.valueOf(original.get(DT_HOURS_INDEX).toString());

	update.add(newData.toString());

	return update;
    }

    private static ValueRange getDownTimeDdata()
    {
	Sheets service = ServiceFactory.getSheetsService();

	ValueRange response = null;
	try
	{
	    response = service.spreadsheets().values().get(SPREADSHEET_ID, RANGE_FULL).execute();
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return response;
    }

    private static void printValueRange(List<List<Object>> rows)
    {

	if (rows == null || rows.isEmpty())
	{
	    System.out.println("No data found.");
	}
	else
	{
	    System.out.println("======================================");
	    System.out.println("User,    PC,    Adventure,  DT Hours");
	    for (List<Object> row : rows)
	    {
//		StringBuffer s = new StringBuffer("");
//		for (Object o : row)
//		{
//		    s.append(o);
//		    s.append(", ");
//		}
//
//		System.out.println(s.toString());

		// Print columns A, B, I, and F, which correspond to indices 0, 1, 8, and 5.
		System.out.printf("%s, %s, %s, %s\n", row.get(0), row.get(1), row.get(ACT_MISSION_INDEX),
			row.get(DT_HOURS_INDEX));
	    }
	}
    }


}
