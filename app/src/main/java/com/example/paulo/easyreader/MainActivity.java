package com.example.paulo.easyreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.util.TypedValue;

import com.acs.smartcard.Features;
import com.acs.smartcard.PinProperties;
import com.acs.smartcard.Reader.OnStateChangeListener;
import com.acs.smartcard.Reader;
import com.acs.smartcard.TlvProperties;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import java.io.FileOutputStream;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {
    //---------------------------------------------------------------------
    int[] ManualToken = { 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int Posicao = 0;
    ImageButton button0, button1, button2, button3, button4,button5, button6, button7, button8, button9, buttonclear;
    TextView tvManualToken0, tvManualToken1,tvManualToken2,tvManualToken3,tvManualToken4,tvManualToken5,tvManualToken6,tvManualToken7,tvManualToken8, tvNfcLabel, tvIps, tvUsb, tvValidar, tvMsgLin1, tvMsgLin2, tvMsgLin3, tvMsgLin4, buttonsim,buttonnao, tvTopEspacamento;
    String ss = "";
    String SecurityCode= "xxx";

    String clientIp = getLocalIpAddress();

    // Configutarion
    String conf_serverIp = "";
    String conf_serverPort = "";
    String conf_inout= "SOIN";
    String conf_postId= "2";
    String conf_MsgAValidar= "";
    String conf_Keyboard= "";
    String conf_MsgEntradasOK= "";
    String conf_MsgEntradasKO= "";
    String conf_MsgEntradasShow= "";
    String conf_MsgEntradasMuda= "";
    String conf_MsgSaidasOK= "";
    String conf_MsgSaidasKO= "";
    String conf_MsgSaidasShow= "";
    String conf_MsgSaidasMuda= "";
    String conf_MsgSoEntradasOK= "";
    String conf_MsgSoEntradasKO= "";
    String conf_MsgSoEntradasShow= "";
    String conf_MsgSoEntradasMuda= "";
    String conf_PerguntaFinal= "";
    String conf_EspacamentoTopTeclado= "";
    String conf_EspacamentoTopMsg= "";
    String conf_TamanhoLetraMsg= "";
    String FileName = "configurationa.txt";
    private static Context context;
    private static SoundPool soundPool;
    private static HashMap soundPoolMap;
    String commandAPDU = "FFCA000000";
    String MyCode = "";
//------------------------------------------------------------------------



    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private static final String[] powerActionStrings = { "Power Down",
            "Cold Reset", "Warm Reset" };

    private static final String[] stateStrings = { "Unknown", "Absent",
            "Present", "Swallowed", "Powered", "Negotiable", "Specific" };

    private static final String[] featureStrings = { "FEATURE_UNKNOWN",
            "FEATURE_VERIFY_PIN_START", "FEATURE_VERIFY_PIN_FINISH",
            "FEATURE_MODIFY_PIN_START", "FEATURE_MODIFY_PIN_FINISH",
            "FEATURE_GET_KEY_PRESSED", "FEATURE_VERIFY_PIN_DIRECT",
            "FEATURE_MODIFY_PIN_DIRECT", "FEATURE_MCT_READER_DIRECT",
            "FEATURE_MCT_UNIVERSAL", "FEATURE_IFD_PIN_PROPERTIES",
            "FEATURE_ABORT", "FEATURE_SET_SPE_MESSAGE",
            "FEATURE_VERIFY_PIN_DIRECT_APP_ID",
            "FEATURE_MODIFY_PIN_DIRECT_APP_ID", "FEATURE_WRITE_DISPLAY",
            "FEATURE_GET_KEY", "FEATURE_IFD_DISPLAY_PROPERTIES",
            "FEATURE_GET_TLV_PROPERTIES", "FEATURE_CCID_ESC_COMMAND" };

    private static final String[] propertyStrings = { "Unknown", "wLcdLayout",
            "bEntryValidationCondition", "bTimeOut2", "wLcdMaxCharacters",
            "wLcdMaxLines", "bMinPINSize", "bMaxPINSize", "sFirmwareID",
            "bPPDUSupport", "dwMaxAPDUDataSize", "wIdVendor", "wIdProduct" };

    private static final int DIALOG_CONFIG = 0;
    private static final int DIALOG_LOG = 1;


    private UsbManager mManager;
    private Reader mReader;
    private PendingIntent mPermissionIntent;

    private String MyAdapter;
    private int MySlot;
    private String MyLog= ".";
    private final String APP_VERSION = "1.10";
    private static final int MAX_LINES = 14;
    private TextView mDialogResponseTextView;
    private TextView mStatusTextView;
    //    private Spinner mReaderSpinner;
    private ArrayAdapter<String> mReaderAdapter;
    private Spinner mSlotSpinner;
    //    private ArrayAdapter<String> mSlotAdapter;
//    private Spinner mPowerSpinner;
    private TextView mShowLog;
    private TextView RespSim;
    private TextView RespNao;
    private Button mListButton;
    private Button mOpenButton;
    private Button mCloseButton;
    private Button mGetStateButton;
    private Button mPowerButton;
    private Button mGetAtrButton;
    private CheckBox mT0CheckBox;
    private CheckBox mT1CheckBox;
    private Button mSetProtocolButton;
    private Button mGetProtocolButton;
    private Button mTransmitButton;
    private Button mControlButton;
    private Button mGetFeaturesButton;
    private Button mInOutButton;
    private Button mSaveButton;
    private EditText editTexta_validar;
    private EditText editTextserverip;
    private EditText editTextserverport;
    private EditText editTextMsgEntradasOK;
    private EditText editTextMsgEntradasKO;
    private CheckBox checkBoxShowEntradas;
    private CheckBox checkBoxMudaEntradas;

    private EditText editTextMsgSaidasOK;
    private EditText editTextMsgSaidasKO;
    private CheckBox checkBoxShowSaidas;
    private CheckBox checkBoxMudaSaidas;

    private EditText editTextMsgSoEntradasOK;
    private EditText editTextMsgSoEntradasKO;

    private EditText editTextEspacamentoTopTeclado;
    private EditText editTextEspacamentoTopMsg;
    private EditText editTextTamanhoLetraMsg;

    private CheckBox checkBoxShowSoEntradas;
    private CheckBox checkBoxMudaSoEntradas;
    private CheckBox checkBoxKeyboard;
    private Typeface face = null;
    private Typeface faceNormal = null;
    private Drawable Bg1 = null;
    private Drawable Bg2 = null;
    private Drawable Bg3 = null;

    private Features mFeatures = new Features();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {

                synchronized (this) {

                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                        if (device != null) {

                            // Open reader
                            logMsg("Opening reader: " + device.getDeviceName()
                                    + "...");
                            new OpenTask().execute(device);
                        }

                    } else {

                        logMsg("Permission denied for device "
                                + device.getDeviceName());

                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                synchronized (this) {

                    // Update reader list
                    mReaderAdapter.clear();
                    for (UsbDevice device : mManager.getDeviceList().values()) {
                        if (mReader.isSupported(device)) {
                            mReaderAdapter.add(device.getDeviceName());
                        }
                    }

                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device != null && device.equals(mReader.getDevice())) {

                        // Clear slot items
//                        mSlotAdapter.clear();

                        // Close reader
                        logMsg("Closing reader...");
                        new CloseTask().execute();
                    }
                }
            }
        }
    };

    private class OpenTask extends AsyncTask<UsbDevice, Void, Exception> {

        @Override
        protected Exception doInBackground(UsbDevice... params) {

            Exception result = null;

            try {

                mReader.open(params[0]);

            } catch (Exception e) {

                result = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Exception result) {

            if (result != null) {

                logMsg(result.toString());

            } else {

                logMsg("Reader name: " + mReader.getReaderName());

                int numSlots = mReader.getNumSlots();
                logMsg("Number of slots: " + numSlots);

                // Add slot items
//                mSlotAdapter.clear();
                for (int i = 0; i < numSlots; i++) {
//                    mSlotAdapter.add(Integer.toString(i));
                    MySlot = i;
                }

                // Remove all control codes
                mFeatures.clear();

            }
        }
    }

    private class CloseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            mReader.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            mOpenButton.setEnabled(true);
        }

    }

    private class PowerParams {

        public int slotNum;
        public int action;
    }

    private class PowerResult {

        public byte[] atr;
        public Exception e;
    }

    private class PowerTask extends AsyncTask<PowerParams, Void, PowerResult> {

        @Override
        protected PowerResult doInBackground(PowerParams... params) {

            PowerResult result = new PowerResult();

            try {

                result.atr = mReader.power(params[0].slotNum, params[0].action);

            } catch (Exception e) {

                result.e = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(PowerResult result) {

            if (result.e != null) {

                logMsg(result.e.toString());

            } else {

                // Show ATR
                if (result.atr != null) {

                    logMsg("ATR:");
                    logBuffer(result.atr, result.atr.length);

                } else {

                    logMsg("ATR: None");
                }
            }
        }
    }

    private class SetProtocolParams {

        public int slotNum;
        public int preferredProtocols;
    }

    private class SetProtocolResult {

        public int activeProtocol;
        public Exception e;
    }

    private class SetProtocolTask extends
            AsyncTask<SetProtocolParams, Void, SetProtocolResult> {

        @Override
        protected SetProtocolResult doInBackground(SetProtocolParams... params) {

            SetProtocolResult result = new SetProtocolResult();

            try {

                result.activeProtocol = mReader.setProtocol(params[0].slotNum,
                        params[0].preferredProtocols);

            } catch (Exception e) {

                result.e = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(SetProtocolResult result) {

            if (result.e != null) {

                logMsg(result.e.toString());

            } else {

                String activeProtocolString = "Active Protocol: ";

                switch (result.activeProtocol) {

                    case Reader.PROTOCOL_T0:
                        activeProtocolString += "T=0";
                        break;

                    case Reader.PROTOCOL_T1:
                        activeProtocolString += "T=1";
                        break;

                    default:
                        activeProtocolString += "Unknown";
                        break;
                }

                // Show active protocol
                logMsg(activeProtocolString);
            }
        }
    }

    private class TransmitParams {

        public int slotNum;
        public int controlCode;
        public String commandString;
    }

    private class TransmitProgress {

        public int controlCode;
        public byte[] command;
        public int commandLength;
        public byte[] response;
        public int responseLength;
        public Exception e;
    }

    private class TransmitTask extends
            AsyncTask<TransmitParams, TransmitProgress, Void> {

        @Override
        protected Void doInBackground(TransmitParams... params) {
            TransmitProgress progress = new TransmitProgress();

            byte[] command;
            byte[] response = new byte[300];
            int responseLength;
            int foundIndex;
            int startIndex = 0;

            do {

                // Find carriage return
                foundIndex = params[0].commandString.indexOf('\n', startIndex);
                if (foundIndex >= 0) {
                    command = toByteArray(params[0].commandString.substring(
                            startIndex, foundIndex));
                } else {
                    command = toByteArray(params[0].commandString
                            .substring(startIndex));
                }

                // Set next start index
                startIndex = foundIndex + 1;

                progress.controlCode = params[0].controlCode;
                try {

                    if (params[0].controlCode < 0) {

                        // Transmit APDU
                        responseLength = mReader.transmit(params[0].slotNum,
                                command, command.length, response,
                                response.length);

                    } else {

                        // Transmit control command
                        responseLength = mReader.control(params[0].slotNum,
                                params[0].controlCode, command, command.length,
                                response, response.length);
                    }

                    progress.command = command;
                    progress.commandLength = command.length;
                    progress.response = response;
                    progress.responseLength = responseLength;
                    progress.e = null;

                } catch (Exception e) {

                    progress.command = null;
                    progress.commandLength = 0;
                    progress.response = null;
                    progress.responseLength = 0;
                    progress.e = e;
                }

                publishProgress(progress);

            } while (foundIndex >= 0);

            return null;
        }

        @Override
        protected void onProgressUpdate(TransmitProgress... progress) {

            if (progress[0].e != null) {

                logMsg(progress[0].e.toString());

            } else {

                logMsg("Command:");
                logBuffer(progress[0].command, progress[0].commandLength);

                logMsg("Response:");
                logBuffer(progress[0].response, progress[0].responseLength);

                if (progress[0].response != null
                        && progress[0].responseLength > 0) {

                    int controlCode;
                    int i;

                    logMsg("Comando " +  toHexString(progress[0].command) );
                    logMsg("WEBSERVICE call " + toHexString(progress[0].response).replaceAll(" ", "").substring(0, 8));

                    CheckUser("0", toHexString(progress[0].response).replaceAll(" ", "").substring(0, 8));

                    // Show control codes for IOCTL_GET_FEATURE_REQUEST
                    if (progress[0].controlCode == Reader.IOCTL_GET_FEATURE_REQUEST) {

                        mFeatures.fromByteArray(progress[0].response,
                                progress[0].responseLength);

                        logMsg("Features:");
                        for (i = Features.FEATURE_VERIFY_PIN_START; i <= Features.FEATURE_CCID_ESC_COMMAND; i++) {

                            controlCode = mFeatures.getControlCode(i);
                            if (controlCode >= 0) {
                                logMsg("Control Code: " + controlCode + " ("
                                        + featureStrings[i] + ")");
                            }
                        }
                    }

                    controlCode = mFeatures
                            .getControlCode(Features.FEATURE_IFD_PIN_PROPERTIES);
                    if (controlCode >= 0
                            && progress[0].controlCode == controlCode) {

                        PinProperties pinProperties = new PinProperties(
                                progress[0].response,
                                progress[0].responseLength);

                        logMsg("PIN Properties:");
                        logMsg("LCD Layout: "
                                + toHexString(pinProperties.getLcdLayout()));
                        logMsg("Entry Validation Condition: "
                                + toHexString(pinProperties
                                .getEntryValidationCondition()));
                        logMsg("Timeout 2: "
                                + toHexString(pinProperties.getTimeOut2()));
                    }

                    controlCode = mFeatures
                            .getControlCode(Features.FEATURE_GET_TLV_PROPERTIES);
                    if (controlCode >= 0
                            && progress[0].controlCode == controlCode) {

                        TlvProperties readerProperties = new TlvProperties(
                                progress[0].response,
                                progress[0].responseLength);

                        Object property;
                        logMsg("TLV Properties:");
                        for (i = TlvProperties.PROPERTY_wLcdLayout; i <= TlvProperties.PROPERTY_wIdProduct; i++) {

                            property = readerProperties.getProperty(i);
                            if (property instanceof Integer) {
                                logMsg(propertyStrings[i] + ": "
                                        + toHexString((Integer) property));
                            } else if (property instanceof String) {
                                logMsg(propertyStrings[i] + ": " + property);
                            }
                        }
                    }
                }
                //fim do else
            }

        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set window fullscreen and remove title bar, and force landscape orientation
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_main);

        context=getApplicationContext();

//		face = Typeface.createFromAsset(getAssets(),"fonts/bryantproboldaltno1.ttf");
//		HOMIZIO-MEDIUM_0.TTF
//		face = Typeface.createFromAsset(getAssets(),"fonts/Simplon_BP_Regular_1.otf");
//		face = Typeface.createFromAsset(getAssets(),"fonts/HEINEKEN CORE-BOLD.TTF");
        face = Typeface.createFromAsset(getAssets(),"fonts/AzoSans-Bold.otf");
        faceNormal = Typeface.createFromAsset(getAssets(),"fonts/AzoSans-Regular.otf");


        // Get App configuration
        try {

            int Ret = ReadConfig();
            if(Ret == 1)
                ReadConfig();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Bg1 = LoadBackground1();
        Bg2 = LoadBackground2();
        Bg3 = LoadBackground3();
        MySetBackGround("1");

        // Espamento a contar de cima tanto para teclado como para as linhas de msgs finais. Mudar dinamicamento
        tvTopEspacamento = (TextView) findViewById(R.id.topespacamento);

        // Para testar a alturas das linhas em rela�ao � imagem de background
        tvValidar = (TextView) findViewById(R.id.textValidar);
        tvMsgLin1 = (TextView) findViewById(R.id.textViewLin1);
        tvMsgLin2 = (TextView) findViewById(R.id.textViewLin2);
        tvMsgLin3 = (TextView) findViewById(R.id.textViewLin3);
        tvMsgLin4 = (TextView) findViewById(R.id.textViewLin4);

        ManualToken[0] = 0;
        tvIps = (TextView) findViewById(R.id.tvips);
        button0 = (ImageButton) findViewById(R.id.imagebutton0);
        button1 = (ImageButton) findViewById(R.id.imagebutton1);
        button2 = (ImageButton) findViewById(R.id.imagebutton2);
        button3 = (ImageButton) findViewById(R.id.imagebutton3);
        button4 = (ImageButton) findViewById(R.id.imagebutton4);
        button5 = (ImageButton) findViewById(R.id.imagebutton5);
        button6 = (ImageButton) findViewById(R.id.imagebutton6);
        button7 = (ImageButton) findViewById(R.id.imagebutton7);
        button8 = (ImageButton) findViewById(R.id.imagebutton8);
        button9 = (ImageButton) findViewById(R.id.imagebutton9);
        buttonclear = (ImageButton) findViewById(R.id.imagebuttonclear);

        tvManualToken0 = (TextView) findViewById(R.id.tvCode0);
        tvManualToken1 = (TextView) findViewById(R.id.tvCode1);
        tvManualToken2 = (TextView) findViewById(R.id.tvCode2);
        tvManualToken3 = (TextView) findViewById(R.id.tvCode3);
        tvManualToken4 = (TextView) findViewById(R.id.tvCode4);
        tvManualToken5 = (TextView) findViewById(R.id.tvCode5);
        tvManualToken6 = (TextView) findViewById(R.id.tvCode6);
        tvManualToken7 = (TextView) findViewById(R.id.tvCode7);
        tvManualToken8 = (TextView) findViewById(R.id.tvCode8);
        tvMsgLin1 = (TextView) findViewById(R.id.textViewLin1);
        tvMsgLin2 = (TextView) findViewById(R.id.textViewLin2);

        // Mostrar o teclado
        if(conf_Keyboard.equals("SIM"))
        {
            LimpaEcran(1);
        }
        else
        {
            LimpaEcran(0);
        }

        tvMsgLin1.setTypeface(face);
        tvMsgLin1.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(conf_TamanhoLetraMsg));
        tvMsgLin2.setTypeface(faceNormal);
        tvMsgLin2.setTextSize(TypedValue.COMPLEX_UNIT_SP,Float.parseFloat(conf_TamanhoLetraMsg));
        tvMsgLin3.setTypeface(faceNormal);
        tvMsgLin4.setTypeface(face);
        // Linhas de teste
/*		tvMsgLin1.setPadding(5, 5, 5, 5);
		tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
		tvMsgLin1.setTextColor(Color.WHITE);
		tvMsgLin1.setText("Olá,");

		tvMsgLin2.setPadding(5, 5, 5, 5);
		tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
		tvMsgLin2.setTextColor(Color.WHITE);
		tvMsgLin2.setText("abcdefghijlmnporstuvxz 67890 12345 67890 ");

		tvMsgLin3.setPadding(5, 5, 5, 5);
		tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
		tvMsgLin3.setTextColor(Color.WHITE);
		tvMsgLin3.setText("PREPARADO PARA CONHECER O NOSSO RUMO ?");

		tvMsgLin4.setPadding(5, 5, 5, 5);
		tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
		tvMsgLin4.setTextColor(Color.WHITE);
		tvMsgLin4.setText("");*/
        // Fim de Linhas de teste
        // Get USB manager
        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // Initialize reader
        mReader = new Reader(mManager);
        mReader.setOnStateChangeListener(new OnStateChangeListener() {

            @Override
            public void onStateChange(int slotNum, int prevState, int currState) {

                if (prevState < Reader.CARD_UNKNOWN
                        || prevState > Reader.CARD_SPECIFIC) {
                    prevState = Reader.CARD_UNKNOWN;
                }

                if (currState < Reader.CARD_UNKNOWN
                        || currState > Reader.CARD_SPECIFIC) {
                    currState = Reader.CARD_UNKNOWN;
                }

                // Create output string
                final String outputString = "Slot " + slotNum + ": "
                        + stateStrings[prevState] + " -> "
                        + stateStrings[currState];

                // Show output
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        logMsg(outputString);
                    }
                });
                if(currState==Reader.CARD_PRESENT)
                {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            playSound(context, 3, 1, 0);
                            if(!conf_MsgAValidar.equals(""))
                            {
                                tvValidar.setBackgroundColor(Color.TRANSPARENT);
                                tvValidar.setTextColor(Color.BLACK);
                                tvValidar.setText(conf_MsgAValidar);

//
//	            				tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
//	            				tvMsgLin1.setTextColor(Color.BLACK);
//	                        	tvMsgLin1.setText("\n");
//
//	            				tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
//	            				tvMsgLin2.setTextColor(Color.BLACK);
//	                        	tvMsgLin2.setText("");
//
//	            				tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
//	            				tvMsgLin3.setTextColor(Color.BLACK);
//	                        	tvMsgLin3.setText("");
//
//	            				tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
//	            				tvMsgLin4.setTextColor(Color.BLACK);
//	                        	tvMsgLin4.setText(conf_MsgAValidar);
                            }
                            MyPower();
                            MySetProtocol();
                            MyTransmit();
                        }
                    });
                }
            }
        });

        // Register receiver for USB permission
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

        // Initialize reader spinner
        mReaderAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        for (UsbDevice device : mManager.getDeviceList().values()) {
            if (mReader.isSupported(device)) {
//                mReaderAdapter.add(device.getDeviceName());
                MyAdapter = device.getDeviceName();
            }
        }
//        mReaderSpinner = (Spinner) findViewById(R.id.main_spinner_reader);
//        mReaderSpinner.setAdapter(mReaderAdapter);

        // Initialize slot spinner
//        mSlotAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
//        mSlotSpinner = (Spinner) findViewById(R.id.main_spinner_slot);
//        mSlotSpinner.setAdapter(mSlotAdapter);

        // Initialize power spinner
        ArrayAdapter<String> powerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, powerActionStrings);
//        mPowerSpinner = (Spinner) findViewById(R.id.main_spinner_power);
//        mPowerSpinner.setAdapter(powerAdapter);
//        mPowerSpinner.setSelection(Reader.CARD_WARM_RESET);

        // Initialize Show Log  button
        mShowLog = (TextView) findViewById(R.id.main_log);
        mShowLog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(1);
            }
        });

        // Initialize Show Configuration  button
        mShowLog = (TextView) findViewById(R.id.main_config);
        mShowLog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(0);
            }
        });
        // Initialize Resposta Sim  button
        RespSim = (TextView) findViewById(R.id.buttonsim);
        RespSim.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    fazResposta(MyCode, 1);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        // Initialize Resposta Nao  button
        RespNao = (TextView) findViewById(R.id.buttonnao);
        RespNao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    fazResposta(MyCode, 0);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });


        MudaCor(Posicao);

//		tvInOut = (TextView) findViewById(R.id.tvinout);
//
        initSounds(context);

//		conf_inout = "IN";


//		tvInOut.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
////				if(Posicao == 3 && ManualToken[0]==1 && ManualToken[1]==5 && ManualToken[2]==9)
//				{
//					if(inout == "IN")
//					{
//						inout = "OUT";
//						tvInOut.setText(R.string.out);
//					}
//					else
//					{
//						inout = "IN";
//						tvInOut.setText(R.string.in);
//					}
//				}
//			}
//		});

        logMsg("Inicio ........................");

        //FAZ um Open

        MyOpen();

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    fazClick(0);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(1);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(2);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(3);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(4);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(5);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(6);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(7);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(8);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fazClick(9);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });



    }
    private void WriteConfig(String Type) {

        BufferedWriter bufferedWriter;

        if(Type.equals("INIT")) {
            try {

                bufferedWriter = new BufferedWriter(new OutputStreamWriter( new FileOutputStream( new File(getFilesDir()+File.separator+FileName)), "ISO-8859-1"));

                bufferedWriter.write("ServerIP               :192.168.2.104"+System.getProperty ("line.separator"));
                bufferedWriter.write("ServerPort             :3334"+System.getProperty ("line.separator"));
                bufferedWriter.write("Posto                  :0001"+System.getProperty ("line.separator"));
                bufferedWriter.write("EntradaSaida           :" + conf_inout+System.getProperty ("line.separator"));
                bufferedWriter.write("Keyboard               :SIM"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgAValidar            :A validar..."+System.getProperty ("line.separator"));
                // Entradas
                bufferedWriter.write("MsgEntradasOK          :Bem-vindo"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgEntradasKO          :Dirija-se ao balc�o"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgEntradasShow        :SIM"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgEntradasMuda        :SIM"+System.getProperty ("line.separator"));
                // Saidas
                bufferedWriter.write("MsgSaidasOK            :Obrigado pela visita"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSaidasKO            :Dirija-se ao balc�o"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSaidasShow          :SIM"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSaidasMuda          :SIM"+System.getProperty ("line.separator"));
                // SoEntradas
                bufferedWriter.write("MsgSoEntradasOK        :"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSoEntradasKO        :Dirija-se ao balc�o"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSoEntradasShow      :SIM"+System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSoEntradasMuda      :SIM"+System.getProperty ("line.separator"));

                bufferedWriter.write("PerguntaFinal          :NAO"+System.getProperty ("line.separator"));
                bufferedWriter.write("EspacamentoTopTeclado  :" + "300" + System.getProperty ("line.separator"));
                bufferedWriter.write("EspacamentoTopMsg      :" + "50" + System.getProperty ("line.separator"));
                bufferedWriter.write("TamanhoLetraMsg        :" + "12" + System.getProperty ("line.separator"));

                bufferedWriter.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if(Type.equals("UPDATE")) {
            try {

                bufferedWriter = new BufferedWriter(new OutputStreamWriter( new FileOutputStream( new File(getFilesDir()+File.separator+FileName)), "ISO-8859-1"));
                bufferedWriter.write("ServerIP               :" + conf_serverIp + System.getProperty ("line.separator"));
                bufferedWriter.write("ServerPort             :" + conf_serverPort + System.getProperty ("line.separator"));
                bufferedWriter.write("Posto                  :" + conf_postId + System.getProperty ("line.separator"));
                bufferedWriter.write("EntradaSaida           :" + conf_inout + System.getProperty ("line.separator"));
                bufferedWriter.write("Keyboard                :" + conf_Keyboard + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgAValidar            :" + conf_MsgAValidar + System.getProperty ("line.separator"));
                // Entradas
                bufferedWriter.write("MsgEntradasOK          :" + conf_MsgEntradasOK + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgEntradasKO          :" + conf_MsgEntradasKO + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgEntradasShow        :" + conf_MsgEntradasShow + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgEntradasMuda        :" + conf_MsgEntradasMuda + System.getProperty ("line.separator"));
                // Saidas
                bufferedWriter.write("MsgSaidasOK            :" + conf_MsgSaidasOK + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSaidasKO            :" + conf_MsgSaidasKO + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSaidasShow          :" + conf_MsgSaidasShow + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSaidasMuda          :" + conf_MsgSaidasMuda + System.getProperty ("line.separator"));
                // SoEntradas
                bufferedWriter.write("MsgSoEntradasOK        :" + conf_MsgSoEntradasOK + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSoEntradasKO        :" + conf_MsgSoEntradasKO + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSoEntradasShow      :" + conf_MsgSoEntradasShow + System.getProperty ("line.separator"));
                bufferedWriter.write("MsgSoEntradasMuda      :" + conf_MsgSoEntradasMuda + System.getProperty ("line.separator"));

                bufferedWriter.write("PerguntaFinal          :" + conf_PerguntaFinal + System.getProperty ("line.separator"));
                bufferedWriter.write("EspacamentoTopTeclado  :" + conf_EspacamentoTopTeclado + System.getProperty ("line.separator"));
                bufferedWriter.write("EspacamentoTopMsg      :" + conf_EspacamentoTopMsg + System.getProperty ("line.separator"));
                bufferedWriter.write("TamanhoLetraMsg        :" + conf_TamanhoLetraMsg + System.getProperty ("line.separator"));

                bufferedWriter.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // Mostrar o teclado
        if(conf_Keyboard.equals("SIM"))
        {
            LimpaEcran(1);
        }
        else
        {
            LimpaEcran(0);
        }

    }

    private int ReadConfig() {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        byte[] buffer = new byte[500];
        try {

            //WriteConfig("INIT");
            fis = new FileInputStream(getFilesDir()+File.separator+FileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(fis,"ISO-8859-1"));
            StringBuilder total = new StringBuilder();


            String Linha;
            while ((Linha = r.readLine()) != null) {
//				String Linha = dis.readLine();
                if(Linha.toUpperCase().contains("SERVERIP"))
                {
                    conf_serverIp=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("SERVERPORT"))
                {
                    conf_serverPort=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("POSTO"))
                {
                    conf_postId=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }

                if(Linha.toUpperCase().contains("KEYBOARD"))
                {
                    conf_Keyboard=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("ENREADASAIDA"))
                {
                    conf_inout=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGAVALIDAR"))
                {
                    conf_MsgAValidar=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }

                if(Linha.toUpperCase().contains("MSGENTRADASOK"))
                {
                    conf_MsgEntradasOK=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGENTRADASKO"))
                {
                    conf_MsgEntradasKO=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGENTRADASSHOW"))
                {
                    conf_MsgEntradasShow=(String)Linha.substring(Linha.indexOf(":")+1, Linha.length());
//					conf_MsgEntradasShow="SIM";
                }
                if(Linha.toUpperCase().contains("MSGENTRADASMUDA"))
                {
                    conf_MsgEntradasMuda=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }

                if(Linha.toUpperCase().contains("MSGSAIDASOK"))
                {
                    conf_MsgSaidasOK=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGSAIDASKO"))
                {
                    conf_MsgSaidasKO=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGSAIDASSHOW"))
                {
                    conf_MsgSaidasShow=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGSAIDASMUDA"))
                {
                    conf_MsgSaidasMuda=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }


                if(Linha.toUpperCase().contains("MSGSOENTRADASOK"))
                {
                    conf_MsgSoEntradasOK=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGSOENTRADASKO"))
                {
                    conf_MsgSoEntradasKO=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGSOENTRADASSHOW"))
                {
                    conf_MsgSoEntradasShow=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("MSGSOENTRADASMUDA"))
                {
                    conf_MsgSoEntradasMuda=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("PERGUNTAFINAL"))
                {
                    conf_PerguntaFinal=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("ESPACAMENTOTOPTECLADO"))
                {
                    conf_EspacamentoTopTeclado=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("ESPACAMENTOTOPMSG"))
                {
                    conf_EspacamentoTopMsg=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }
                if(Linha.toUpperCase().contains("TAMANHOLETRAMSG"))
                {
                    conf_TamanhoLetraMsg=Linha.substring(Linha.indexOf(":")+1, Linha.length());
                }

            }
        } catch (FileNotFoundException e) {

            WriteConfig("INIT");
            return 1;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void MyOpen() {
        boolean requested = false;

        // Disable open button
//        mOpenButton.setEnabled(false);

//        String deviceName = (String) mReaderSpinner.getSelectedItem();
        String deviceName = MyAdapter;

        if (deviceName != null) {

            // For each device
            for (UsbDevice device : mManager.getDeviceList().values()) {

                // If device name is found
                if (deviceName.equals(device.getDeviceName())) {

                    // Request permission
                    mManager.requestPermission(device,
                            mPermissionIntent);

                    requested = true;
                    break;
                }
            }
        }

        if (!requested) {

            // Enable open button
//            mOpenButton.setEnabled(true);
        }

    }

    private void MyPower() {
        // Get slot number
//      int slotNum = mSlotSpinner.getSelectedItemPosition();
        int slotNum = MySlot;

        // Get action number
//      int actionNum = mPowerSpinner.getSelectedItemPosition();
        int actionNum = Reader.CARD_WARM_RESET;

        // If slot and action are selected
        if (slotNum != Spinner.INVALID_POSITION
                && actionNum != Spinner.INVALID_POSITION) {

            if (actionNum < Reader.CARD_POWER_DOWN
                    || actionNum > Reader.CARD_WARM_RESET) {
                actionNum = Reader.CARD_WARM_RESET;
            }

            // Set parameters
            PowerParams params = new PowerParams();
            params.slotNum = slotNum;
            params.action = actionNum;

            // Perform power action
            logMsg("Slot " + slotNum + ": " + powerActionStrings[actionNum] + "...");
            logMsg("POWER action Done. Inicio POWERTASK.");
            new PowerTask().execute(params);
        }

    }

    private void MySetProtocol() {
        // Get slot number
//      int slotNum = mSlotSpinner.getSelectedItemPosition();
        int slotNum = MySlot;

        // If slot is selected
        if (slotNum != Spinner.INVALID_POSITION) {

            int preferredProtocols = Reader.PROTOCOL_UNDEFINED;
            String preferredProtocolsString = "";

            preferredProtocols |= Reader.PROTOCOL_T1;
            if (preferredProtocolsString != "") {
                preferredProtocolsString += "/";
            }

            preferredProtocolsString += "T=1";
//            }

            if (preferredProtocolsString == "") {
                preferredProtocolsString = "None";
            }

            // Set Parameters
            SetProtocolParams params = new SetProtocolParams();
            params.slotNum = slotNum;
            params.preferredProtocols = preferredProtocols;

            // Set protocol
            logMsg("Slot " + slotNum + ": Setting protocol to "
                    + preferredProtocolsString + "...");
            new SetProtocolTask().execute(params);
        }

    }

    private void MyTransmit() {

        // Get slot number
//      int slotNum = mSlotSpinner.getSelectedItemPosition();
        int slotNum = MySlot;

        // If slot is selected
        if (slotNum != Spinner.INVALID_POSITION) {

            // Set parameters
            TransmitParams params = new TransmitParams();
            params.slotNum = slotNum;
            params.controlCode = -1;
            params.commandString = commandAPDU;

            // Transmit APDU
            logMsg("Slot " + slotNum + ": Transmitting APDU...");
            new TransmitTask().execute(params);
        }

    }

    private void ShowUsbControls(int i) {
        // Mostra controles de control do usb
        if(i == 1)
        {
//			mReaderSpinner.setVisibility(View.VISIBLE);
//			mSlotSpinner.setVisibility(View.VISIBLE);
//			mPowerSpinner.setVisibility(View.VISIBLE);
        }
        else
        {
            // Esconde controles de control do usb
//			mReaderSpinner.setVisibility(View.INVISIBLE);
//			mSlotSpinner.setVisibility(View.INVISIBLE);
//			mPowerSpinner.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onDestroy() {

        // Close reader
        mReader.close();

        // Unregister receiver
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        LayoutInflater inflater;
        final View layout;
        AlertDialog.Builder builder;
        AlertDialog dialog=null;

        switch (id) {

            case DIALOG_LOG:
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.log_dialog,(ViewGroup) findViewById(R.id.log_view));

                builder = new AlertDialog.Builder(this);
                builder.setView(layout);
                builder.setTitle("Log");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText editText;
                                byte[] buffer;

                                dialog.dismiss();
                            }
                        });

                dialog = builder.create();

                // Hide input window
                dialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case DIALOG_CONFIG:
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.config_dialog,(ViewGroup) findViewById(R.id.config_view));

                builder = new AlertDialog.Builder(this);
                builder.setView(layout);
                builder.setTitle("Configuration");
                builder.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                conf_MsgAValidar = editTexta_validar.getText().toString();
                                conf_serverIp= editTextserverip.getText().toString();
                                conf_serverPort = editTextserverport.getText().toString();
                                if(checkBoxKeyboard.isChecked())
                                {
                                    conf_Keyboard = "SIM";
                                    LimpaEcran(1);
                                }
                                else
                                {
                                    conf_Keyboard = "NAO";
                                    LimpaEcran(0);
                                }
                                conf_MsgEntradasOK= editTextMsgEntradasOK.getText().toString();
                                conf_MsgEntradasKO = editTextMsgEntradasKO.getText().toString();
                                if(checkBoxShowEntradas.isChecked())
                                {
                                    conf_MsgEntradasShow = "SIM";
                                }
                                else
                                {
                                    conf_MsgEntradasShow = "NAO";
                                }
                                if(checkBoxMudaEntradas.isChecked())
                                {
                                    conf_MsgEntradasMuda = "SIM";
                                }
                                else
                                {
                                    conf_MsgEntradasMuda = "NAO";
                                }
                                conf_MsgSaidasOK = editTextMsgSaidasOK.getText().toString();
                                conf_MsgSaidasKO = editTextMsgSaidasKO.getText().toString();
                                if(checkBoxShowSaidas.isChecked())
                                {
                                    conf_MsgSaidasShow = "SIM";
                                }
                                else
                                {
                                    conf_MsgSaidasShow = "NAO";
                                }
                                if(checkBoxMudaSaidas.isChecked())
                                {
                                    conf_MsgSaidasMuda = "SIM";
                                }
                                else
                                {
                                    conf_MsgSaidasMuda = "NAO";
                                }
                                conf_MsgSoEntradasOK = editTextMsgSoEntradasOK.getText().toString();
                                conf_MsgSoEntradasKO = editTextMsgSoEntradasKO.getText().toString();

                                conf_EspacamentoTopTeclado = editTextEspacamentoTopTeclado.getText().toString();
                                conf_EspacamentoTopMsg = editTextEspacamentoTopMsg.getText().toString();
                                conf_TamanhoLetraMsg = editTextTamanhoLetraMsg.getText().toString();

                                if(checkBoxShowSoEntradas.isChecked())
                                {
                                    conf_MsgSoEntradasShow = "SIM";
                                }
                                else
                                {
                                    conf_MsgSoEntradasShow = "NAO";
                                }
                                if(checkBoxMudaSoEntradas.isChecked())
                                {
                                    conf_MsgSoEntradasMuda = "SIM";
                                }
                                else
                                {
                                    conf_MsgSoEntradasMuda = "NAO";
                                }

                                WriteConfig("UPDATE");

                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                dialog = builder.create();

                // Hide input window
                dialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;

        }

        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        switch (id) {
            case DIALOG_CONFIG:
                // Inicializa bot�o entradas e saidas
                mInOutButton = (Button) dialog.findViewById(R.id.main_button_inout);
                if(conf_inout.equals("IN"))
                {
                    mInOutButton.setText(R.string.in);
                }
                else if(conf_inout.equals("OUT"))
                {
                    mInOutButton.setText(R.string.out);
                }
                else if(conf_inout.equals("SOIN"))
                {
                    mInOutButton.setText(R.string.soin);
                }

                mInOutButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(conf_inout.equals("IN"))
                        {
                            conf_inout = "OUT";
                            mInOutButton.setText(R.string.out);
                        }
                        else if(conf_inout.equals("OUT"))
                        {
                            conf_inout = "SOIN";
                            mInOutButton.setText(R.string.soin);
                        }
                        else if(conf_inout.equals("SOIN"))
                        {
                            conf_inout = "IN";
                            mInOutButton.setText(R.string.in);
                        }
                    }
                });
                // Inicializa edit text s das configura��es
                editTexta_validar = (EditText) dialog.findViewById(R.id.config_dialog_edit_text_a_validar);
                editTextserverip = (EditText) dialog.findViewById(R.id.config_dialog_edit_text_serverip);
                editTextserverport = (EditText) dialog.findViewById(R.id.config_dialog_edit_text_serverport);
                checkBoxKeyboard = (CheckBox) dialog.findViewById(R.id.keyboard);
                editTextMsgEntradasOK = (EditText) dialog.findViewById(R.id.modify_pin_dialog_edit_text_in_ok);
                editTextMsgEntradasKO = (EditText) dialog.findViewById(R.id.modify_pin_dialog_edit_text_in_ko);
                checkBoxShowEntradas = (CheckBox) dialog.findViewById(R.id.modify_pin_dialog_edit_text_in_mostra);
                checkBoxMudaEntradas = (CheckBox) dialog.findViewById(R.id.modify_pin_dialog_edit_text_in_muda);

                editTextMsgSaidasOK = (EditText) dialog.findViewById(R.id.modify_pin_dialog_edit_text_out_ok);
                editTextMsgSaidasKO = (EditText) dialog.findViewById(R.id.modify_pin_dialog_edit_text_out_ko);
                checkBoxShowSaidas = (CheckBox) dialog.findViewById(R.id.modify_pin_dialog_edit_text_out_mostra);
                checkBoxMudaSaidas = (CheckBox) dialog.findViewById(R.id.modify_pin_dialog_edit_text_out_muda);

                editTextMsgSoEntradasOK = (EditText) dialog.findViewById(R.id.modify_pin_dialog_edit_text_soin_ok);
                editTextMsgSoEntradasKO = (EditText) dialog.findViewById(R.id.modify_pin_dialog_edit_text_soin_ko);
                checkBoxShowSoEntradas = (CheckBox) dialog.findViewById(R.id.modify_pin_dialog_edit_text_soin_mostra);
                checkBoxMudaSoEntradas = (CheckBox) dialog.findViewById(R.id.modify_pin_dialog_edit_text_soin_muda);

                editTextEspacamentoTopTeclado = (EditText) dialog.findViewById(R.id.edit_text_espacamentotopteclado);
                editTextEspacamentoTopMsg = (EditText) dialog.findViewById(R.id.edit_text_espacamentotopmsg);
                editTextTamanhoLetraMsg = (EditText) dialog.findViewById(R.id.edit_text_tamanholetramsg);

                editTexta_validar.setText(conf_MsgAValidar);
                editTextserverip.setText(conf_serverIp);
                editTextserverport.setText(conf_serverPort);
                editTextMsgEntradasOK.setText(conf_MsgEntradasOK);
                editTextMsgEntradasKO.setText(conf_MsgEntradasKO);
                if(conf_MsgEntradasShow.equals("SIM") )
                {
                    checkBoxShowEntradas.setChecked(true);
                }
                else
                {
                    checkBoxShowEntradas.setChecked(false);
                }
                if(conf_MsgEntradasMuda.equals("SIM"))
                {
                    checkBoxMudaEntradas.setChecked(true);
                }
                else
                {
                    checkBoxMudaEntradas.setChecked(false);
                }
                editTextMsgSaidasOK.setText(conf_MsgSaidasOK);
                editTextMsgSaidasKO.setText(conf_MsgSaidasKO);
                if(conf_MsgSaidasShow.equals("SIM"))
                {
                    checkBoxShowSaidas.setChecked(true);
                }
                else
                {
                    checkBoxShowSaidas.setChecked(false);
                }
                if(conf_MsgSaidasMuda.equals("SIM"))
                {
                    checkBoxMudaSaidas.setChecked(true);
                }
                else
                {
                    checkBoxMudaSaidas.setChecked(false);
                }
                editTextMsgSoEntradasOK.setText(conf_MsgSoEntradasOK);
                editTextMsgSoEntradasKO.setText(conf_MsgSoEntradasKO);

                editTextEspacamentoTopTeclado.setText(conf_EspacamentoTopTeclado);
                editTextEspacamentoTopMsg.setText(conf_EspacamentoTopMsg);
                editTextTamanhoLetraMsg.setText(conf_TamanhoLetraMsg);

                if(conf_MsgSoEntradasShow.equals("SIM"))
                {
                    checkBoxShowSoEntradas.setChecked(true);
                }
                else
                {
                    checkBoxShowSoEntradas.setChecked(false);
                }
                if(conf_MsgSoEntradasMuda.equals("SIM"))
                {
                    checkBoxMudaSoEntradas.setChecked(true);
                }
                else
                {
                    checkBoxMudaSoEntradas.setChecked(false);
                }
                if(conf_Keyboard.equals("SIM"))
                {
                    checkBoxKeyboard.setChecked(true);
                }
                else
                {
                    checkBoxKeyboard.setChecked(false);
                }

                break;
            case DIALOG_LOG:

                tvIps = (TextView) dialog.findViewById(R.id.tvips);
                tvIps.setText("Version : " + APP_VERSION + " " + getResources().getString(R.string.local_ip) + clientIp + " - " + getResources().getString(R.string.server_ip) + conf_serverIp );
                tvUsb = (TextView) dialog.findViewById(R.id.tvusb);
                tvUsb.setText("Adapter - " + MyAdapter + " Slot - " + MySlot);

                mDialogResponseTextView = (TextView) dialog.findViewById(R.id.config_dialog_response);
                mDialogResponseTextView.setMovementMethod(new ScrollingMovementMethod());
                mDialogResponseTextView.setMaxLines(MAX_LINES);

                mDialogResponseTextView.setText(MyLog);

                if (mDialogResponseTextView.getLineCount() > MAX_LINES) {
                    mDialogResponseTextView.scrollTo(0,
                            (mDialogResponseTextView.getLineCount() - MAX_LINES)* mDialogResponseTextView.getLineHeight());
                }
            default:
                break;
        }
    }

    @SuppressWarnings("deprecation")

    private Drawable LoadBackground1()
    {
        Drawable Background1=null;
        File imgFile = new  File("/sdcard/Android/background1.jpg");
        if(imgFile.exists()){
            Background1 = new BitmapDrawable(getResources(),BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            logMsg("Encontrou o ficheiro Background1 !");
        }
        else
        {
            Background1 = (BitmapDrawable) getResources().getDrawable(R.drawable.background2); //new Image that was added to the res folder
            logMsg("Não Encontrou o ficheiro /sdcard/Android/Background1 !");
        }
        return Background1;
    };
    private Drawable LoadBackground2()
    {
        Drawable Background2=null;
        File imgFile = new  File("/sdcard/Android/background2.jpg");
        if(imgFile.exists()){
            Background2 = new BitmapDrawable(getResources(),BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            logMsg("Encontrou o ficheiro Background2 !");
        }
        else
        {
            Background2 = (BitmapDrawable) getResources().getDrawable(R.drawable.background2); //new Image that was added to the res folder
            logMsg("Não Encontrou o ficheiro /sdcard/Android/Background2 !");
        }
        return Background2;
    };
    private Drawable LoadBackground3()
    {
        Drawable Background3=null;
        File imgFile = new  File("/sdcard/Android/background3.jpg");
        if(imgFile.exists()){
            Background3 = new BitmapDrawable(getResources(),BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            logMsg("Encontrou o ficheiro Background3 !");
        }
        else
        {
            Background3 = (BitmapDrawable) getResources().getDrawable(R.drawable.background3); //new Image that was added to the res folder
            logMsg("Não Encontrou o ficheiro /sdcard/Android/Background3 !");
        }
        return Background3;
    };

    private void MySetBackGround(String WallPaper) {
        try {
            if(WallPaper.equals("1"))
            {
                LinearLayout layout =(LinearLayout)findViewById(R.id.main_all);
                // Retirar para poder testar virtualmente
                layout.setBackground(Bg1);
            }
            if(WallPaper.equals("2"))
            {
                LinearLayout layout =(LinearLayout)findViewById(R.id.main_all);
                // Retirar para poder testar virtualmente
                layout.setBackground(Bg2);
            }
            if(WallPaper.equals("3"))
            {
                LinearLayout layout =(LinearLayout)findViewById(R.id.main_all);
                // Retirar para poder testar virtualmente
                layout.setBackground(Bg3);
            }
        } catch (Exception e) {
            logMsg("Erro a mudar de wallpaper");
        }
    }

    /**
     * Logs the message.
     *
     * @param msg
     *            the message.
     */
    private void logMsg(String msg) {

        DateFormat dateFormat = new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss]: ");
        Date date = new Date();

        MyLog = MyLog + "\n" + dateFormat.format(date) + msg;
        if(MyLog.length()>3000)
        {
            MyLog="Log cortado ---------------";
        }
    }

    /**
     * Logs the contents of buffer.
     *
     * @param buffer
     *            the buffer.
     * @param bufferLength
     *            the buffer length.
     */
    private void logBuffer(byte[] buffer, int bufferLength) {

        String bufferString = "";

        for (int i = 0; i < bufferLength; i++) {

            String hexChar = Integer.toHexString(buffer[i] & 0xFF);
            if (hexChar.length() == 1) {
                hexChar = "0" + hexChar;
            }

            if (i % 16 == 0) {

                if (bufferString != "") {

                    logMsg(bufferString);
                    bufferString = "";
                }
            }

            bufferString += hexChar.toUpperCase() + " ";
        }

        if (bufferString != "") {
            logMsg(bufferString);
        }

    }

    /**
     * Converts the HEX string to byte array.
     *
     * @param hexString
     *            the HEX string.
     * @return the byte array.
     */
    private byte[] toByteArray(String hexString) {

        int hexStringLength = hexString.length();
        byte[] byteArray = null;
        int count = 0;
        char c;
        int i;

        // Count number of hex characters
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f') {
                count++;
            }
        }

        byteArray = new byte[(count + 1) / 2];
        boolean first = true;
        int len = 0;
        int value;
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else if (c >= 'A' && c <= 'F') {
                value = c - 'A' + 10;
            } else if (c >= 'a' && c <= 'f') {
                value = c - 'a' + 10;
            } else {
                value = -1;
            }

            if (value >= 0) {

                if (first) {

                    byteArray[len] = (byte) (value << 4);

                } else {

                    byteArray[len] |= value;
                    len++;
                }

                first = !first;
            }
        }

        return byteArray;
    }

    /**
     * Converts the integer to HEX string.
     *
     * @param i
     *            the integer.
     * @return the HEX string.
     */
    private String toHexString(int i) {

        String hexString = Integer.toHexString(i);
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }

        return hexString.toUpperCase();
    }

    /**
     * Converts the byte array to HEX string.
     *
     * @param buffer
     *            the buffer.
     * @return the HEX string.
     */
    private String toHexString(byte[] buffer) {

        String bufferString = "";

        for (int i = 0; i < buffer.length; i++) {

            String hexChar = Integer.toHexString(buffer[i] & 0xFF);
            if (hexChar.length() == 1) {
                hexChar = "0" + hexChar;
            }

            bufferString += hexChar.toUpperCase() + " ";
        }

        return bufferString;
    }

    private void checkResponseForIntentUserAnswer(String response) throws JSONException {

        tvMsgLin1.setText("");
        tvMsgLin2.setText("");
        tvMsgLin3.setText("");
        tvMsgLin4.setText("");
        MySetBackGround("1");
        if(conf_Keyboard.equals("SIM"))
        {
            LimpaEcran(1);
        }
        else
        {
            LimpaEcran(0);
        }

    }


    private void checkResponseForIntent(String response) throws JSONException {

        try {
            //		JSONArray oneObject = new JSONArray(response);
            // Pulling items from the array
            //	    String oneObjectsItem = oneObject.getString("ResponseCode");
            //	    String oneObjectsItem2 = oneObject.getString("ResponseDescription");
            //		tvNfcLabel.setText( oneObjectsItem + " " + oneObjectsItem2);

            //Parse do ResponseUser
            String Resp = response.substring(response.indexOf(":")+1, response.indexOf(","));

            String NomeParcial = response.substring(response.indexOf("ResponseUser")+17 , response.length());
            String Nome = NomeParcial.substring(0, NomeParcial.indexOf("\\"));
            //Parse do ResponseDescrition
            String MsgCustBDParcial = response.substring(response.indexOf("ResponseDescrition")+23 , response.length());
            String MsgCustBD = MsgCustBDParcial.substring(0, MsgCustBDParcial.indexOf("\\"));

            //Parse do ResponseCompany
            String MsgResponseCompany = response.substring(response.indexOf("ResponseCompany")+20 , response.length());
            MyCode = MsgResponseCompany.substring(0, MsgResponseCompany.indexOf("\\"));

            logMsg("Resposta do WebService : " + response);

            // limpa msg de a Validar
            tvValidar.setText("");

            if(Resp.equals( "0" )) {
                LimpaEcran(0);
                if(conf_inout.equals("IN")) {
                    if(conf_MsgEntradasMuda.equals("SIM"))
                    {
                        MySetBackGround("2");
                    }
                    if(conf_MsgEntradasShow.equals("SIM")) {
//						tvMsgLin1.setPadding(5, 5, 5, 5);
//						tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
//						tvMsgLin1.setTextColor(Color.BLACK);
//						tvMsgLin1.setText(conf_MsgEntradasOK);
//
//						tvMsgLin2.setPadding(5, 5, 5, 5);
//						tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
//						tvMsgLin2.setTextColor(Color.BLACK);
//						tvMsgLin2.setText(Nome);
// ZON OPTIMUS
                        tvMsgLin1.setPadding(5, 5, 5, 5);
                        tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin1.setTextColor(Color.WHITE);
                        tvMsgLin1.setText(conf_MsgEntradasOK);

                        tvMsgLin2.setPadding(5, 5, 5, 5);
                        tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin2.setTextColor(Color.WHITE);
                        tvMsgLin2.setText(Nome);

                        tvMsgLin3.setPadding(5, 5, 5, 5);
                        tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin3.setTextColor(Color.WHITE);
                        tvMsgLin3.setText(MsgCustBD);

                        tvMsgLin4.setPadding(5, 5, 5, 5);
                        tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin4.setTextColor(Color.WHITE);
                        tvMsgLin4.setText("");
                    }
                    else
                    {
                        tvMsgLin1.setPadding(5, 5, 5, 5);
                        tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin1.setTextColor(Color.BLACK);
                        tvMsgLin1.setText("");

                        tvMsgLin2.setPadding(5, 5, 5, 5);
                        tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin2.setTextColor(Color.BLACK);
                        tvMsgLin2.setText("");

                        tvMsgLin3.setPadding(5, 5, 5, 5);
                        tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin3.setTextColor(Color.BLACK);
                        tvMsgLin3.setText("");

                        tvMsgLin4.setPadding(5, 5, 5, 5);
                        tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin4.setTextColor(Color.BLACK);
                        tvMsgLin4.setText("");
                    }
                }
                if(conf_inout.equals("OUT")) {
                    if(conf_MsgSaidasMuda.equals("SIM"))
                    {
                        MySetBackGround("2");
                    }
                    if(conf_MsgSaidasShow.equals("SIM")) {
                        tvMsgLin1.setPadding(5, 5, 5, 5);
                        tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin1.setTextColor(Color.BLACK);
                        tvMsgLin1.setText("");

                        tvMsgLin2.setPadding(5, 5, 5, 5);
                        tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin2.setTextColor(Color.BLACK);
                        tvMsgLin2.setText("conf_MsgSaidasOK");

                        tvMsgLin3.setPadding(5, 5, 5, 5);
                        tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin3.setTextColor(Color.BLACK);
                        tvMsgLin3.setText(MsgCustBD);

                        tvMsgLin4.setPadding(5, 5, 5, 5);
                        tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin4.setTextColor(Color.BLACK);
                        tvMsgLin4.setText(Nome);
                    }
                    else
                    {
                        tvMsgLin1.setPadding(5, 5, 5, 5);
                        tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin1.setTextColor(Color.BLACK);
                        tvMsgLin1.setText("");

                        tvMsgLin2.setPadding(5, 5, 5, 5);
                        tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin2.setTextColor(Color.BLACK);
                        tvMsgLin2.setText("");

                        tvMsgLin3.setPadding(5, 5, 5, 5);
                        tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin3.setTextColor(Color.BLACK);
                        tvMsgLin3.setText("");

                        tvMsgLin4.setPadding(5, 5, 5, 5);
                        tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin4.setTextColor(Color.BLACK);
                        tvMsgLin4.setText("");
                    }

                }
                if(conf_inout.equals("SOIN")) {
                    if(conf_MsgSoEntradasMuda.equals("SIM"))
                    {
                        MySetBackGround("2");
                    }
                    if(conf_MsgSoEntradasShow.equals("SIM")) {
//						tvMsgLin1.setPadding(5, 5, 5, 5);
//						tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
//						tvMsgLin1.setTextColor(Color.BLACK);
//						tvMsgLin1.setText(conf_MsgSoEntradasOK);
//
//						tvMsgLin2.setPadding(5, 5, 5, 5);
//						tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
//						tvMsgLin2.setTextColor(Color.BLACK);
//						tvMsgLin2.setText(Nome);
// ZON OPTIMUS
                        tvMsgLin1.setPadding(5, 5, 5, 5);
                        tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin1.setTextColor(Color.WHITE);
                        tvMsgLin1.setText(conf_MsgSoEntradasOK);

                        tvMsgLin2.setPadding(5, 5, 5, 5);
                        tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin2.setTextColor(Color.WHITE);
                        tvMsgLin2.setText(Nome);

                        tvMsgLin3.setPadding(5, 5, 5, 5);
                        tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin3.setTextColor(Color.WHITE);
                        tvMsgLin3.setText(MsgCustBD);

                        tvMsgLin4.setPadding(5, 5, 5, 5);
                        tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin4.setTextColor(Color.WHITE);
                        tvMsgLin4.setText("");
                    }
                    else
                    {
                        tvMsgLin1.setPadding(5, 5, 5, 5);
                        tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin1.setTextColor(Color.WHITE);
                        tvMsgLin1.setText("");

                        tvMsgLin2.setPadding(5, 5, 5, 5);
                        tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin2.setTextColor(Color.WHITE);
                        tvMsgLin2.setText("");

                        tvMsgLin3.setPadding(5, 5, 5, 5);
                        tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin3.setTextColor(Color.WHITE);
                        tvMsgLin3.setText("");

                        tvMsgLin4.setPadding(5, 5, 5, 5);
                        tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                        tvMsgLin4.setTextColor(Color.WHITE);
                        tvMsgLin4.setText("");
                    }
                }
            }
            if(!Resp.equals("0")) {
                if(conf_inout.equals("IN")) {
//					if(conf_MsgEntradasShow.equals("SIM")) {
                    tvMsgLin1.setPadding(5, 5, 5, 5);
                    tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin1.setTextColor(Color.BLACK);
                    tvMsgLin1.setText(conf_MsgEntradasKO);

                    tvMsgLin2.setPadding(5, 5, 5, 5);
                    tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin2.setTextColor(Color.BLACK);
                    tvMsgLin2.setText("");

                    tvMsgLin3.setPadding(5, 5, 5, 5);
                    tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin3.setTextColor(Color.BLACK);
                    tvMsgLin3.setText("");

                    tvMsgLin4.setPadding(5, 5, 5, 5);
                    tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin4.setTextColor(Color.BLACK);
                    tvMsgLin4.setText("");
//					}

                }
                if(conf_inout.equals("OUT")) {
//					if(conf_MsgSaidasShow.equals("SIM")) {
                    tvMsgLin1.setPadding(5, 5, 5, 5);
                    tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin1.setTextColor(Color.BLACK);
                    tvMsgLin1.setText("conf_MsgSaidasKO");

                    tvMsgLin2.setPadding(5, 5, 5, 5);
                    tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin2.setTextColor(Color.BLACK);
                    tvMsgLin2.setText("");

                    tvMsgLin3.setPadding(5, 5, 5, 5);
                    tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin3.setTextColor(Color.BLACK);
                    tvMsgLin3.setText("");

                    tvMsgLin4.setPadding(5, 5, 5, 5);
                    tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin4.setTextColor(Color.BLACK);
                    tvMsgLin4.setText("");
//					}

                }
                if(conf_inout.equals("SOIN")) {
//					if(conf_MsgSoEntradasShow.equals("SIM")) {
                    // Na SCC a msg de erro obriga a mudar o backgroud
                    MySetBackGround("3");
                    tvMsgLin1.setPadding(5, 5, 5, 5);
                    tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin1.setTextColor(Color.WHITE);
                    tvMsgLin1.setText(conf_MsgSoEntradasKO);

                    tvMsgLin2.setPadding(100, 5, 5, 5);
                    tvMsgLin2.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin2.setTextColor(Color.WHITE);
                    tvMsgLin2.setText("");

                    tvMsgLin3.setPadding(5, 5, 5, 5);
                    tvMsgLin3.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin3.setTextColor(Color.WHITE);
                    tvMsgLin3.setText("");

                    tvMsgLin4.setPadding(5, 5, 5, 5);
                    tvMsgLin4.setBackgroundColor(Color.TRANSPARENT);
                    tvMsgLin4.setTextColor(Color.WHITE);
                    tvMsgLin4.setText("");

//						Thread timer = new Thread() {
//						    public void run () {
//
//					            try {
//									Thread.sleep(3000);
//						            // do stuff in a separate thread
//						            uiCallback.sendEmptyMessage(0);
//								} catch (InterruptedException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}    // sleep for 3 seconds
//
//						    }
//						};
// 						timer.start();

                }

            }
//			if(Resp.equals( "-2"))
//			{
//				tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
//				tvMsgLin1.setTextColor(Color.WHITE);
//				tvMsgLin1.setText("Dirija-se ao balc�o");
//			}
//			if(Resp.equals( "-3"))
//			{
//				tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
//				tvMsgLin1.setTextColor(Color.WHITE);
//				tvMsgLin1.setText("Dirija-se ao balc�o");
//			}
//			if(Resp.equals( "-20"))
//			{
//				tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
//				tvMsgLin1.setTextColor(Color.WHITE);
//				tvMsgLin1.setText("Dirija-se ao balc�o");
//			}
//			if(Resp.equals( "-21"))
//			{
//				tvMsgLin1.setTextColor(Color.TRANSPARENT);
//				tvMsgLin1.setText("Dirija-se ao balc�o");
//			}
//			if(Resp.equals( "-22"))
//			{
//				tvMsgLin1.setBackgroundColor(Color.TRANSPARENT);
//				tvMsgLin1.setTextColor(Color.WHITE);
//				tvMsgLin1.setText("Dirija-se ao balc�o");
//			}

            //
            //   Temporizador de saida para IN OUT SOIN
            //
            if(conf_PerguntaFinal.equals("NAO"))
            {
                Thread timer = new Thread() {
                    public void run () {

                        try {
                            Thread.sleep(1000);
                            // do stuff in a separate thread
                            uiCallback.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }    // sleep for 3 seconds

                    }
                };
                timer.start();
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onResume() {
        super.onResume();
//		if(mAdapter!=null)mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
//				mTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();
//		if(mAdapter!=null)mAdapter.disableForegroundDispatch(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reader_test, menu);
        return true;
    }

    private Handler uiCallback = new Handler () {
        public void handleMessage (Message msg) {
            tvMsgLin1.setText("");
            tvMsgLin2.setText("");
            tvMsgLin3.setText("");
            tvMsgLin4.setText("");
            MySetBackGround("1");
            if(conf_Keyboard.equals("SIM"))
            {
                LimpaEcran(1);
            }
            else
            {
                LimpaEcran(0);
            }

        }
    };

    private Handler updateUIHandler=new Handler();

    private void exit() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.tips)
                .setMessage(R.string.confirm_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }


    private int fazClick(int botao)  {
        Posicao ++;
        ManualToken[8] = ManualToken[7];
        ManualToken[7] = ManualToken[6];
        ManualToken[6] = ManualToken[5];
        ManualToken[5] = ManualToken[4];
        ManualToken[4] = ManualToken[3];
        ManualToken[3] = ManualToken[2];
        ManualToken[2] = ManualToken[1];
        ManualToken[1] = ManualToken[0];
        ManualToken[0] = botao;
        MudaCor(Posicao);
//		Toca beep
        playSound(context, 3, 1, 0);

        if(Posicao==9)
        {
            CheckUser("1", Integer.toString(ManualToken[8]) +Integer.toString(ManualToken[7]) +Integer.toString(ManualToken[6]) +Integer.toString(ManualToken[5]) + Integer.toString(ManualToken[4])+ Integer.toString(ManualToken[3])+ Integer.toString(ManualToken[2])+ Integer.toString(ManualToken[1])+ Integer.toString(ManualToken[0]));
            Posicao=0;
            MudaCor(Posicao);
            LimpaEcran(0);
        }

        return 0;
    }
    private int fazResposta(String Code, int Resp)  {

        if(Resp==1)
        {

            String url = "http://"+ conf_serverIp +":" + conf_serverPort + "/PostService/ua?securityCode=" + SecurityCode + "&userCode=" + Code + "&answer=SIM";
            new MyAsyncTaskUserAnswer(this).execute(url);
        }
        else
        {
            String url = "http://"+ conf_serverIp +":" + conf_serverPort + "/PostService/ua?securityCode=" + SecurityCode + "&userCode=" + Code + "&answer=NAO";
            new MyAsyncTaskUserAnswer(this).execute(url);
        }


        return 0;
    }

    private void CheckUser(String TokenType, String Code )  {

        String url = "http://"+ conf_serverIp +":" + conf_serverPort + "/PostService/uc?securityCode=" + SecurityCode + "&userCode=" + Code + "&tokenType=" + TokenType + "&postId=" + conf_postId + "&clientIp=" + clientIp + "&inout=" + conf_inout;
        new MyAsyncTask(this).execute(url);


    }
    private void LimpaEcran(int tipo) {
        if(tipo==0)
        {
            // Faz o espaçamento para o topo quando não tem o teclado
            tvTopEspacamento.getLayoutParams().height = Integer.parseInt(conf_EspacamentoTopMsg);
            button0.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);
            button4.setVisibility(View.GONE);
            button5.setVisibility(View.GONE);
            button6.setVisibility(View.GONE);
            button7.setVisibility(View.GONE);
            button8.setVisibility(View.GONE);
            button9.setVisibility(View.GONE);

            tvManualToken0.setVisibility(View.GONE);
            tvManualToken1.setVisibility(View.GONE);
            tvManualToken2.setVisibility(View.GONE);
            tvManualToken3.setVisibility(View.GONE);
            tvManualToken4.setVisibility(View.GONE);
            tvManualToken5.setVisibility(View.GONE);
            tvManualToken6.setVisibility(View.GONE);
            tvManualToken7.setVisibility(View.GONE);
            tvManualToken8.setVisibility(View.GONE);

            tvValidar.setVisibility(View.GONE);
        }
        if(tipo==1)
        {
            // Faz o espaçamento para o topo quando tem o teclado
            tvTopEspacamento.getLayoutParams().height = Integer.parseInt(conf_EspacamentoTopTeclado);
            button0.setVisibility(View.VISIBLE);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
            button5.setVisibility(View.VISIBLE);
            button6.setVisibility(View.VISIBLE);
            button7.setVisibility(View.VISIBLE);
            button8.setVisibility(View.VISIBLE);
            button9.setVisibility(View.VISIBLE);

            tvManualToken0.setVisibility(View.VISIBLE);
            tvManualToken1.setVisibility(View.VISIBLE);
            tvManualToken2.setVisibility(View.VISIBLE);
            tvManualToken3.setVisibility(View.VISIBLE);
            tvManualToken4.setVisibility(View.VISIBLE);
            tvManualToken5.setVisibility(View.VISIBLE);
            tvManualToken6.setVisibility(View.VISIBLE);
            tvManualToken7.setVisibility(View.VISIBLE);
            tvManualToken8.setVisibility(View.VISIBLE);

            tvValidar.setVisibility(View.VISIBLE);
        }
    }
    private void MudaCor(int Posicao) {
        if(Posicao==0)
        {
            tvManualToken0.setTextColor(Color.WHITE);
            tvManualToken1.setTextColor(Color.WHITE);
            tvManualToken2.setTextColor(Color.WHITE);
            tvManualToken3.setTextColor(Color.WHITE);
            tvManualToken4.setTextColor(Color.WHITE);
            tvManualToken5.setTextColor(Color.WHITE);
            tvManualToken6.setTextColor(Color.WHITE);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==1)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.WHITE);
            tvManualToken2.setTextColor(Color.WHITE);
            tvManualToken3.setTextColor(Color.WHITE);
            tvManualToken4.setTextColor(Color.WHITE);
            tvManualToken5.setTextColor(Color.WHITE);
            tvManualToken6.setTextColor(Color.WHITE);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==2)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.WHITE);
            tvManualToken3.setTextColor(Color.WHITE);
            tvManualToken4.setTextColor(Color.WHITE);
            tvManualToken5.setTextColor(Color.WHITE);
            tvManualToken6.setTextColor(Color.WHITE);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==3)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.RED);
            tvManualToken3.setTextColor(Color.WHITE);
            tvManualToken4.setTextColor(Color.WHITE);
            tvManualToken5.setTextColor(Color.WHITE);
            tvManualToken6.setTextColor(Color.WHITE);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==4)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.RED);
            tvManualToken3.setTextColor(Color.RED);
            tvManualToken4.setTextColor(Color.WHITE);
            tvManualToken5.setTextColor(Color.WHITE);
            tvManualToken6.setTextColor(Color.WHITE);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==5)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.RED);
            tvManualToken3.setTextColor(Color.RED);
            tvManualToken4.setTextColor(Color.RED);
            tvManualToken5.setTextColor(Color.WHITE);
            tvManualToken6.setTextColor(Color.WHITE);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==6)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.RED);
            tvManualToken3.setTextColor(Color.RED);
            tvManualToken4.setTextColor(Color.RED);
            tvManualToken5.setTextColor(Color.RED);
            tvManualToken6.setTextColor(Color.WHITE);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==7)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.RED);
            tvManualToken3.setTextColor(Color.RED);
            tvManualToken4.setTextColor(Color.RED);
            tvManualToken5.setTextColor(Color.RED);
            tvManualToken6.setTextColor(Color.RED);
            tvManualToken7.setTextColor(Color.WHITE);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==8)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.RED);
            tvManualToken3.setTextColor(Color.RED);
            tvManualToken4.setTextColor(Color.RED);
            tvManualToken5.setTextColor(Color.RED);
            tvManualToken6.setTextColor(Color.RED);
            tvManualToken7.setTextColor(Color.RED);
            tvManualToken8.setTextColor(Color.WHITE);
        }
        if(Posicao==9)
        {
            tvManualToken0.setTextColor(Color.RED);
            tvManualToken1.setTextColor(Color.RED);
            tvManualToken2.setTextColor(Color.RED);
            tvManualToken3.setTextColor(Color.RED);
            tvManualToken4.setTextColor(Color.RED);
            tvManualToken5.setTextColor(Color.RED);
            tvManualToken6.setTextColor(Color.RED);
            tvManualToken7.setTextColor(Color.RED);
            tvManualToken8.setTextColor(Color.RED);
        }

    }
    public String getLocalIpAddress()
    {
        try {
            String ipv4="";
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {

                        String ip = inetAddress.getHostAddress().toString();
                        System.out.println("ip---::" + ip);

                        return ipv4;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }
///** Populate the SoundPool*/

    public static void initSounds(Context context)
    {
        int S1 = R.raw.beep9;
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap(4);
        soundPoolMap.put( R.raw.button1, soundPool.load(context, R.raw.button1, 1) );
        soundPoolMap.put( R.raw.beep9, soundPool.load(context, R.raw.beep9, 2) );
        soundPoolMap.put( R.raw.beep16, soundPool.load(context, R.raw.beep16, 3) );
        soundPoolMap.put( R.raw.button50, soundPool.load(context, R.raw.button50, 4) );

    }
    /** Play a given sound in the soundPool */
    public static void playSound(Context context, int soundID, float volume, int repeat)
    {
        if(soundPool == null || soundPoolMap == null)
        {
            initSounds(context);
        }

        // play sound with same right and left volume, with a priority of 1,
        // zero repeats (i.e play once), and a playback rate of 1f
        soundPool.play(soundID, volume, volume, 1, repeat, 1f);
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public class MyAsyncTask extends AsyncTask<String, Void, String>
    {
        private static final int REGISTRATION_TIMEOUT = 3 * 1000;
        private static final int WAIT_TIMEOUT = 30 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();

        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content =  null;
        private boolean error = false;

        private Context mContext;
        private int NOTIFICATION_ID = 1;
        private Notification mNotification;
        private NotificationManager mNotificationManager;

        public MyAsyncTask(Context context)
        {
            this.mContext = context;
            //Get the notification manager
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        protected void onPreExecute()
        {
//			 createNotification("Data download is in progress","");
        }
        //		 @Override
        protected String doInBackground(String... url)
        {

            try {
                HttpGet request = new HttpGet(url[0]);
                HttpClient client = new DefaultHttpClient();
                HttpResponse httpResponse;

                httpResponse = client.execute(request);
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                String message = httpResponse.getStatusLine().getReasonPhrase();

                HttpEntity entity = httpResponse.getEntity();

                if (entity != null) {

                    InputStream instream = entity.getContent();
                    String response = convertStreamToString(instream);

//		     			createNotification("Resposta", response);

                    // Closing the input stream will trigger connection release
                    instream.close();
                    return response;
                }
            }

            catch (ClientProtocolException e)
            {
                Log.w("HTTP2:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
                createNotification("RespostaErro", content);
            } catch (IOException e)
            {
                Log.w("HTTP3:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
                createNotification("RespostaErro", content);
            }catch (Exception e)
            {
                Log.w("HTTP4:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
                createNotification("RespostaErro", content);
            }
            return content;
        }
        protected void onCancelled()
        {
//			createNotification("Error occured during data download",content);
        }
        protected void onPostExecute(String content)
        {
            super.onPostExecute(content);

            try {
                MainActivity.this.checkResponseForIntent(content);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        private void createNotification(String contentTitle, String contentText)
        {

            //Build the notification using Notification.Builder
            Notification.Builder builder = new Notification.Builder(mContext)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText);

            //Get current notification
            mNotification = builder.getNotification();
            //Show the notification
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }

    public class MyAsyncTaskUserAnswer extends AsyncTask<String, Void, String>
    {
        private static final int REGISTRATION_TIMEOUT = 3 * 1000;
        private static final int WAIT_TIMEOUT = 30 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();

        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content =  null;
        private boolean error = false;

        private Context mContext;
        private int NOTIFICATION_ID = 1;
        private Notification mNotification;
        private NotificationManager mNotificationManager;

        public MyAsyncTaskUserAnswer(Context context)
        {
            this.mContext = context;
            //Get the notification manager
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        protected void onPreExecute()
        {
//			 createNotification("Data download is in progress","");
        }
        //		 @Override
        protected String doInBackground(String... url)
        {

            try {
                HttpGet request = new HttpGet(url[0]);
                HttpClient client = new DefaultHttpClient();
                HttpResponse httpResponse;

                httpResponse = client.execute(request);
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                String message = httpResponse.getStatusLine().getReasonPhrase();

                HttpEntity entity = httpResponse.getEntity();

                if (entity != null) {

                    InputStream instream = entity.getContent();
                    String response = convertStreamToString(instream);

//		     			createNotification("Resposta", response);

                    // Closing the input stream will trigger connection release
                    instream.close();
                    return response;
                }
            }

            catch (ClientProtocolException e)
            {
                Log.w("HTTP2:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
                createNotification("RespostaErro", content);
            } catch (IOException e)
            {
                Log.w("HTTP3:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
                createNotification("RespostaErro", content);
            }catch (Exception e)
            {
                Log.w("HTTP4:",e );
                content = e.getMessage();
                error = true;
                cancel(true);
                createNotification("RespostaErro", content);
            }
            return content;
        }
        protected void onCancelled()
        {
//			createNotification("Error occured during data download",content);
        }
        protected void onPostExecute(String content)
        {
            super.onPostExecute(content);

            try {
                MainActivity.this.checkResponseForIntentUserAnswer(content);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        private void createNotification(String contentTitle, String contentText)
        {

            //Build the notification using Notification.Builder
            Notification.Builder builder = new Notification.Builder(mContext)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText);

            //Get current notification
            mNotification = builder.getNotification();
            //Show the notification
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }



}

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}
