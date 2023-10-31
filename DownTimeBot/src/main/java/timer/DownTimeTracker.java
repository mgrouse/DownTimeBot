package timer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mgrouse.downtimebot.Secret;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Update;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import service.ServiceFactory;


public class DownTimeTracker
{
    private static Logger m_logger = LoggerFactory.getLogger(DownTimeTracker.class);

    // If the last cell in a range is blank, it is not included.
    // (row will not contain the blank cell's "Empty" value)

    // static final String RANGE_HEADER = SHEET_NAME + "1:1";
    static final String SHEET_NAME = "Town Directory!";

    static final String RANGE_FULL = SHEET_NAME + "J2:K";
    static final String COLUMN_DT = SHEET_NAME + "J2";

    // Regex
    static final String NUMBER = "-?\\d+";
    static final String MISSION = "[a-zA-Z].*";

    // zero based
    static final int DT_HOURS_INDEX = 0;
    static final int ACT_MISSION_INDEX = 1;

    // both pieces of data equals a list size of 2
    static final int FULL_SIZE = 2;


    public static void addDownTime() throws IOException
    {
	m_logger.info("Adding Down Time.");

	ValueRange sheetData = getDownTimeData();

	// for debugging
	// printValueRange(sheetData.getValues());

	UpdateValuesResponse response = updateData(sheetData);

	// for debugging
	// printValueRange(response.getUpdatedData().getValues());

	// for debugging
	// sheetData = getDownTimeDdata();

	// printValueRange(sheetData.getValues());

    }


    private static ValueRange getDownTimeData() throws IOException
    {
	m_logger.info("Retrieving current Down Time data.");

	Sheets service = ServiceFactory.getSheetsService();

	ValueRange response = null;

	response = service.spreadsheets().values().get(Secret.getSheetID(), RANGE_FULL).execute();

//	catch (IOException e)
//	{
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	}

	return response;
    }

    private static UpdateValuesResponse updateData(ValueRange originalData) throws IOException
    {
	m_logger.info("Updating Down Time values.");

	UpdateValuesResponse result = null;

	Sheets sheets = ServiceFactory.getSheetsService();

	ValueRange range = new ValueRange();

	List<List<Object>> newColumn = new ArrayList<List<Object>>();

	for (List<Object> originalRow : originalData.getValues())
	{
	    // size is 1 based, Index is 0 based

	    switch (originalRow.size())
	    {
		case 0:
		    // add an empty list to preserve spacing.
		    newColumn.add(new ArrayList<Object>());
		    break;

		case 1:
		    // this one means DT only was returned,
		    // could be a number or something wacky

		    // if number
		    if (originalRow.get(DT_HOURS_INDEX).toString().matches(NUMBER))
		    {
			// add DT
			newColumn.add(plusOneAsList(originalRow));
		    }
		    // if something wacky
		    else
		    {
			// add the original data back
			newColumn.add(Arrays.asList(originalRow.get(DT_HOURS_INDEX)));
		    }
		    break;

		case 2:
		    // if the Mission is filled in
		    if (!originalRow.get(ACT_MISSION_INDEX).toString().isBlank())
		    {
			// The character is on a Mission, no additional DownTime has been earned.
			// add the same original data into the newColumn data.
			newColumn.add(Arrays.asList(originalRow.get(DT_HOURS_INDEX)));
		    }
		    else
		    {
			// if number
			if (originalRow.get(DT_HOURS_INDEX).toString().matches(NUMBER))
			{
			    // add DT
			    newColumn.add(plusOneAsList(originalRow));
			}
			// if something wacky
			else
			{
			    // add the original data back
			    newColumn.add(Arrays.asList(originalRow.get(DT_HOURS_INDEX)));
			}
		    }

		    break;

		default:
		    break;
	    }

	}

	range.setValues(newColumn);

	Update update;

	update = sheets.spreadsheets().values().update(Secret.getSheetID(), COLUMN_DT, range);

	update.setValueInputOption("RAW");

	// for debugging
	// update.setIncludeValuesInResponse(true);

	result = update.execute();

//	}
//	catch (IOException e)
//	{
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	}

	return result;
    }


    private static List<Object> plusOneAsList(List<Object> original)
    {
	List<Object> update = new ArrayList<Object>();

	Long newData = 1 + Long.valueOf(original.get(DT_HOURS_INDEX).toString());

	update.add(newData.toString());

	return update;
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

	    for (List<Object> row : rows)
	    {
		StringBuffer s = new StringBuffer("");
		for (Object o : row)
		{
		    s.append(o);
		    s.append(", ");
		}

		System.out.println(s.toString());

	    }
	}
    }


}
