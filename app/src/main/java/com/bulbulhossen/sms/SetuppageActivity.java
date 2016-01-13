package com.bulbulhossen.sms;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class SetuppageActivity extends Activity implements OnClickListener{
	
	private String mNumber;
	EditText mEditTextNumber;
	EditText mEditTextMessage;
	private String mContactdisplayName;
	private long mInterval;
	private String mMessage;
	private String mFrequency;
	Button mButtonContact;
	Button mDatePickUp;
	Button mTimePickUp;
	Button mConfirm;
	RadioButton mOneTime, mFifteenMinuets, mHalfHour, mHourR, mHalfDay, mDaily, mWeekly, mMonthly, mYearly; 
	private int mHour;
	private int mMinutes;
	private int mSeconds;
	
	private int mYear;
	private int mMonth;
	private int mDay;
	
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private int mCurrentHour;
    private int mCurrentMinute;
    
    DatePickerDialog mDatePickerDialog;
    TimePickerDialog mTimePickerDialog;
    Calendar c;
    private static SetuppageActivity mMainActivity;
    
    //AlarmManager am;
    //PendingIntent pi;
    private int mId;
    private boolean isOneTime = false;
    private PendingIntentsDataSource mDatasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setuppage);
        mMainActivity = this;
        mButtonContact = (Button)findViewById(R.id.buttonContact);
        mEditTextNumber = (EditText)findViewById(R.id.editTextNumber);
        mDatePickUp = (Button)findViewById(R.id.buttonDatePickup);
        mTimePickUp = (Button)findViewById(R.id.buttonTimePickup);
        mConfirm = (Button)findViewById(R.id.buttonConfirm);
        mEditTextMessage = (EditText)findViewById(R.id.editTextMessage);
        mOneTime = (RadioButton)findViewById(R.id.oneTime);
        mOneTime.setOnClickListener(this);
        mFifteenMinuets = (RadioButton)findViewById(R.id.fifteenMinutes);
        mFifteenMinuets.setOnClickListener(this);
        mHalfHour = (RadioButton)findViewById(R.id.halfHour);
        mHalfHour.setOnClickListener(this);
        mHourR= (RadioButton)findViewById(R.id.hour);
        mHourR.setOnClickListener(this);
        mHalfDay = (RadioButton)findViewById(R.id.halfDay);
        mHalfDay.setOnClickListener(this);
        mDaily = (RadioButton)findViewById(R.id.daily);
        mDaily.setOnClickListener(this);
        mWeekly = (RadioButton)findViewById(R.id.weekly);
        mWeekly.setOnClickListener(this);
        mMonthly = (RadioButton)findViewById(R.id.monthly);
        mMonthly.setOnClickListener(this);
        mYearly = (RadioButton)findViewById(R.id.Yearly);
        mEditTextNumber.setText("");
        mEditTextMessage.setText("");
        
        //create/open the database
        mDatasource = new PendingIntentsDataSource(this);
        mDatasource.open();
        
        c = Calendar.getInstance();
        mCurrentYear = c.get(Calendar.YEAR);
        mCurrentMonth = c.get(Calendar.MONTH);
        mCurrentDay = c.get(Calendar.DAY_OF_MONTH);
        mCurrentHour = c.get(Calendar.HOUR_OF_DAY);
        mCurrentMinute = c.get(Calendar.MINUTE);
        mButtonContact.setOnClickListener( new OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            startActivityForResult(intent, 1);               
	        }
        });
        mDatePickerDialog = new DatePickerDialog(this, mDateSetListener, mCurrentYear, mCurrentMonth,
				mCurrentDay);
        mTimePickerDialog = new TimePickerDialog(this, mTimeSetListener, mCurrentHour, mCurrentMinute, true);
     // add a click listener to the button
        mDatePickUp.setOnClickListener(new OnClickListener() {
            
			public void onClick(View v) {
                //showDialog(DATE_DIALOG_ID);
                //DialogFragment df = new DialogFragment();
                //FragmentManager fm = getFragmentManager();
                mDatePickerDialog.show();
                //df.show(manager, tag)
            }
        });
        
        mTimePickUp.setOnClickListener(new OnClickListener() {
            
			public void onClick(View v) {
                //showDialog(DATE_DIALOG_ID);
                //DialogFragment df = new DialogFragment();
                //FragmentManager fm = getFragmentManager();
                mTimePickerDialog.show();
                //df.show(manager, tag)
            }
        });
        
        mConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMessage = mEditTextMessage.getText().toString();
				mNumber = mEditTextNumber.getText().toString();
			    c.set(Calendar.HOUR_OF_DAY, mHour);
			    c.set(Calendar.MINUTE, mMinutes);
			    c.set(Calendar.SECOND, 0);
			    //c.
			    //c.set(Calendar.DAY_OF_MONTH, mDay);
			    c.set(mYear, mMonth, mDay);
			    /*AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);*/
			    Intent i = new Intent(getApplicationContext(), SendSMSAlarmService.class);
			    i.putExtra("com.somitsolutions.android.smsscheduler.number",mNumber);
			    i.putExtra("com.somitsolutions.android.smsscheduler.message",mMessage );
			    i.putExtra("com.somitsolutions.android.smsscheduler.frequency", mFrequency);
			    
			    /*final int*/ mId = (int) System.currentTimeMillis();
			    i.putExtra("com.somitsolutions.android.smsscheduler.id", mId);
			    AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
			    PendingIntent pi = PendingIntent.getService(getApplicationContext(), mId, i, PendingIntent.FLAG_UPDATE_CURRENT);
			    
			    if(isOneTime == false){
			    	am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), mInterval, pi);
			    	addToDatabase(true);			    
			    	}
			    if(isOneTime == true){
			    	am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pi);
			    	addToDatabase(false);
			    	/*TimerTask deleteAPendingIntentFromDatabaseForOneTimeAlarm = new TimerTask(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//if(isOneTime == true){
								mDatasource.deletePendingIntent(mId);
							//}
						}
				    	
				    };
				    Timer timer = new Timer();
				    timer.schedule(deleteAPendingIntentFromDatabaseForOneTimeAlarm, c.getTimeInMillis());*/
			    }
			    
			    
			    
			    Toast.makeText(getApplicationContext(), "Your SMS has been scheduled...", Toast.LENGTH_LONG).show();
			    //exit the app
			    /*Intent intent = new Intent(Intent.ACTION_MAIN);
			    intent.addCategory(Intent.CATEGORY_HOME);
			    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(intent);*/
			    //finish();
			    Intent refresh = new Intent();
			    refresh.setClassName("com.somitsolutions.android.smsscheduler", "com.somitsolutions.android.smsscheduler.SetuppageActivity");
		    	startActivity(refresh);
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setuppage, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	
        switch(item.getItemId()){
        case R.id.menuCancelAnAlarm:
        	Intent i = new Intent();
        	i.setClassName("com.somitsolutions.android.smsscheduler", "com.somitsolutions.android.smsscheduler.CancelAnAlarmActivity");
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(i);
        	finish();
        	return true;
        }
        return false;
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
    	 c = Calendar.getInstance();
         mCurrentYear = c.get(Calendar.YEAR);
         mCurrentMonth = c.get(Calendar.MONTH);
         mCurrentDay = c.get(Calendar.DAY_OF_MONTH);
         mCurrentHour = c.get(Calendar.HOUR_OF_DAY);
         mCurrentMinute = c.get(Calendar.MINUTE);
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	/*mEditTextMessage.setText("");
    	mEditTextNumber.setText("");	*/
    	mDatasource.open();
    	 c = Calendar.getInstance();
         mCurrentYear = c.get(Calendar.YEAR);
         mCurrentMonth = c.get(Calendar.MONTH);
         mCurrentDay = c.get(Calendar.DAY_OF_MONTH);
         mCurrentHour = c.get(Calendar.HOUR_OF_DAY);
         mCurrentMinute = c.get(Calendar.MINUTE);
    	
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
    	//exit the app
    	mDatasource.close();
    	//finish();
	    Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.addCategory(Intent.CATEGORY_HOME);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	    startActivity(intent);
	    //android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (data != null) {
	        Uri uri = data.getData();

	        if (uri != null) {
	            Cursor c = null;
	            try {
	                c = getContentResolver().query(uri, new String[]{ 
	                            ContactsContract.CommonDataKinds.Phone.NUMBER,  
	                            ContactsContract.CommonDataKinds.Phone.TYPE,
	                            ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME},
	                        null, null, null);

	                if (c != null && c.moveToFirst()) {
	                    //mNumber = c.getString(0);
	                    int type = c.getInt(1);
	                    mEditTextNumber.setText(c.getString(0));
	                    mContactdisplayName = c.getString(2);
	                }
	            } finally {
	                if (c != null) {
	                    c.close();
	                }
	            }
	        }
	    }
    }
    
 // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                   
                }
            };

     // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            	mHour = hourOfDay;
                mMinutes = minute;
                //updateDisplay();
            }
        };
     
        public static SetuppageActivity getMAinActivity(){
        	return mMainActivity;
        }
        
        public String getNumberToSend(){
        	return mNumber;
        }
        
        public String getSMSMessage(){
        	return mMessage;
        }
        
        public int getDay(){
        	return mDay;
        }
        
        public int getMonth(){
        	return mMonth;
        }
        public int getYear(){
        	return mYear;
        }
        
        public int getHour(){
        	return mHour;
        }
        
        public int getMinutes(){
        	return mMinutes;
        }
        
        public int getSeconds(){
        	return mSeconds;
        }
        
        public PendingIntentsDataSource getDataSource(){
        	//create/open the database
        	return mDatasource;
        }
        //mDatasource = new PendingIntentsDataSource(this);
        //mDatasource.open();

		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			
			
			if(v.equals(mOneTime)){
				isOneTime = true;
				mFrequency = "Once";
			}
			if(v.equals(mFifteenMinuets)){
				//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
				mInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
				mFrequency = "15 mins";
				isOneTime = false;
				//c.set(mYear, mMonth, mDay);
			}
			if(v.equals(mHalfHour)){
				//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pi);
				mInterval = AlarmManager.INTERVAL_HALF_HOUR;
				mFrequency = "Half Hour";
				//c.set(mYear, mMonth, mDay);
				isOneTime = false;
			}
			if(v.equals(mHourR)){
				//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pi);
				//c.set(mYear, mMonth, mDay);
				mInterval = AlarmManager.INTERVAL_HOUR;
				mFrequency = "Hour";
				isOneTime = false;
			}
			if(v.equals(mHalfDay)){
					//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pi);
					//c.set(mYear, mMonth, mDay);
					mInterval = AlarmManager.INTERVAL_HALF_DAY;
					mFrequency = "Half Day";
					isOneTime = false;
			}
			if(v.equals(mDaily)){
				//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
				//c.set(mYear, mMonth, mDay);
				mInterval = AlarmManager.INTERVAL_DAY;
				mFrequency = "Daily";
				isOneTime = false;
			}
			if(v.equals(mWeekly)){
				//int day = c.get(Calendar.DAY_OF_WEEK);
				//c.set(mYear, mMonth, mDay);
				//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 7*24*60*60*1000, pi);
				mInterval = 7*24*60*60*1000;
				mFrequency = "Week";
				isOneTime = false;
				
			}
			if(v.equals(mMonthly)){
				//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);*/
				isOneTime = false;
				mFrequency = "Month";
				int month = c.get(Calendar.MONTH);
				if((month == Calendar.JANUARY) || (month == Calendar.MARCH) || (month == Calendar.MAY) || (month == Calendar.JULY)|| (month == Calendar.AUGUST) || (month == Calendar.OCTOBER)|| (month == Calendar.DECEMBER)){
					mInterval = 31*24*60*60*1000;
				}
				if((month == Calendar.APRIL) || (month == Calendar.JUNE) || (month == Calendar.SEPTEMBER) || (month == Calendar.NOVEMBER)){
					mInterval = 30*24*60*60*1000;
				}
				if(month == Calendar.FEBRUARY){
					int year = c.get(Calendar.YEAR);
					if ((year%4) == 0){
						mInterval = 29*24*60*60*1000;
					}
					if((year%4) != 0){
						mInterval = 28*24*60*60*1000;
					}
						
				}
				
			}
			if(v.equals(mYearly)){
				//c.set(mYear, mMonth, mDay);
				//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 365*24*60*60*1000, pi);
				mInterval = 365*24*60*60*1000;
				mFrequency = "Year";
				isOneTime = false;
			}
			//
		}
		
		private void addToDatabase(boolean isRepeated){
			if(isRepeated == true){
				
				SMSSchedulerPendingIntent newDatabeseEntry = mDatasource.createPendingIntents(mId, mHour, mMinutes, mSeconds, mYear, mMonth, mDay, mFrequency, mNumber, mContactdisplayName, mMessage);
			}
			if(isRepeated == false){
				SMSSchedulerPendingIntent newDatabeseEntry = mDatasource.createPendingIntents(mId, mHour, mMinutes, mSeconds, mYear, mMonth, mDay, mFrequency, mNumber, mContactdisplayName, mMessage);
			}
		}
		
		
}
