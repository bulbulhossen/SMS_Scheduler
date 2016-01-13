package com.bulbulhossen.sms;

import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CancelAnAlarmActivity extends ListActivity implements OnItemClickListener{
	private PendingIntentsDataSource mDatasource;
	private ListView mListView;
	private Button mDetailsButton;
	private Button mCancelButton;
	private List<SMSSchedulerPendingIntent> mPendingIntentList;
	private String mNumberToSend;
	private String mReceiverName;
	private String mMessage;
	private String mFrequency;
	private static CancelAnAlarmActivity mCancelAlarmActivity;
	private int mIdOfAnPendingIntent;;
	
	//String newLine;
	String newL = System.getProperty("line.separator");
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    mCancelAlarmActivity = this;
	    mListView = getListView();
	    
	    //mDetailsButton = (Button)(findViewById(R.id.buttonDetails));
	    mDatasource = new PendingIntentsDataSource(this);
	    mDatasource.open();
	    
	    
	    List<SMSSchedulerPendingIntent> values = mDatasource.getAllPendingIntents();
	  
	    final PendingIntentsArrayAdapter pendingIntentAdapter = new PendingIntentsArrayAdapter(this, R.layout.cancelanalarm, values);
	    //setListAdapter(new PendingIntentsArrayAdapter(this, R.layout.cancelanalarm, values));
	    setListAdapter(pendingIntentAdapter);
	    //mDetailsButton = (Button)findViewById(R.id.buttonDetails);
	    mPendingIntentList = pendingIntentAdapter.getPendingIntentList();
	    
	    mListView.setOnItemClickListener(this);
	  
	}
	
	@Override
    public void onPause(){
    	super.onPause();
    	mDatasource.close();
    	/*mEditTextMessage.setText("");
    	mEditTextNumber.setText("");*/
    }
      @Override
    public void onResume(){
    	super.onResume();
    	mDatasource.open();
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	/*mEditTextMessage.setText("");
    	mEditTextNumber.setText("");	*/
    	mDatasource.open();
    	
    }
    @Override
    public void onStop(){
    	super.onStop();
    	/*mEditTextMessage.setText("");
    	mEditTextNumber.setText("");	*/	
    	mDatasource.close();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	/*mEditTextMessage.setText("");
    	mEditTextNumber.setText("");*/
    	mDatasource.close();
    }
    
    @Override
    public void onBackPressed(){
    	mDatasource.close();
    	Intent i = new Intent();
    	i.setClassName("com.somitsolutions.android.smsscheduler", "com.somitsolutions.android.smsscheduler.SetuppageActivity");
    	startActivity(i);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		//final String newLine = System.getProperty("line.seperator");
		SMSSchedulerPendingIntent selectedsmsSchedulerPendingIntent = (SMSSchedulerPendingIntent)(getListAdapter().getItem(arg2));
		mIdOfAnPendingIntent = (int) selectedsmsSchedulerPendingIntent.getId();
		mNumberToSend = selectedsmsSchedulerPendingIntent.getNumberToSend();
		mReceiverName = selectedsmsSchedulerPendingIntent.getReceiverName();
		mMessage = selectedsmsSchedulerPendingIntent.getMessage();
		mFrequency = selectedsmsSchedulerPendingIntent.getFrequency();
		String detailsTest = "Receiver's Number: "+ mNumberToSend + newL + "Receiver's Name: " + mReceiverName + newL + "Message: " + mMessage + newL + "Frequency: " + mFrequency;
		ShowCancelAlarmDetails(detailsTest);
		//mDetailsButton = (Button)(arg1.findViewById(R.id.buttonDetails));
		//mCancelButton = (Button)(arg1.findViewById(R.id.buttonCancel));
		/*mDetailsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String details = "Receiver's Number: "+ mNumberToSend + newL + "Receiver's Name: " + mReceiverName + newL + "Message: " + mMessage + newL + "Frequency: " + mFrequency;
				ShowCancelAlarmDetails(details);
				
			}
		});
		*/
		/*mCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CancelAnAlarm(mIdOfAnPendingIntent);
				
				
			}
		});*/
	}
	
	public static CancelAnAlarmActivity getCancelAlarmActivity(){
		return mCancelAlarmActivity;
	}
	@SuppressWarnings("deprecation")
	public void  ShowCancelAlarmDetails(String message){
		//Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Do you want to cancel this scheduled sms...");
		alertDialog.setMessage(message);
		//alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      // TODO Add your code for the button here.
			   CancelAnAlarm(mIdOfAnPendingIntent);
		   }
		});
		alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int which) {
		      // TODO Add your code for the button here.
		   }
		});
		alertDialog.show();
	}
	
	public void CancelAnAlarm(int id){
		AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(), SendSMSAlarmService.class);
	    PendingIntent pi = PendingIntent.getService(getApplicationContext(), id, i, PendingIntent.FLAG_UPDATE_CURRENT);
	    am.cancel(pi);
	    mDatasource.deletePendingIntent(id);
	    Intent refresh = new Intent();
	    refresh.setClassName("com.somitsolutions.android.smsscheduler", "com.somitsolutions.android.smsscheduler.CancelAnAlarmActivity");
	    startActivity(refresh);
	}
	
	public void DeleteAnForOnceAlarmEntryFromDatabase(int id){
		mDatasource.deletePendingIntent(id);
		/*Intent refresh = new Intent();
	    refresh.setClassName("com.somitsolutions.android.smsscheduler", "com.somitsolutions.android.smsscheduler.CancelAnAlarmActivity");
	    startActivity(refresh);*/
	}
}