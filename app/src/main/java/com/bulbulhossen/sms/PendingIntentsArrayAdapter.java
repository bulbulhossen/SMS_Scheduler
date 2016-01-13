package com.bulbulhossen.sms;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PendingIntentsArrayAdapter extends ArrayAdapter<SMSSchedulerPendingIntent> /*implements OnClickListener*/{
	private List<SMSSchedulerPendingIntent> mPendingIntentList;
	private Context mContext;
	//final String newLine = System.getProperty("line.seperator");
	String newL = System.getProperty("line.separator");
	String messageAlertDialog;
	long mIdOfAnAlarm;
	private CancelAnAlarmActivity mCancelAnAlarmActivity;
	public PendingIntentsArrayAdapter(Context context, int resource,
			/*int textViewResourceId,*/ List<SMSSchedulerPendingIntent> objects) {
		super(context, R.layout.cancelanalarm, /*textViewResourceId,*/ objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		mPendingIntentList = objects;
		mCancelAnAlarmActivity = CancelAnAlarmActivity.getCancelAlarmActivity();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// First let's verify the convertView is not null
	    if (convertView == null) {
	        // This a new view we inflate the new layout
	        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = inflater.inflate(R.layout.cancelanalarm, parent, false);
	    }
	    TextView hr = (TextView)convertView.findViewById(R.id.textViewHr);
	    TextView hrValue = (TextView)convertView.findViewById(R.id.textViewHrValue);
	    
	    TextView mins = (TextView)convertView.findViewById(R.id.textViewMins);
	    TextView minsValue = (TextView)convertView.findViewById(R.id.textViewMinsValue);
	    
	    TextView secs = (TextView)convertView.findViewById(R.id.textViewSecs);
	    TextView secsValue = (TextView)convertView.findViewById(R.id.textViewSecsValue);
	
	    TextView day = (TextView)convertView.findViewById(R.id.textViewDay);
	    TextView dayValue = (TextView)convertView.findViewById(R.id.textViewDayValue);
	    
	    TextView month = (TextView)convertView.findViewById(R.id.textViewMonth);
	    TextView monthValue = (TextView)convertView.findViewById(R.id.textViewMonthValue);
	    
	    TextView year = (TextView)convertView.findViewById(R.id.textViewYear);
	    TextView yearValue = (TextView)convertView.findViewById(R.id.textViewYearValue);
	    
	    //TextView details = (TextView)convertView.findViewById(R.id.textViewDetails);
	    
	    //TextView frequency = (TextView)convertView.findViewById(R.id.textViewFrequency);
	    //TextView frequencyValue = (TextView)convertView.findViewById(R.id.textViewFrequencyValue);
	    //Button detailsButton = (Button)convertView.findViewById(R.id.buttonDetails);
	   // Button cancelButton = (Button)convertView.findViewById(R.id.buttonCancel);
	    
	    SMSSchedulerPendingIntent smsSchedulerPendingIntent = mPendingIntentList.get(position);
	    String receiverNumber = smsSchedulerPendingIntent.getNumberToSend();
	    String receiverName = smsSchedulerPendingIntent.getReceiverName();
	    String message = smsSchedulerPendingIntent.getMessage();
	    String frequency = smsSchedulerPendingIntent.getFrequency();
	    /*String newL = System.getProperty("line.separator");*/
	    messageAlertDialog = "Receiver's Number: " + receiverNumber + newL + "Receiver's Name: " + receiverName + newL + "Message: " + message + newL + "Frequency: " + frequency;
	    mIdOfAnAlarm = smsSchedulerPendingIntent.getId();
	    if(position == 0){
	    	hr.setText("Hr");
		    mins.setText("Mins");
		    secs.setText("Secs");
		    day.setText("Day");
		    month.setText("Month");
		    year.setText("Year");
		    //frequency.setText("Frequency");
		    //details.setText("Details");
	    }
	    
	    else{
	    	hr.setText("");
		    mins.setText("");
		    secs.setText("");
		    day.setText("");
		    month.setText("");
		    year.setText("");
		    //frequency.setText("");
		    //details.setText("");
	    }
	    
	    hrValue.setText(Long.toString(smsSchedulerPendingIntent.getHour()));
	    minsValue.setText(Long.toString(smsSchedulerPendingIntent.getMinutes()));
	    secsValue.setText(Long.toString(smsSchedulerPendingIntent.getSeconds()));
	    
	    dayValue.setText(Integer.toString(smsSchedulerPendingIntent.getDay()));
	    //As usually January means month 1 not month 0. Hence while displaying we need to take care of that
	    monthValue.setText(Integer.toString(smsSchedulerPendingIntent.getMonth() + 1));
	    yearValue.setText(Integer.toString(smsSchedulerPendingIntent.getYear()));
	    
	     /*detailsButton.setOnClickListener(new OnClickListener() {
	    // SMSSchedulerPendingIntent currentPendingIntent = mPendingIntentList.get(position);	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), text, duration)
				mCancelAnAlarmActivity.ShowCancelAlarmDetails(messageAlertDialog);
			}
		});
	     cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCancelAnAlarmActivity.CancelAnAlarm(mIdOfAnAlarm);
			}
		});
		*/	
	    //frequencyValue.setText(smsSchedulerPendingIntent.getFrequency());
	    return convertView;
	}
	
	public List<SMSSchedulerPendingIntent> getPendingIntentList(){
		return mPendingIntentList;
	}

	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}*/
	
	

}
